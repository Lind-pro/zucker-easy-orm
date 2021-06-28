package org.zucker.ezorm.rdb.operator.dml.query;

import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.executor.SyncSqlExecutor;
import org.zucker.ezorm.rdb.executor.reactive.ReactiveSqlExecutor;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrapper;
import org.zucker.ezorm.rdb.metadata.RDBDatabaseMetadata;
import org.zucker.ezorm.rdb.metadata.TableOrViewMetadata;
import org.zucker.ezorm.rdb.utils.ExceptionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

/**
 * @auther: lind
 * @since: 1.0
 */
class DefaultQueryResultOperator<E, R> implements QueryResultOperator<E, R> {

    private Supplier<SqlRequest> sqlRequest;

    private RDBDatabaseMetadata metadata;

    private ResultWrapper<E, R> wrapper;

    public DefaultQueryResultOperator(Supplier<SqlRequest> sqlRequest,
                                      TableOrViewMetadata tableOrViewMetadata,
                                      ResultWrapper<E, R> wrapper) {
        this.sqlRequest = sqlRequest;
        this.metadata = tableOrViewMetadata.getSchema().getDatabase();
        this.wrapper = wrapper;
    }

    protected ResultWrapper<E, R> getWrapper() {
        return wrapper;
    }

    @Override
    public R sync() {
        return ExceptionUtils.translation(() -> metadata
                .findFeatureNow(SyncSqlExecutor.ID)
                .select(sqlRequest.get(), getWrapper()), metadata);
    }

    @Override
    public Flux<E> reactive() {
        return Flux.defer(() -> metadata.findFeatureNow(ReactiveSqlExecutor.ID)
                .select(Mono.fromSupplier(sqlRequest), getWrapper())
                .onErrorMap(error -> ExceptionUtils.translation(metadata, error)));
    }
}
