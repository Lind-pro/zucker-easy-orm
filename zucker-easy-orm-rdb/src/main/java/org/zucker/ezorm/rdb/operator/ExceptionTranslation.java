package org.zucker.ezorm.rdb.operator;

import org.zucker.ezorm.core.FeatureId;
import org.zucker.ezorm.core.FeatureType;
import org.zucker.ezorm.core.meta.Feature;
import org.zucker.ezorm.rdb.metadata.RDBFeatureType;

/**
 * @author lind
 * @since 1.0
 */
public interface ExceptionTranslation extends Feature {

    String ID_VALUE = "exceptionTranslation";

    FeatureId<ExceptionTranslation> ID = FeatureId.of(ID_VALUE);

    @Override
    default String getId() {
        return ID_VALUE;
    }

    @Override
    default String getName() {
        return getType().getName();
    }

    default FeatureType getType() {
        return RDBFeatureType.exceptionTranslation;
    }

    Throwable translate(Throwable e);
}
