package org.zucker.ezorm.rdb.supports.mysql;

import org.zucker.ezorm.rdb.metadata.RDBSchemaMetadata;

/**
 * @auther: lind
 * @since: 1.0
 */
public class MysqlSchemaMetadata extends RDBSchemaMetadata {

    public MysqlSchemaMetadata(String name) {
        super(name);
        addFeature(new MysqlCreateTableSqlBuilder());
        addFeature(new MysqlAlterTableSqlBuilder());
        addFeature(new MysqlPaginator());

        //TODO
    }
}
