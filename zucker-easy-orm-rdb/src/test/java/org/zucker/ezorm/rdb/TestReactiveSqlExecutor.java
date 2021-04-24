package org.zucker.ezorm.rdb;

import io.r2dbc.spi.Connection;
import lombok.extern.slf4j.Slf4j;
import org.zucker.ezorm.rdb.executor.reactive.r2dbc.R2dbcReactiveSqlExecutor;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

/**
 * @auther: lind
 * @since: 1.0
 */
@Slf4j
public class TestReactiveSqlExecutor extends R2dbcReactiveSqlExecutor {

    private R2dbConnectionProvider provider;

    private String symbol;

    private int bindIndex = 1;

    public TestReactiveSqlExecutor(String symbol, R2dbConnectionProvider provider) {
        this.provider = provider;
        this.symbol = symbol;
    }

    public TestReactiveSqlExecutor(String symbol, int bindIndex, R2dbConnectionProvider provider) {
        this.symbol = symbol;
        this.bindIndex = bindIndex;
        this.provider = provider;
    }

    public TestReactiveSqlExecutor(R2dbConnectionProvider provider) {
        this(null, provider);
    }

    @Override
    protected Mono<Connection> getConnection() {
        return provider.getConnection().doOnNext(connection -> log.debug("get connection {}", connection));
    }

    @Override
    protected void releaseConnection(SignalType type, Connection connection) {
        log.debug("release connection {}", connection);
        provider.releaseConnection(connection);
    }

    @Override
    protected int getBindFirstIndex() {
        return bindIndex;
    }

    @Override
    protected String getBindSymbol() {
        if (symbol != null) {
            return symbol;
        }
        return super.getBindSymbol();
    }
}
