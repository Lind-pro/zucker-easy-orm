package org.zucker.ezorm.rdb.supports.h2;

import io.r2dbc.spi.Connection;
import org.zucker.ezorm.rdb.R2dbConnectionProvider;

public class H2R2dbcConnectionProvider implements R2dbConnectionProvider {
    @Override
    public Mono<Connection> getConnection() {
        return null;
    }

    @Override
    public void releaseConnection(Connection connection) {

    }
}
