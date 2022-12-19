package org.zucker.ezorm.rdb.operator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.zucker.ezorm.rdb.TestJdbcReactiveSqlExecutor;
import org.zucker.ezorm.rdb.TestSyncSqlExecutor;
import org.zucker.ezorm.rdb.executor.SyncSqlExecutor;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrappers;
import org.zucker.ezorm.rdb.metadata.RDBDatabaseMetadata;
import org.zucker.ezorm.rdb.metadata.dialect.Dialect;
import org.zucker.ezorm.rdb.supports.h2.H2ConnectionProvider;
import org.zucker.ezorm.rdb.supports.h2.H2SchemaMetadata;
import org.zucker.ezorm.rdb.supports.h2.H2TableMetadataParser;

import java.io.Serializable;

/**
 * @auther: lind
 * @since: 1.0
 */
public class DefaultDatabaseOperatorTest {

    private RDBDatabaseMetadata database;

    private DatabaseOperator operator;

    @Before
    public void init() {
        database = new RDBDatabaseMetadata(Dialect.H2);

        SyncSqlExecutor sqlExecutor = new TestSyncSqlExecutor(new H2ConnectionProvider());
        database.addFeature(sqlExecutor);
        database.addFeature(new TestJdbcReactiveSqlExecutor(new H2ConnectionProvider()));
        H2SchemaMetadata schema = new H2SchemaMetadata("PUBLIC");

        schema.addFeature(new H2TableMetadataParser(schema));
        database.addSchema(schema);
        database.setCurrentSchema(schema);

        operator = DefaultDatabaseOperator.of(database);

    }

    @Test
    public void testDDLCreate() {

        operator.ddl()
                .createOrAlter("test_ddl_create")
                .addColumn().name("id").varchar(32).primaryKey().comment("ID").commit()
                .addColumn().name("name").varchar(64).notNull().comment("名称").commit()
                .addColumn().name("comment").columnDef("varchar(32) not null default '1'").commit()
                .index().name("index_").column("name").commit()
                .commit()
                .sync();

        operator.dml()
                .insert("test_ddl_create")
                .value("id", "1234")
                .value("name", "名称")
                .execute()
                .sync();

        int sum = operator.dml()
                .query("test_ddl_create")
                .select("comment")
                .fetch(ResultWrappers.mapStream())
                .sync()
                .map(map -> map.get("comment"))
                .map(String::valueOf)
                .mapToInt(Integer::valueOf)
                .sum();

        Assert.assertEquals(sum, 1);
    }

    @Getter
    @Setter
    @AllArgsConstructor(staticName = "of")
    @NoArgsConstructor
    public static class TestEntity implements Serializable {
        private String id;
        private String name;
        private int status;
    }
}
