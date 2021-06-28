package org.zucker.ezorm.rdb.operator.dml.query;

import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrapper;
import org.zucker.ezorm.rdb.metadata.TableOrViewMetadata;

/**
 * @auther: lind
 * @since: 1.0
 */
public class ExecutableQueryOperator extends BuildParameterQueryOperator {

    private final TableOrViewMetadata metadata;

    public ExecutableQueryOperator(TableOrViewMetadata metadata) {
        super(metadata.getName());
        this.metadata = metadata;
    }

    @Override
    public SqlRequest getSql() {
        return metadata.findFeatureNow(QuerySqlBuilder.ID).build(this.getParameter());
    }

    @Override
    public <E, R> QueryResultOperator<E, R> fetch(ResultWrapper<E, R> wrapper) {
        return new DefaultQueryResultOperator<>(this::getSql, metadata, wrapper);
    }
}
