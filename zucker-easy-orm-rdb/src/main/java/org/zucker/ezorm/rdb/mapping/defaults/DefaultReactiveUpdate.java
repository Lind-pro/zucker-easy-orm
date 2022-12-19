package org.zucker.ezorm.rdb.mapping.defaults;

import org.zucker.ezorm.rdb.events.ContextKeyValue;
import org.zucker.ezorm.rdb.mapping.EntityColumnMapping;
import org.zucker.ezorm.rdb.mapping.ReactiveUpdate;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.dml.update.UpdateOperator;
import org.zucker.ezorm.rdb.operator.dml.upsert.UpsertOperator;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

/**
 * @author lind
 * @since 1.0
 */
public class DefaultReactiveUpdate<E> extends DefaultUpdate<E, ReactiveUpdate<E>> implements ReactiveUpdate<E> {

    public DefaultReactiveUpdate(RDBTableMetadata table,
                                 UpdateOperator operator,
                                 EntityColumnMapping mapping,
                                 ContextKeyValue<?>... keyValues) {
        super(table, operator, mapping, keyValues);
    }

    private BiFunction<ReactiveUpdate<E>, Mono<Integer>, Mono<Integer>> mapper = (update, mono) -> mono;


    @Override
    public Mono<Integer> execute() {
        return mapper.apply(this, doExecute().reactive());
    }

    @Override
    public ReactiveUpdate<E> onExecute(BiFunction<ReactiveUpdate<E>, Mono<Integer>, Mono<Integer>> consumer) {
        this.mapper = this.mapper.andThen(r -> consumer.apply(this, r));
        return this;
    }
}
