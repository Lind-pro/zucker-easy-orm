package org.zucker.ezorm.rdb.mapping.defaults;

import lombok.Getter;
import lombok.Setter;
import org.zucker.ezorm.core.GlobalConfig;
import org.zucker.ezorm.core.ObjectPropertyOperator;
import org.zucker.ezorm.rdb.events.ContextKeyValue;
import org.zucker.ezorm.rdb.events.ContextKeys;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrapper;
import org.zucker.ezorm.rdb.mapping.EntityColumnMapping;
import org.zucker.ezorm.rdb.mapping.LazyEntityColumnMapping;
import org.zucker.ezorm.rdb.mapping.MappingFeatureType;
import org.zucker.ezorm.rdb.mapping.events.MappingContextKeys;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.DatabaseOperator;
import org.zucker.ezorm.rdb.operator.dml.upsert.SaveResultOperator;
import org.zucker.ezorm.rdb.operator.dml.upsert.UpsertOperator;

import java.util.*;
import java.util.function.Supplier;

import static org.zucker.ezorm.rdb.mapping.events.MappingContextKeys.*;


/**
 * @auther: lind
 * @since: 1.0
 */
public abstract class DefaultRepository<E> {

    protected DatabaseOperator operator;

    protected ResultWrapper<E, ?> wrapper;

    private volatile String idColumn;

    protected EntityColumnMapping mapping;

    @Getter
    protected volatile String[] properties;

    protected Supplier<RDBTableMetadata> tableSupplier;

    protected final List<ContextKeyValue> defaultContextKeyValue = new ArrayList<>();

    @Getter
    @Setter
    private ObjectPropertyOperator propertyOperator = GlobalConfig.getPropertyOperator();

    public DefaultRepository(DatabaseOperator operator, Supplier<RDBTableMetadata> supplier, ResultWrapper<E, ?> wrapper) {
        this.operator = operator;
        this.wrapper = wrapper;
        this.tableSupplier = supplier;
        defaultContextKeyValue.add(repository.value(this));
        defaultContextKeyValue.add(ContextKeys.database.value(operator));
    }

    protected RDBTableMetadata getTable() {
        return tableSupplier.get();
    }

    protected ContextKeyValue[] getDefaultContextKeyValue(ContextKeyValue... kv) {
        if (kv.length == 0) {
            return defaultContextKeyValue.toArray(new ContextKeyValue[0]);
        }
        List<ContextKeyValue> keyValues = new ArrayList<>(defaultContextKeyValue);
        keyValues.addAll(Arrays.asList(kv));
        return keyValues.toArray(new ContextKeyValue[0]);
    }

    public String[] getProperties() {
        if (properties == null) {
            properties = mapping.getColumnPropertyMapping()
                    .entrySet()
                    .stream()
                    .filter(kv -> getTable().getColumn(kv.getKey()).isPresent())
                    .map(Map.Entry::getValue)
                    .toArray(String[]::new);
        }
        return properties;
    }

    protected String getIdColumn() {
        if (idColumn == null) {
            this.idColumn = getTable().getColumns().stream()
                    .filter(RDBColumnMetadata::isPrimaryKey)
                    .findFirst()
                    .map(RDBColumnMetadata::getName)
                    .orElseThrow(() -> new UnsupportedOperationException("id column not exists"));
        }
        return idColumn;
    }

    protected void initMapping(Class<E> entityType) {
        this.mapping = LazyEntityColumnMapping.of(() -> getTable()
                .<EntityColumnMapping>findFeature(MappingFeatureType.columnPropertyMapping.createFeatureId(entityType))
                .orElseThrow(() -> new UnsupportedOperationException("unsupported columnPropertyMapping feature")));
        defaultContextKeyValue.add(columnMapping(mapping));
    }

    protected SaveResultOperator doSave(Collection<E> data){
        RDBTableMetadata table = getTable();
        UpsertOperator upsert = operator.dml().upsert(table.getFullName());
    }
}
