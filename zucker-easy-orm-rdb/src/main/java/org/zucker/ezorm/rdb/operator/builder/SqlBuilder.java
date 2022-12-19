package org.zucker.ezorm.rdb.operator.builder;

import org.zucker.ezorm.core.FeatureType;
import org.zucker.ezorm.core.meta.Feature;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.metadata.RDBFeatureType;

/**
 * @author lind
 * @since 1.0
 */
public interface SqlBuilder<T> extends Feature {

    @Override
    default RDBFeatureType getType() {
        return RDBFeatureType.sqlBuilder;
    }

    SqlRequest build(T parameter);
}
