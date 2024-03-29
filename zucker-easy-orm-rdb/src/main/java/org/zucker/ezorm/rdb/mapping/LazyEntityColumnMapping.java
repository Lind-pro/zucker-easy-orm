package org.zucker.ezorm.rdb.mapping;

import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author lind
 * @since 1.0
 */
public abstract class LazyEntityColumnMapping implements EntityColumnMapping {

    public abstract EntityColumnMapping getMapping();

    public static LazyEntityColumnMapping of(Supplier<EntityColumnMapping> mappingSupplier) {
        return new LazyEntityColumnMapping() {
            @Override
            public EntityColumnMapping getMapping() {
                return mappingSupplier.get();
            }
        };
    }

    @Override
    public Optional<RDBColumnMetadata> getColumnByProperty(String property) {
        return getMapping().getColumnByProperty(property);
    }

    @Override
    public Optional<String> getPropertyByColumnName(String columnName) {
        return getMapping().getPropertyByColumnName(columnName);
    }

    @Override
    public Optional<RDBColumnMetadata> getColumnByName(String columnName) {
        return getMapping().getColumnByName(columnName);
    }

    @Override
    public Map<String, String> getColumnPropertyMapping() {
        return getMapping().getColumnPropertyMapping();
    }

    @Override
    public String getId() {
        return getMapping().getId();
    }

    @Override
    public String getName() {
        return getMapping().getName();
    }

    @Override
    public Class<?> getEntityType() {
        return getMapping().getEntityType();
    }
}
