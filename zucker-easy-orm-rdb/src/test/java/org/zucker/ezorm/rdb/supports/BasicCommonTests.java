package org.zucker.ezorm.rdb.supports;

import lombok.extern.slf4j.Slf4j;
import org.zucker.ezorm.core.DefaultValue;
import org.zucker.ezorm.core.DefaultValueGenerator;
import org.zucker.ezorm.core.meta.ObjectMetadata;
import org.zucker.ezorm.rdb.executor.SyncSqlExecutor;
import org.zucker.ezorm.rdb.mapping.SyncRepository;
import org.zucker.ezorm.rdb.mapping.defaults.record.Record;
import org.zucker.ezorm.rdb.metadata.RDBDatabaseMetadata;
import org.zucker.ezorm.rdb.metadata.RDBSchemaMetadata;
import org.zucker.ezorm.rdb.metadata.dialect.Dialect;
import org.zucker.ezorm.rdb.operator.DefaultDatabaseOperator;

import java.util.UUID;

/**
 * @auther: lind
 * @since: 1.0
 */
@Slf4j
public abstract class BasicCommonTests {

    private SyncRepository<BasicTestEntity, String> repository;
    private SyncRepository<Record, String> addressRepository;

    protected abstract RDBSchemaMetadata getSchema();

    protected abstract Dialect getDialect();

    protected abstract SyncSqlExecutor getSqlExecutor();

    protected RDBDatabaseMetadata getDatabase() {
        RDBDatabaseMetadata metadata = new RDBDatabaseMetadata(getDialect());

        RDBSchemaMetadata schema = getSchema();
        schema.addFeature(new DefaultValueGenerator() {

            @Override
            public String getName() {
                return "UUID";
            }

            @Override
            public String getSortId() {
                return "uuid";
            }

            @Override
            public DefaultValue generate(ObjectMetadata metadata) {
                return () -> UUID.randomUUID().toString().replace("-", "");
            }
        });

        log.debug(schema.toString());
        metadata.setCurrentSchema(schema);
        metadata.addSchema(schema);
        metadata.addFeature(getSqlExecutor());
        return metadata;
    }

    public void init(){
        RDBDatabaseMetadata metadata = getDatabase();
        DefaultDatabaseOperator operator = DefaultDatabaseOperator.of(metadata);

        new Jpa
    }
}
