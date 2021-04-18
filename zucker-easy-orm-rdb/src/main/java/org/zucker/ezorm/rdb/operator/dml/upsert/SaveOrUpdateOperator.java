package org.zucker.ezorm.rdb.operator.dml.upsert;

import org.zucker.ezorm.core.FeatureId;
import org.zucker.ezorm.core.FeatureType;
import org.zucker.ezorm.core.meta.Feature;
import org.zucker.ezorm.rdb.metadata.RDBFeatureType;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface SaveOrUpdateOperator extends Feature {

    String ID_VALUE = "saveOrUpdateOperator";
    FeatureId<SaveOrUpdateOperator> ID = FeatureId.of(ID_VALUE);

    @Override
    default String getId() {
        return ID_VALUE;
    }

    @Override
    default String getName() {
        return getType().getName();
    }

    @Override
    default FeatureType getType() {
        return RDBFeatureType.saveOrUpdateOperator;
    }

    SaveResultOperator execute(UpsertOperatorParameter parameter);

}
