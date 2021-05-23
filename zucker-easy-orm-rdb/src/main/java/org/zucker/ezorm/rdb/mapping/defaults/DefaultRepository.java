package org.zucker.ezorm.rdb.mapping.defaults;

import lombok.Getter;
import lombok.Setter;
import org.zucker.ezorm.core.GlobalConfig;
import org.zucker.ezorm.core.ObjectPropertyOperator;
import org.zucker.ezorm.rdb.events.ContextKeyValue;
import org.zucker.ezorm.rdb.events.ContextKeys;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrapper;
import org.zucker.ezorm.rdb.mapping.EntityColumnMapping;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.DatabaseOperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    protected void initMapping(Class<E> entityType){
    }
}
