package org.zucker.ezorm.rdb.operator.dml.query;

import org.zucker.ezorm.core.FeatureId;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.operator.builder.SqlBuilder;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface QuerySqlBuilder extends SqlBuilder<QueryOperatorParameter> {

    String ID_VALUE = "querySqlBuilder";

    FeatureId<QuerySqlBuilder> ID = FeatureId.of(ID_VALUE);

    @Override
    default String getId() {
        return ID_VALUE;
    }

    @Override
    default String getName() {
        return "查询SQL构造器";
    }

    SqlRequest build(QueryOperatorParameter parameter);
}
