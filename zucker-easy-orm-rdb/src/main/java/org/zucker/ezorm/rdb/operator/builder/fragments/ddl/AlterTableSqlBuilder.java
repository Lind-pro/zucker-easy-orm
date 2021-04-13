package org.zucker.ezorm.rdb.operator.builder.fragments.ddl;

import org.zucker.ezorm.core.FeatureId;
import org.zucker.ezorm.rdb.operator.builder.SqlBuilder;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface AlterTableSqlBuilder extends SqlBuilder<AlterRequest> {

    String ID_VALUE = "alterTableSqlBuilder";

    FeatureId<AlterTableSqlBuilder> ID = FeatureId.of(ID_VALUE);

    @Override
    default String getId() {
        return ID_VALUE;
    }

    @Override
    default String getName() {
        return "表结构更新SQL构造器";
    }
}
