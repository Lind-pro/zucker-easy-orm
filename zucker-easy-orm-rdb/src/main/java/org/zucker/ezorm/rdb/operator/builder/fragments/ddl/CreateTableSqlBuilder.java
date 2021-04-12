package org.zucker.ezorm.rdb.operator.builder.fragments.ddl;

import org.zucker.ezorm.core.FeatureId;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.builder.SqlBuilder;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface CreateTableSqlBuilder extends SqlBuilder<RDBTableMetadata> {

    String ID_VALUE = "createTableSqlBuilder";
    FeatureId<CreateTableSqlBuilder> ID = FeatureId.of(ID_VALUE);

    @Override
    default String getId() {
        return ID_VALUE;
    }

    @Override
    default String getName() {
        return "Create Table SQL 构造器";
    }
}
