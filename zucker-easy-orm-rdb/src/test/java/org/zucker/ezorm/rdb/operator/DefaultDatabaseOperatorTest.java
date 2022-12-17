package org.zucker.ezorm.rdb.operator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.Before;
import org.zucker.ezorm.rdb.TestJdbcReactiveSqlExecutor;
import org.zucker.ezorm.rdb.TestSyncSqlExecutor;
import org.zucker.ezorm.rdb.executor.SyncSqlExecutor;
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
