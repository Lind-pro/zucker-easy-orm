package org.zucker.ezorm.rdb.mapping;

import org.zucker.ezorm.core.FeatureType;
import org.zucker.ezorm.core.meta.Feature;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;

import java.util.Map;
import java.util.Optional;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface EntityColumnMapping extends Feature {

    @Override
    default FeatureType getType() {
        return MappingFeatureType.columnPropertyMapping;
    }

    Class<?> getEntityType();

    Optional<RDBColumnMetadata> getColumnByProperty(String property);

    Optional<String> getPropertyByColumnName(String columnName);

    Optional<RDBColumnMetadata> getColumnByName(String columnName);

    Map<String, String> getColumnPropertyMapping();
}
