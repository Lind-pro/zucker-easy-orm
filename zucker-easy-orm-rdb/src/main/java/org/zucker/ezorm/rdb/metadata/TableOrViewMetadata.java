package org.zucker.ezorm.rdb.metadata;

import org.zucker.ezorm.core.meta.Feature;
import org.zucker.ezorm.core.meta.FeatureSupportedMetadata;
import org.zucker.ezorm.core.meta.ObjectMetadata;
import org.zucker.ezorm.core.meta.ObjectType;
import org.zucker.ezorm.rdb.metadata.dialect.Dialect;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * DQL 对象元数据：表或者视图
 *
 * @auther: lind
 * @since: 1.0
 */
public interface TableOrViewMetadata extends ObjectMetadata, FeatureSupportedMetadata {

    @Override
    ObjectType getObjectType();

    /**
     * 当前数据库方言
     */
    Dialect getDialect();

    /**
     * @return 元数据所在schema
     */
    RDBSchemaMetadata getSchema();

    default List<Feature> findFeatures(Predicate<Feature> predicate) {
        return Stream.concat(getSchema().getFeatureList().stream(), getFeatureList().stream())
                .filter(predicate)
                .collect(Collectors.toList());
    }

    default List<Feature> findFeatures() {
        return findFeatures((feature -> true));
    }

    default String getFullName() {
        return getSchema().getName().concat(".").concat(getName());
    }
}
