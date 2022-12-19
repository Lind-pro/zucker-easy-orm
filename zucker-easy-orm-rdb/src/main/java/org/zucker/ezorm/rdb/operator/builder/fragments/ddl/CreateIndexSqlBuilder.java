package org.zucker.ezorm.rdb.operator.builder.fragments.ddl;

import org.zucker.ezorm.core.FeatureId;
import org.zucker.ezorm.rdb.executor.CreateIndexParameter;
import org.zucker.ezorm.rdb.operator.builder.SqlBuilder;

/**
 * @author lind
 * @since 1.0
 */
public interface CreateIndexSqlBuilder extends SqlBuilder<CreateIndexParameter> {

    String ID_VALUE = "createIndexSqlBuilder";
    FeatureId<CreateIndexSqlBuilder> ID = FeatureId.of(ID_VALUE);

    @Override
    default String getId() {
        return ID_VALUE;
    }

    @Override
    default String getName() {
        return "索引SQL构造器";
    }
}
