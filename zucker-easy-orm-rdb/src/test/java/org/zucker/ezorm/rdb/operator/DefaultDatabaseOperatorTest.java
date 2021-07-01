package org.zucker.ezorm.rdb.operator;

import org.zucker.ezorm.rdb.TestSyncSqlExecutor;
import org.zucker.ezorm.rdb.executor.SyncSqlExecutor;
import org.zucker.ezorm.rdb.metadata.RDBDatabaseMetadata;
import org.zucker.ezorm.rdb.metadata.dialect.Dialect;
import org.zucker.ezorm.rdb.supports.h2.H2ConnectionProvider;

/**
 * @auther: lind
 * @since: 1.0
 */
public class DefaultDatabaseOperatorTest {

    private RDBDatabaseMetadata database;

    private DatabaseOperator operator;

    public void init(){
        database = new RDBDatabaseMetadata(Dialect.H2);

        SyncSqlExecutor sqlExecutor = new TestSyncSqlExecutor(new H2ConnectionProvider());
        database.addFeature(sqlExecutor);


    }
}
