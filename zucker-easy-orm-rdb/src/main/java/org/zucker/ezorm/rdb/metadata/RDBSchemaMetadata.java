package org.zucker.ezorm.rdb.metadata;

import org.zucker.ezorm.core.PropertyWrapper;
import org.zucker.ezorm.core.meta.AbstractColumnMetadata;
import org.zucker.ezorm.core.meta.AbstractSchemaMetadata;
import org.zucker.ezorm.core.meta.ObjectType;
import org.zucker.ezorm.rdb.operator.builder.DefaultQuerySqlBuilder;
import org.zucker.ezorm.rdb.operator.builder.fragments.term.DefaultForeignKeyTermFragmentBuilder;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @auther: lind
 * @since: 1.0
 */
public class RDBSchemaMetadata extends AbstractSchemaMetadata {

    private final List<ObjectType> allObjectType = Arrays.asList(RDBObjectType.table, RDBObjectType.view);

    public RDBSchemaMetadata(String name) {
        {
            // 查询
            addFeature(DefaultQuerySqlBuilder.of(this));

            addFeature(RDBFeatures.eq);
            addFeature(RDBFeatures.not);
            addFeature(RDBFeatures.gt);
            addFeature(RDBFeatures.gte);
            addFeature(RDBFeatures.lt);
            addFeature(RDBFeatures.lte);
            addFeature(RDBFeatures.like);
            addFeature(RDBFeatures.nlike);


            addFeature(RDBFeatures.in);
            addFeature(RDBFeatures.notIn);
            addFeature(RDBFeatures.between);
            addFeature(RDBFeatures.notBetween);


            addFeature(RDBFeatures.isNull);
            addFeature(RDBFeatures.notNull);

            // 自动关联外键条件
            addFeature(DefaultForeignKeyTermFragmentBuilder.INSTANCE);

            /* 函数 */
            addFeature(RDBFeatures.count);
            addFeature(RDBFeatures.sum);
            addFeature(RDBFeatures.max);
            addFeature(RDBFeatures.min);
            addFeature(RDBFeatures.avg);

            /* DDL */
            // TODO
//            addFeature(RDBFeatures.count);

            /* 编解码工厂*/
            addFeature(DefaultValueCodecFactory.COMMONS);
        }
        this.setName(name);
    }

    @Override
    public RDBDatabaseMetadata getDatabase() {
        return (RDBDatabaseMetadata) super.getDatabase();
    }

    public Optional<RDBTableMetadata> getTable(String name, boolean autoLoad) {
        if (name.contains(".")) {
            return findTableOrView(name).map(RDBTableMetadata.class::cast);
        }
        return getObject(RDBObjectType.table, name, autoLoad);
    }

    public Optional<RDBTableMetadata> getTable(String name) {
        if (name.contains(".")) {
            return findTableOrView(name)
                    .map(RDBTableMetadata.class::cast);
        }
        return getObject(RDBObjectType.table, name);
    }

    public Optional<TableOrViewMetadata> findTableOrView(String name) {
        Optional<TableOrViewMetadata> current = getTableOrView(name, false);
        if (current.isPresent()) {
            return current;
        }
        return getDatabase().getTableOrView(name);
    }

    public Mono<RDBTableMetadata> getTableReactive(String name) {
        return getTableReactive(name, true);
    }

    public Mono<RDBTableMetadata> getTableReactive(String name, boolean autoLoad) {
        if (name.contains(".")) {
            return findTableOrViewReactive(name)
                    .map(RDBSchemaMetadata.class::cast);
        }
        return getObjectReactive(RDBObjectType.table, name, autoLoad);
    }

    public Mono<TableOrViewMetadata> getTableOrViewReactive(String name, boolean autoLoad) {
        return getTableReactive(name, autoLoad)
                .cast(TableOrViewMetadata.class)
                .switchIfEmpty(Mono.defer(() -> getViewReactive(name, autoLoad).cast(TableOrViewMetadata.class)));
    }


    public Mono<TableOrViewMetadata> getTableOrViewReactive(String name) {
        return getTableOrViewReactive(name, true);
    }


    public Mono<RDBViewMetadata> getViewReactive(String name) {
        return getObjectReactive(RDBObjectType.view, name);
    }

    public Mono<RDBViewMetadata> getViewReactive(String name, boolean autoLoad) {
        return getObjectReactive(RDBObjectType.view, name, autoLoad);
    }

    public Optional<RDBViewMetadata> getView(String name, boolean autoLoad) {
        return getObject(RDBObjectType.view, name, autoLoad);
    }

    public Optional<RDBViewMetadata> getView(String name) {
        return getView(name, true);
    }

    public void addTable(RDBTableMetadata metadata){

    }

    public Mono<TableOrViewMetadata> findTableOrViewReactive(String name) {
        return getTableOrViewReactive(name, false)
                .switchIfEmpty(getDatabase().getTableOrViewReactive(name));
    }


    public Optional<TableOrViewMetadata> getTableOrView(String name, boolean autoLoad) {
        return Optional.of(getTable(name, autoLoad)
                .map(AbstractTableOrViewMetadata.class::cast))
                .filter(Optional::isPresent)
                .orElseGet(() -> getView(name, autoLoad)
                        .map(AbstractTableOrViewMetadata.class::cast))
                .map(TableOrViewMetadata.class::cast);
    }

}
