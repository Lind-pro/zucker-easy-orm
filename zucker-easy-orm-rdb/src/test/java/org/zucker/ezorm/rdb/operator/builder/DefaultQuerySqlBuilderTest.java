package org.zucker.ezorm.rdb.operator.builder;

import org.junit.Before;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.metadata.RDBDatabaseMetadata;
import org.zucker.ezorm.rdb.metadata.RDBSchemaMetadata;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.metadata.dialect.Dialect;

import java.sql.JDBCType;

public class DefaultQuerySqlBuilderTest {

    RDBSchemaMetadata schema;

    @Before
    public void init() {
        RDBDatabaseMetadata database = new RDBDatabaseMetadata(Dialect.H2);
        schema = new RDBSchemaMetadata("DEFAULT");

        database.setCurrentSchema(schema);
        database.addSchema(schema);

        RDBTableMetadata table = new RDBTableMetadata();
        table.setName("test");
        RDBTableMetadata detail = new RDBTableMetadata();
        detail.setName("detail");

        schema.addTable(table);
        schema.addTable(detail);

        {
            RDBColumnMetadata id = new RDBColumnMetadata();
            id.setName("id");
            id.setJdbcType(JDBCType.VARCHAR, String.class);
            id.setLength(32);

            RDBColumnMetadata name = new RDBColumnMetadata();
            name.setName("name");
            name.setJdbcType(JDBCType.VARCHAR, String.class);
            name.setLength(64);

            table.addColumn(id);
            table.addColumn(name);

        }
    }
}
