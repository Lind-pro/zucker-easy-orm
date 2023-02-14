package org.zucker.ezorm.rdb.operator.builder;

import lombok.Getter;
import lombok.Setter;
import org.junit.Before;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.metadata.RDBDatabaseMetadata;
import org.zucker.ezorm.rdb.metadata.RDBSchemaMetadata;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.metadata.dialect.Dialect;
import org.zucker.ezorm.rdb.metadata.key.ForeignKeyBuilder;
import org.zucker.ezorm.rdb.operator.dml.JoinType;

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

        {
            RDBColumnMetadata id = new RDBColumnMetadata();
            id.setName("id");
            id.setJdbcType(JDBCType.VARCHAR, String.class);
            id.setLength(32);
            RDBColumnMetadata detailInfo = new RDBColumnMetadata();
            detailInfo.setName("comment");
            detailInfo.setJdbcType(JDBCType.VARCHAR, String.class);
            detailInfo.setLength(64);
            detail.addColumn(id);
            detail.addColumn(detailInfo);
        }

        table.addForeignKey(ForeignKeyBuilder.builder()
                .target("detail")
                .alias("info")
                .autoJoin(true)
                .joinType(JoinType.left)
                .build()
                .addColumn("id", "id"));
    }

    @Getter
    @Setter
    public class User {
        private String id;
    }
}