package org.zucker.ezorm.rdb.supports.mysql;

import org.zucker.ezorm.rdb.TestSyncSqlExecutor;
import org.zucker.ezorm.rdb.executor.SyncSqlExecutor;
import org.zucker.ezorm.rdb.metadata.RDBSchemaMetadata;
import org.zucker.ezorm.rdb.metadata.dialect.Dialect;
import org.zucker.ezorm.rdb.supports.BasicCommonTests;

/**
 * @auther: lind
 * @since: 1.0
 */
public class Mysql8BasicTest extends BasicCommonTests {


    @Override
    protected RDBSchemaMetadata getSchema() {
        return new MysqlSchemaMetadata("ezorm");
    }

    @Override
    protected Dialect getDialect() {
        return Dialect.MYSQL;
    }

    @Override
    protected SyncSqlExecutor getSqlExecutor() {
        return new TestSyncSqlExecutor(new Mysql8ConnectionProvider());
    }
}
