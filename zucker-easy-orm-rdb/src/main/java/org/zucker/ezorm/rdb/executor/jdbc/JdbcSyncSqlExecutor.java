package org.zucker.ezorm.rdb.executor.jdbc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.executor.SyncSqlExecutor;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @auther: lind
 * @since: 1.0
 */
@Slf4j
public abstract class JdbcSyncSqlExecutor extends JdbcSqlExecutor implements SyncSqlExecutor {

    public JdbcSyncSqlExecutor() {
        super(log);
    }

    public abstract Connection getConnection(SqlRequest sqlRequest);

    public abstract void releaseConnection(Connection connection, SqlRequest sqlRequest);

    @SneakyThrows
    @Override
    protected void releaseStatement(Statement statement) {
        statement.close();
    }

    @SneakyThrows
    @Override
    protected void releaseResultSet(ResultSet resultSet) {
        resultSet.close();
    }

    @SneakyThrows
    @Override
    public int update(SqlRequest request) {
        Connection connection = getConnection(request);
        try {
            return doUpdate(connection, request);
        } finally {
            releaseConnection(connection, request);
        }
    }

    @SneakyThrows
    @Override
    public void execute(SqlRequest request) {
        Connection connection = getConnection(request);
        try {
            doExecute(connection, request);
        } finally {
            releaseConnection(connection, request);
        }
    }

    @SneakyThrows
    @Override
    public <T, R> R select(SqlRequest request, ResultWrapper<T, R> wrapper) {
        Connection connection = getConnection(request);
        try {
            return doSelect(connection, request, wrapper);
        } finally {
            releaseConnection(connection, request);
        }
    }
}
