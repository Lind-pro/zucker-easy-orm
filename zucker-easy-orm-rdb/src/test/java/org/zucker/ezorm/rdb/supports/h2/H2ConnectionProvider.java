package org.zucker.ezorm.rdb.supports.h2;

import lombok.SneakyThrows;
import org.h2.Driver;
import org.zucker.ezorm.rdb.ConnectionProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;

/**
 * @auther: lind
 * @since: 1.0
 */
public class H2ConnectionProvider implements ConnectionProvider {

    private Connection connection;

    public H2ConnectionProvider() {
        Driver.load();
        try {
            connection = DriverManager.getConnection("jdbc:h2:mem:ezorm" + new Random().nextInt(10000), "sa", "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @SneakyThrows
    public Connection getConnection() {
        return connection;
    }

    @Override
    @SneakyThrows
    public void releaseConnect(Connection connection) {

    }
}
