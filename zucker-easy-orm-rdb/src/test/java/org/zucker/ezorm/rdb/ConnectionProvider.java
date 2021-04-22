package org.zucker.ezorm.rdb;

import java.sql.Connection;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface ConnectionProvider {
    Connection getConnection();

    void releaseConnect(Connection connection);
}

