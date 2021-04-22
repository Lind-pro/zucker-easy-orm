package org.zucker.ezorm.rdb;

import reactor.core.publisher.Mono;

import java.sql.Connection;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface R2dbConnectionProvider {
    Mono<Connection> getConnection();

    void releaseConnection(Connection connection);
}
