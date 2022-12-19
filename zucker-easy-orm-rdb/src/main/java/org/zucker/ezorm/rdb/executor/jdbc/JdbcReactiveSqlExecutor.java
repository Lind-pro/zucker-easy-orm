package org.zucker.ezorm.rdb.executor.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.executor.reactive.ReactiveSqlExecutor;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrapper;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrappers;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Connection;
import java.util.stream.Collectors;

/**
 * @author lind
 * @since 1.0
 */
@Slf4j
public abstract class JdbcReactiveSqlExecutor extends JdbcSqlExecutor implements ReactiveSqlExecutor {

    public JdbcReactiveSqlExecutor() {
        super(log);
    }

    public abstract Mono<Connection> getConnection(SqlRequest sqlRequest);

    public abstract void releaseConnection(Connection connection, SqlRequest request);

    @Override
    public Mono<Integer> update(Publisher<SqlRequest> request) {
        return Mono.defer(() -> toFlux(request)
                .flatMap(sqlRequest -> getConnection(sqlRequest)
                        .flatMap(connection -> Mono.fromSupplier(() -> doUpdate(connection, sqlRequest))
                                .doFinally(type -> releaseConnection(connection, sqlRequest))))
                .collect(Collectors.summingInt(Integer::intValue)));
    }

    @Override
    public Mono<Void> execute(Publisher<SqlRequest> request) {
        return Mono.defer(() -> toFlux(request).flatMap(SqlRequest -> getConnection(SqlRequest)
                .flatMap(connection ->
                        Mono.fromSupplier(() -> {
                            doExecute(connection, SqlRequest);
                            return null;
                        }).doFinally(type -> releaseConnection(connection, SqlRequest)))).then());
    }

    @Override
    public <E> Flux<E> select(Publisher<SqlRequest> request, ResultWrapper<E, ?> wrapper) {
        return Flux.create(sink -> {
            Disposable disposable = toFlux(request)
                    .doFinally(type -> sink.complete())
                    .subscribe(sqlRequest -> getConnection(sqlRequest)
                            .subscribe(connection -> {
                                doSelect(connection, sqlRequest, ResultWrappers.consumer(wrapper, sink::next));
                                releaseConnection(connection, sqlRequest);
                            }));
            sink.onCancel(disposable)
                    .onDispose(disposable);
        });
    }

    protected Flux<SqlRequest> toFlux(Publisher<SqlRequest> request) {
        return Flux.from(request);
    }
}
