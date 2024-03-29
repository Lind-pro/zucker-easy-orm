package org.zucker.ezorm.core;

import org.zucker.ezorm.core.meta.DefaultFeatureType;
import org.zucker.ezorm.core.meta.Feature;
import org.zucker.ezorm.core.meta.ObjectMetadata;

/**
 * @author lind
 * @since 1.0
 */
public interface DefaultValueGenerator<E extends ObjectMetadata> extends Feature {

    static <E extends ObjectMetadata> FeatureId<DefaultValueGenerator<E>> createId(String id) {
        return FeatureId.of("generator_".concat(id));
    }

    String getSortId();

    @Override
    default String getId() {
        return "generator_".concat(getSortId());
    }

    @Override
    default FeatureType getType() {
        return DefaultFeatureType.defaultValueGenerator;
    }

    DefaultValue generate(E metadata);
}
