package org.zucker.ezorm.rdb.operator.builder.fragments.function;

import org.zucker.ezorm.core.FeatureId;
import org.zucker.ezorm.core.FeatureType;
import org.zucker.ezorm.core.meta.Feature;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.metadata.RDBFeatureType;
import org.zucker.ezorm.rdb.operator.builder.fragments.SqlFragments;

import java.util.Map;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface FunctionFragmentBuilder extends Feature {

    static FeatureId<FunctionFragmentBuilder> createFeatureId(String suffix) {
        return FeatureId.of(RDBFeatureType.function.getId().concat(":").concat(suffix));
    }

    @Override
    default String getId() {
        return getType().getFeatureId(getFunction());
    }

    @Override
    default RDBFeatureType getType() {
        return RDBFeatureType.function;
    }

    String getFunction();

    SqlFragments create(String columnFullName, RDBColumnMetadata metadata, Map<String, Object> opts);
}
