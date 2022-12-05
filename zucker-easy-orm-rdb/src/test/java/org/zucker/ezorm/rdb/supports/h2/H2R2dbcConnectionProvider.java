package org.zucker.ezorm.rdb.supports.h2;

import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.zucker.ezorm.rdb.R2dbConnectionProvider;
import reactor.core.publisher.Mono;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

public class H2R2dbcConnectionProvider implements R2dbConnectionProvider {

    private Connection connection;

    public H2R2dbcConnectionProvider() {
        H2ConnectionFactory connectionFactory = (H2ConnectionFactory) ConnectionFactories.get(ConnectionFactoryOptions.builder()
                .option(DRIVER, "h2")
                .option(PROTOCOL, "mem")
                .option(DATABASE, "test_" + Math.random())
                .build());
        connection = connectionFactory.create().block();
    }

    @Override
    public Mono<Connection> getConnection() {
        return Mono.just(connection);
    }

    @Override
    public void releaseConnection(Connection connection) {

    }
}
