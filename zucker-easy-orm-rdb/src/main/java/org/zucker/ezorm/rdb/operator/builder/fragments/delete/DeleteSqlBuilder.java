package org.zucker.ezorm.rdb.operator.builder.fragments.delete;

import org.zucker.ezorm.core.FeatureId;
import org.zucker.ezorm.rdb.operator.builder.SqlBuilder;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface DeleteSqlBuilder extends SqlBuilder<DeleteOperatorParameter> {

    String ID_VALUE = "deleteSqlBuilder";
    FeatureId<DeleteSqlBuilder> ID = FeatureId.of(ID_VALUE);

    @Override
    default String getId() {
        return ID_VALUE;
    }

    @Override
    default String getName() {
        return "Delete SQL 构造器";
    }
}
