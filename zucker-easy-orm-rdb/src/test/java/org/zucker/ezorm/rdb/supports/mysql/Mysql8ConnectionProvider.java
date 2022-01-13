package org.zucker.ezorm.rdb.supports.mysql;

import com.mysql.jdbc.Driver;
import lombok.SneakyThrows;
import org.zucker.ezorm.rdb.ConnectionProvider;
import org.zucker.ezorm.rdb.TestSyncSqlExecutor;
import org.zucker.ezorm.rdb.executor.SqlRequests;
import org.zucker.ezorm.rdb.executor.SyncSqlExecutor;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrappers;

import java.sql.Connection;
import java.sql.DriverManager;

/**TestSyncSqlExecutor
 * @auther: lind
 * @since: 1.0
 */
public class Mysql8ConnectionProvider implements ConnectionProvider {

    static {
        Driver.getPlatform();
    }

    @Override
    @SneakyThrows
    public Connection getConnection() {
        String username = System.getProperty("mysql8.username", "root");
        String password = System.getProperty("mysql8.password", "root");
        String url = System.getProperty("mysql8.url", "127.0.0.1:13307");
        String db = System.getProperty("mysql8.db", "ezorm");

        return DriverManager.getConnection("jdbc:mysql://" + url + "/" + db + "?allowPublicKeyRetrieval=true&useSSL=false", username, password);
    }

    @Override
    @SneakyThrows
    public void releaseConnect(Connection connection) {
        connection.close();
    }

    public static void main(String[] args) {
        SyncSqlExecutor executor = new TestSyncSqlExecutor(new Mysql8ConnectionProvider());
        System.out.println(executor.select(SqlRequests.of("show index from ezorm.table_name"), ResultWrappers.mapList()));
    }
}
