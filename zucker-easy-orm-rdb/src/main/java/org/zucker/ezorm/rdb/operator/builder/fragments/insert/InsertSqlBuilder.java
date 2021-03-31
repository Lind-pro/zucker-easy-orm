package org.zucker.ezorm.rdb.operator.builder.fragments.insert;

import org.zucker.ezorm.core.FeatureId;
import org.zucker.ezorm.rdb.operator.builder.SqlBuilder;
import org.zucker.ezorm.rdb.operator.dml.insert.InsertOperatorParameter;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface InsertSqlBuilder extends SqlBuilder<InsertOperatorParameter> {

    String ID_VALUE = "insertSqlBuilder";

    FeatureId<InsertSqlBuilder> ID = FeatureId.of(ID_VALUE);

    @Override
    default String getId() {
        return ID_VALUE;
    }

    @Override
    default String getName() {
        return "Insert SQL 构造器";
    }
}
