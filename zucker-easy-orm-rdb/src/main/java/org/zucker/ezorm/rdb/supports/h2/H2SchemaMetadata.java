package org.zucker.ezorm.rdb.supports.h2;

import org.zucker.ezorm.rdb.metadata.RDBSchemaMetadata;

/**
 * @auther: lind
 * @since: 1.0
 */
public class H2SchemaMetadata extends RDBSchemaMetadata {
    public H2SchemaMetadata(String name) {
        super(name);
        addFeature(new H2CreateTableSqlBuilder());
        addFeature(new H2AlterTableSqlBuilder());
        addFeature(new H2Paginator());
//        addFeature(new);
    }
}
