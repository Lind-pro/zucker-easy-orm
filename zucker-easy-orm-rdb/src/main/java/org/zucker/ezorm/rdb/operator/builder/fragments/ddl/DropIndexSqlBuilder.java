package org.zucker.ezorm.rdb.operator.builder.fragments.ddl;

import org.zucker.ezorm.core.FeatureId;
import org.zucker.ezorm.rdb.executor.CreateIndexParameter;
import org.zucker.ezorm.rdb.operator.builder.SqlBuilder;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface DropIndexSqlBuilder extends SqlBuilder<CreateIndexParameter> {

    String ID_VALUE = "dropIndexSqlBuilder";
    FeatureId<DropIndexSqlBuilder> ID = FeatureId.of(ID_VALUE);

    @Override
   default String getId(){
        return ID_VALUE;
    }

    @Override
   default String getName(){
        return "删除SQL构造器";
    }
}
