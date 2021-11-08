package org.zucker.ezorm.rdb.mapping;

import org.zucker.ezorm.core.FeatureType;
import org.zucker.ezorm.core.meta.Feature;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface EntityManager extends Feature {

    @Override
    default String getName() {
        return MappingFeatureType.entityManager.getName();
    }

    @Override
    default String getId() {
        return MappingFeatureType.entityManager.getId();
    }

    @Override
    default FeatureType getType() {
        return MappingFeatureType.entityManager;
    }

    <E> E newInstance(Class<E> type);

    EntityColumnMapping getMapping(Class entity);
}
