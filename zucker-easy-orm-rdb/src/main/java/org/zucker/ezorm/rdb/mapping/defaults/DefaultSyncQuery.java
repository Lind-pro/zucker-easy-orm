package org.zucker.ezorm.rdb.mapping.defaults;

import org.zucker.ezorm.rdb.events.ContextKeys;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrapper;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrappers;
import org.zucker.ezorm.rdb.mapping.EntityColumnMapping;
import org.zucker.ezorm.rdb.mapping.SyncQuery;
import org.zucker.ezorm.rdb.mapping.events.EventSupportWrapper;
import org.zucker.ezorm.rdb.mapping.events.MappingContextKeys;
import org.zucker.ezorm.rdb.mapping.events.MappingEventTypes;
import org.zucker.ezorm.rdb.metadata.TableOrViewMetadata;
import org.zucker.ezorm.rdb.operator.DMLOperator;

import java.util.List;
import java.util.Optional;

/**
 * @auther: lind
 * @since: 1.0
 */
public class DefaultSyncQuery<T> extends DefaultQuery<T, SyncQuery<T>> implements SyncQuery<T> {

    public DefaultSyncQuery(TableOrViewMetadata tableMetadata, EntityColumnMapping mapping, DMLOperator operator, ResultWrapper<T, ?> wrapper) {
        super(tableMetadata, mapping, operator, wrapper);
    }

    @Override
    public List<T> fetch() {
        return operator
                .query(tableMetadata)
                .context(param.getContext())
                .select(getSelectColumn())
                .selectExcludes(param.getExcludes())
                .where(param.getTerms())
                .orderBy(getSortOrder())
                .when(param.isPaging(), query -> query.paging(param.getPageIndex(), param.getPageSize()))
                .accept(queryOperator ->
                        tableMetadata.fireEvent(MappingEventTypes.select_before, eventContext ->
                                eventContext.set(
                                        ContextKeys.source(DefaultSyncQuery.this),
                                        MappingContextKeys.query(queryOperator),
                                        MappingContextKeys.dml(operator),
                                        MappingContextKeys.executorType("sync"),
                                        MappingContextKeys.type("fetch")
                                )))
                .fetch(EventSupportWrapper.eventWrapper(tableMetadata, ResultWrappers.list(wrapper), MappingContextKeys.executorType("sync"), MappingContextKeys.type("fetch")))
                .sync();

    }

    @Override
    public Optional<T> fetchOne() {
        return operator
                .query(tableMetadata)
                .context(param.getContext())
                .select(getSelectColumn())
                .where(param.getTerms())
                .orderBy(getSortOrder())
                .paging(0, 1)
                .accept(queryOperator ->
                        tableMetadata.fireEvent(
                                MappingEventTypes.select_before,
                                ContextKeys.source(DefaultSyncQuery.this),
                                MappingContextKeys.query(queryOperator),
                                MappingContextKeys.dml(operator),
                                MappingContextKeys.executorType("sync"),
                                MappingContextKeys.type("fetchOne")
                        ))
                .fetch(EventSupportWrapper.eventWrapper(tableMetadata, ResultWrappers.optional(ResultWrappers.single(wrapper)), MappingContextKeys.executorType("sync"), MappingContextKeys.type("fetchOne")))
                .sync();

    }

    @Override
    public int count() {
        return operator
                .query(tableMetadata)
                .context(param.getContext())
//                .select(); todo
    }
}
