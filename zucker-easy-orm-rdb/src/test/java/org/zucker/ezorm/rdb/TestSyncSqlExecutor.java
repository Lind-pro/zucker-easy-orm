package org.zucker.ezorm.rdb;

import lombok.AllArgsConstructor;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.executor.jdbc.JdbcSyncSqlExecutor;

import java.sql.Connection;

/**
 * @auther: lind
 * @since: 1.0
 */
@AllArgsConstructor
public class TestSyncSqlExecutor extends JdbcSyncSqlExecutor {

    private ConnectionProvider provider;

    @Override
    public Connection getConnection(SqlRequest sqlRequest) {
        return provider.getConnection();
    }

    @Override
    public void releaseConnection(Connection connection, SqlRequest sqlRequest) {
        provider.releaseConnect(connection);
    }
}
