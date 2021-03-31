package org.zucker.ezorm.rdb.operator;

import org.zucker.ezorm.rdb.metadata.RDBDatabaseMetadata;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface DatabaseOperator {

    RDBDatabaseMetadata getMetadata();

    DMLOperator dml();

    DDLOperator ddl();

    SQLOperator sql();
}
