package org.zucker.ezorm.rdb.operator;

import org.zucker.ezorm.rdb.metadata.RDBDatabaseMetadata;
import org.zucker.ezorm.rdb.metadata.dialect.Dialect;

/**
 * @auther: lind
 * @since: 1.0
 */
public class DefaultDatabaseOperatorTest {

    private RDBDatabaseMetadata database;

    private DatabaseOperator operator;

    public void init(){
        database = new RDBDatabaseMetadata(Dialect.H2)
    }
}
