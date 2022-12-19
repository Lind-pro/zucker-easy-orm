package org.zucker.ezorm.rdb;

import lombok.AllArgsConstructor;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.executor.jdbc.JdbcReactiveSqlExecutor;
import reactor.core.publisher.Mono;

import java.sql.Connection;

/**
 * @author lind
 * @since 1.0
 */
@AllArgsConstructor
public class TestJdbcReactiveSqlExecutor extends JdbcReactiveSqlExecutor {

    private ConnectionProvider provider;

    @Override
    public Mono<Connection> getConnection(SqlRequest sqlRequest) {
        return Mono.fromSupplier(provider::getConnection);
    }

    @Override
    public void releaseConnection(Connection connection, SqlRequest request) {
        provider.releaseConnect(connection);
    }
}
