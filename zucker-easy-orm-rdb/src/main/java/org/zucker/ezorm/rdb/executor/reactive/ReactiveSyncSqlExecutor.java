package org.zucker.ezorm.rdb.executor.reactive;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.executor.SyncSqlExecutor;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrapper;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/**
 * @auther: lind
 * @since: 1.0
 */
@AllArgsConstructor(staticName = "of")
public class ReactiveSyncSqlExecutor implements SyncSqlExecutor {

    private final ReactiveSqlExecutor sqlExecutor;

    @Override
    @SneakyThrows
    public int update(SqlRequest request) {
        return sqlExecutor
                .update(Mono.just(request))
                .toFuture()
                .get(30, TimeUnit.SECONDS);
    }

    @Override
    @SneakyThrows
    public void execute(SqlRequest request) {
        sqlExecutor
                .execute(Mono.just(request))
                .toFuture()
                .get(30, TimeUnit.SECONDS);
    }

    @Override
    @SneakyThrows
    public <T, R> R select(SqlRequest request, ResultWrapper<T, R> wrapper) {
        sqlExecutor.select(Mono.just(request), wrapper)
                .collectList()
                .toFuture()
                .get(30, TimeUnit.SECONDS);
        return wrapper.getResult();
    }
}
