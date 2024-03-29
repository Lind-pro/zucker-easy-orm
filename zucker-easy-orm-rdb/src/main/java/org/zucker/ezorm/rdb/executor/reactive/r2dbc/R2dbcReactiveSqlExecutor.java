package org.zucker.ezorm.rdb.executor.reactive.r2dbc;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Result;
import io.r2dbc.spi.Statement;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.zucker.ezorm.core.CastUtil;
import org.zucker.ezorm.rdb.executor.BatchSqlRequest;
import org.zucker.ezorm.rdb.executor.DefaultColumnWrapperContext;
import org.zucker.ezorm.rdb.executor.NullValue;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.executor.reactive.ReactiveSqlExecutor;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrapper;
import org.zucker.ezorm.rdb.utils.SqlUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author lind
 * @since 1.0
 */
@Slf4j
public abstract class R2dbcReactiveSqlExecutor implements ReactiveSqlExecutor {

    @Getter
    @Setter
    private Logger logger = R2dbcReactiveSqlExecutor.log;

    protected abstract Mono<Connection> getConnection();

    protected abstract void releaseConnection(SignalType type, Connection connection);

    enum Interrupted {
        instance;

        static boolean nonInterrupted(Object o) {
            return o != instance;
        }
    }

    protected Flux<Result> doExecute(Connection connection, SqlRequest request) {
        return Flux
                .just(preparedStatement(connection.createStatement(request.getSql()), request))
                .flatMap(Statement::execute)
                .map(Result.class::cast)
                .doOnSubscribe(subscription -> SqlUtils.printSql(logger, request));
    }

    protected Flux<Result> doExecute(Flux<SqlRequest> sqlRequestFlux) {
        return this
                .getConnection()
                .flatMapMany(connection -> sqlRequestFlux
                        .flatMap(sqlRequest -> this.doExecute(connection, sqlRequest))
                        .doFinally(type -> releaseConnection(type, connection)));
    }

    @Override
    public Mono<Integer> update(Publisher<SqlRequest> request) {
        return this
                .doExecute(toFlux(request))
                .flatMap(result -> Mono.from(result.getRowsUpdated()).defaultIfEmpty(0))
                .doOnNext(count -> logger.debug("==>   Updated:{}", count))
                .collect(Collectors.summingInt(Integer::intValue))
                .defaultIfEmpty(0);
    }

    @Override
    public Mono<Void> execute(Publisher<SqlRequest> request) {
        return this.doExecute(toFlux(request)).then();
    }

    @Override
    public <E> Flux<E> select(Publisher<SqlRequest> request, ResultWrapper<E, ?> wrapper) {
        return this
                .toFlux(request)
                .as(this::doExecute)
                .flatMap(result ->
                        result.map((row, meta) -> {
                            ArrayList<String> columns = new ArrayList<>(meta.getColumnNames());
                            wrapper.beforeWrap(() -> columns);
                            E e = wrapper.newRowInstance();
                            for (int i = 0; i < columns.size(); i++) {
                                String column = columns.get(0);
                                DefaultColumnWrapperContext<E> context = new DefaultColumnWrapperContext<>(i, column, row.get(column), e);
                                wrapper.wrapColumn(context);
                                e = context.getRowInstance();
                            }
                            if (!wrapper.completedWrapRow(e)) {
                                return Interrupted.instance;
                            }
                            return e;
                        }))
                .takeWhile(Interrupted::nonInterrupted)
                .map(CastUtil::<E>cast)
                .doOnCancel(wrapper::completedWrap)
                .doOnComplete(wrapper::completedWrap);
    }


    protected Flux<SqlRequest> toFlux(Publisher<SqlRequest> request) {
        return Flux
                .from(request)
                .flatMap(sql -> {
                    if (sql instanceof BatchSqlRequest) {
                        return Flux.concat(Flux.just(sql), Flux.fromIterable(((BatchSqlRequest) sql).getBatch()));
                    }
                    return Flux.just(sql);
                })
                .filter(SqlRequest::isNotEmpty)
                .map(this::convertRequest);
    }

    protected SqlRequest convertRequest(SqlRequest sqlRequest) {
        return R2dbcSqlRequest.of(getBindFirstIndex(), getBindSymbol(), sqlRequest);
    }

    protected String getBindSymbol() {
        return "$";
    }

    protected int getBindFirstIndex() {
        return 1;
    }

    protected void bindNull(Statement statement, int index, Class<?> type) {
        if (type == Date.class) {
            type = LocalDateTime.class;
        }
        statement.bindNull(getBindSymbol() + (index + getBindFirstIndex()), type);
    }

    protected void bind(Statement statement, int index, Object value) {
        if (value instanceof Date) {
            value = ((Date) value).toInstant().atZone(ZoneOffset.systemDefault())
                    .toLocalDateTime();
        }
        statement.bind(getBindSymbol() + (index + getBindFirstIndex()), value);
    }

    protected Statement preparedStatement(Statement statement, SqlRequest request) {
        if (request.isEmpty() || request.getParameters() == null) {
            return statement;
        }
        int index = 0;
        for (Object parameter : request.getParameters()) {
            if (parameter == null) {
                bindNull(statement, index, String.class);
            } else if (parameter instanceof NullValue) {
                bindNull(statement, index, ((NullValue) parameter).getDataType().getJavaType());
            } else {
                bind(statement, index, parameter);
            }
            index++;
        }
        return statement;
    }
}
