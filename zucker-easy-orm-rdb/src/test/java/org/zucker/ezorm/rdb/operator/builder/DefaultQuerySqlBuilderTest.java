package org.zucker.ezorm.rdb.operator.builder;

import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.metadata.RDBDatabaseMetadata;
import org.zucker.ezorm.rdb.metadata.RDBSchemaMetadata;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.metadata.dialect.Dialect;
import org.zucker.ezorm.rdb.metadata.key.ForeignKeyBuilder;
import org.zucker.ezorm.rdb.operator.dml.JoinType;
import org.zucker.ezorm.rdb.operator.dml.query.BuildParameterQueryOperator;
import org.zucker.ezorm.rdb.operator.dml.query.Orders;

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

    @Test
    public void testAutoJoin() {
        BuildParameterQueryOperator query = new BuildParameterQueryOperator("test");
        query.select("id", "info.comment")
                .where(dsl -> dsl.is("info.comment", "1234"));

        DefaultQuerySqlBuilder sqlBuilder = DefaultQuerySqlBuilder.of(schema);
        SqlRequest sqlRequest = sqlBuilder.build(query.getParameter());
        System.out.println(sqlRequest);
        String sql = sqlRequest.getSql();
        System.out.println(sql);
        Assert.assertTrue(sql.contains("where"));
        Assert.assertTrue(sql.contains("join"));
    }

    @Test
    public void test() {
        BuildParameterQueryOperator query = new BuildParameterQueryOperator("test");
        query.select("*")
                .orderBy(Orders.count("name"), Orders.desc("info.comment"))
                .paging(0, 10);

        DefaultQuerySqlBuilder sqlBuilder = DefaultQuerySqlBuilder.of(schema);
        long time = System.currentTimeMillis();
        SqlRequest sqlRequest = sqlBuilder.build(query.getParameter());

        System.out.println(System.currentTimeMillis() - time);
        Assert.assertNotNull(sqlRequest);
        Assert.assertNotNull(sqlRequest.getSql());
        System.out.println(sqlRequest.toNativeSql());
    }

}
