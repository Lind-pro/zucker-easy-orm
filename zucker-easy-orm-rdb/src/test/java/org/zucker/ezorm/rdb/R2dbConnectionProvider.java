package org.zucker.ezorm.rdb;

import io.r2dbc.spi.Connection;
import reactor.core.publisher.Mono;


/**
 * @author lind
 * @since 1.0
 */
public interface R2dbConnectionProvider {
    Mono<Connection> getConnection();

    void releaseConnection(Connection connection);
}
