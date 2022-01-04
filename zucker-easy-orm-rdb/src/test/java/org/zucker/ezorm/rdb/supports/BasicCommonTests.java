package org.zucker.ezorm.rdb.supports;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.zucker.ezorm.core.DefaultValue;
import org.zucker.ezorm.core.DefaultValueGenerator;
import org.zucker.ezorm.core.meta.ObjectMetadata;
import org.zucker.ezorm.rdb.executor.SqlRequests;
import org.zucker.ezorm.rdb.executor.SyncSqlExecutor;
import org.zucker.ezorm.rdb.mapping.EntityColumnMapping;
import org.zucker.ezorm.rdb.mapping.MappingFeatureType;
import org.zucker.ezorm.rdb.mapping.SyncRepository;
import org.zucker.ezorm.rdb.mapping.defaults.DefaultSyncRepository;
import org.zucker.ezorm.rdb.mapping.defaults.record.Record;
import org.zucker.ezorm.rdb.mapping.jpa.JpaEntityTableMetadataParser;
import org.zucker.ezorm.rdb.mapping.wrapper.EntityResultWrapper;
import org.zucker.ezorm.rdb.metadata.RDBDatabaseMetadata;
import org.zucker.ezorm.rdb.metadata.RDBSchemaMetadata;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.metadata.dialect.Dialect;
import org.zucker.ezorm.rdb.operator.DefaultDatabaseOperator;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Before
    public void init() {
        RDBDatabaseMetadata metadata = getDatabase();
        DefaultDatabaseOperator operator = DefaultDatabaseOperator.of(metadata);

        JpaEntityTableMetadataParser parser = new JpaEntityTableMetadataParser();
        parser.setDatabaseMetadata(metadata);

        parser.parseTableMetadata(Address.class)
                .ifPresent(address -> {
                    operator.ddl()
                            .createOrAlter(address)
                            .commit()
                            .sync();
                });

        RDBTableMetadata table = parser
                .parseTableMetadata(BasicTestEntity.class)
                .orElseThrow(NullPointerException::new);

        operator.ddl()
                .createOrAlter(table)
                .commit()
                .sync();

        EntityResultWrapper<BasicTestEntity> wrapper = new EntityResultWrapper<>(BasicTestEntity::new);
        wrapper.setMapping(table
                .<EntityColumnMapping>getFeature(MappingFeatureType.columnPropertyMapping.createFeatureId(BasicTestEntity.class))
                .orElseThrow(NullPointerException::new));

        repository = new DefaultSyncRepository<>(operator, table, BasicTestEntity.class, wrapper);
        addressRepository = operator.dml().createRepository("test_address");
    }

    @After
    public void after() {
        try {
            getSqlExecutor().execute(SqlRequests.of("drop table entity_test_table"));
        } catch (Exception e) {

        }
        try {
            getSqlExecutor().execute(SqlRequests.of("drop table test_address"));
        } catch (Exception e) {

        }
    }

    @Test
    public void testRepositoryInsertBatch() {
        List<BasicTestEntity> entities = Flux.range(0, 100)
                .map(integer -> BasicTestEntity.builder()
                        .balance(1000L)
                        .name("test:" + integer)
                        .createTime(new Date())
                        .tags(Arrays.asList("a", "b", "c", "d"))
                        .state((byte) 1)
                        .build())
                .collectList().block();
        Assert.assertEquals(100, repository.insertBatch(entities));
    }

    @Test
    public void testInsertDuplicate() {
        // 10次insert
        Assert.assertEquals(3, repository.insertBatch(
                Stream.of(1, 2, 2, 3, 1, 3)
                        .map(integer -> BasicTestEntity
                                .builder()
                                .id("test_dup_" + integer)
                                .balance(1000L)
                                .name("test2:" + integer)
                                .createTime(new Date())
                                .tags(Arrays.asList("a", "b", "c", "d"))
                                .state((byte) 1)
                                .stateEnum(StateEnum.enabled)
                                .build())
                        .collect(Collectors.toList())
        ));

        Assert.assertEquals(3,
                repository
                        .createDelete()
                        .like$(BasicTestEntity::getId, "test_dup_")
                        .execute());
    }

    public void testRepositorySave() {
        BasicTestEntity entity = BasicTestEntity
                .builder()
                .id("test_id_save")
                .balance(1000L)
                .name("test")
                .createTime(new Date())
                .tags(Arrays.asList("a", "b", "c", "d"))
                .state((byte) 1)
                .addressId("test")
                .doubleValue(1D)
                .bigDecimal(new BigDecimal("1.2"))
                .stateEnum(StateEnum.enabled)
                .build();

        Assert.assertEquals(repository.save(entity).getTotal(), 1);
        {
            BasicTestEntity inBase = repository
                    .createQuery()
                    .select("*")
                    .where("id", entity.getId())
                    .fetchOne()
                    .orElseThrow(UnsupportedOperationException::new);

            Assert.assertEquals(entity, inBase);
        }

        entity.setStateEnum(null);
        Assert.assertEquals(repository.save(entity).getTotal(), 1);

        BasicTestEntity inBase = repository
                .createQuery()
                .select("*")
                .where("id", entity.getId())
                .fetchOne()
                .orElseThrow(UnsupportedOperationException::new);

        Assert.assertEquals(StateEnum.enabled, inBase.getStateEnum());
    }
}
