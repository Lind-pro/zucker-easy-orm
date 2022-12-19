package org.zucker.ezorm.rdb.mapping.defaults;

import org.reactivestreams.Publisher;
import org.zucker.ezorm.rdb.events.ContextKeyValue;
import org.zucker.ezorm.rdb.events.ContextKeys;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrapper;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrappers;
import org.zucker.ezorm.rdb.mapping.EntityColumnMapping;
import org.zucker.ezorm.rdb.mapping.ReactiveQuery;
import org.zucker.ezorm.rdb.mapping.events.DefaultReactiveResultHolder;
import org.zucker.ezorm.rdb.mapping.events.EventSupportWrapper;
import org.zucker.ezorm.rdb.mapping.events.MappingContextKeys;
import org.zucker.ezorm.rdb.mapping.events.MappingEventTypes;
import org.zucker.ezorm.rdb.metadata.TableOrViewMetadata;
import org.zucker.ezorm.rdb.operator.DMLOperator;
import org.zucker.ezorm.rdb.operator.dml.QueryOperator;
import org.zucker.ezorm.rdb.operator.dml.query.Selects;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * @author lind
 * @since 1.0
 */
public class DefaultReactiveQuery<T> extends DefaultQuery<T, ReactiveQuery<T>> implements ReactiveQuery<T> {

    public DefaultReactiveQuery(TableOrViewMetadata tableMetadata,
                                EntityColumnMapping mapping,
                                DMLOperator operator,
                                ResultWrapper<T, ?> wrapper,
                                ContextKeyValue<?>... keyValues) {
        super(tableMetadata, mapping, operator, wrapper, keyValues);
    }

    @Override
    public Flux<T> fetch() {
        return this
                .doFetch(operator.query(tableMetadata),
                        "fetch",
                        (_queryOperator) -> _queryOperator
                                .context(param.getContext())
                                .select(getSelectColumn())
                                .where(param.getTerms())
                                .orderBy(getSortOrder())
                                .when(param.isPaging(), query -> query.paging(param.getPageIndex(), param.getPageSize()))
                                .fetch(EventSupportWrapper.eventWrapper(tableMetadata, wrapper, MappingContextKeys.executorType("reactive"), MappingContextKeys.type("fetch")))
                                .reactive());
    }

    @Override
    public Mono<Integer> count() {
        QueryOperator queryOperator = operator.query(tableMetadata)
                .select(Selects.count1().as("total"));
        return this.doFetch(queryOperator, "count", _opt -> _opt
                .context(param.getContext())
                .where(param.getTerms())
                .fetch(ResultWrappers.column("total", Number.class::cast))
                .reactive()
                .map(Number::intValue)
                .reduce(Math::addExact)
                .switchIfEmpty(Mono.just(0))
        ).as(Mono::from);
    }

    @Override
    public Mono<T> fetchOne() {
        return this.doFetch(operator.query(tableMetadata), "fetchOne",
                        (_queryOperator) -> _queryOperator
                                .context(param.getContext())
                                .select(getSelectColumn())
                                .where(param.getTerms())
                                .orderBy(getSortOrder())
                                .paging(0, 1)
                                .fetch(EventSupportWrapper.eventWrapper(tableMetadata, wrapper, MappingContextKeys.executorType("reactive"), MappingContextKeys.type("fetchOne")))
                                .reactive())
                .as(Mono::from);
    }

    private <O> Flux<O> doFetch(QueryOperator queryOperator, String type, Function<QueryOperator, Publisher<O>> executor) {
        DefaultReactiveResultHolder holder = new DefaultReactiveResultHolder();
        tableMetadata
                .fireEvent(MappingEventTypes.select_before, eventContext -> {
                    eventContext.set(
                            ContextKeys.source(DefaultReactiveQuery.this),
                            MappingContextKeys.query(queryOperator),
                            MappingContextKeys.dml(operator),
                            ContextKeys.tableMetadata(tableMetadata),
                            MappingContextKeys.columnMapping(columnMapping),
                            MappingContextKeys.reactiveResultHolder.value(holder),
                            MappingContextKeys.queryParam.value(param),
                            MappingContextKeys.executorType("reactive"),
                            MappingContextKeys.type(type)
                    );
                });
        return holder
                .doBefore()
                .thenMany(Flux.defer(() -> executor.apply(queryOperator)));
    }
}
