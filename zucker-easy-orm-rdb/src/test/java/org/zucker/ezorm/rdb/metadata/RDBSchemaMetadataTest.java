package org.zucker.ezorm.rdb.metadata;

import org.zucker.ezorm.rdb.TestSyncSqlExecutor;
import org.zucker.ezorm.rdb.executor.SyncSqlExecutor;
import org.zucker.ezorm.rdb.metadata.dialect.Dialect;
import org.zucker.ezorm.rdb.supports.h2.H2ConnectionProvider;
import org.zucker.ezorm.rdb.supports.h2.H2TableMetadataParser;

public class RDBSchemaMetadataTest {

    public SyncSqlExecutor executor;

    public RDBSchemaMetadata schema;

    public void init() {
        RDBDatabaseMetadata database = new RDBDatabaseMetadata(Dialect.H2);

        schema = new RDBSchemaMetadata("PUBLIC");
        schema.setDatabase(database);
        executor = new TestSyncSqlExecutor(new H2ConnectionProvider());

        schema.addFeature(executor);
        schema.addFeature(new H2TableMetadataParser(schema));

        database.addSchema(schema);
        database.setCurrentSchema(schema);
    }
}
