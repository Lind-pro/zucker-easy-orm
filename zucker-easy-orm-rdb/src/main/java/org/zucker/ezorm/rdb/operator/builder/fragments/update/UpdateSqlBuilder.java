package org.zucker.ezorm.rdb.operator.builder.fragments.update;

import org.zucker.ezorm.core.FeatureId;
import org.zucker.ezorm.rdb.operator.builder.SqlBuilder;
import org.zucker.ezorm.rdb.operator.dml.update.UpdateOperatorParameter;

/**
 * @author lind
 * @since 1.0
 */
public interface UpdateSqlBuilder extends SqlBuilder<UpdateOperatorParameter> {

    String ID_VALUE = "updateSqlBuilder";

    FeatureId<UpdateSqlBuilder> ID = FeatureId.of(ID_VALUE);

    @Override
    default String getId() {
        return ID_VALUE;
    }

    @Override
    default String getName() {
        return "Update SQL 构造器";
    }
}
