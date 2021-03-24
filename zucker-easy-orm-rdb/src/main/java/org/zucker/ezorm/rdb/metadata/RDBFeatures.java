package org.zucker.ezorm.rdb.metadata;

import org.zucker.ezorm.core.FeatureId;
import org.zucker.ezorm.core.meta.Feature;
import org.zucker.ezorm.rdb.operator.builder.fragments.query.QuerySqlFragmentBuilder;

/**
 * @auther: lind
 * @since: 1.0
 */
public class RDBFeatures {

    FeatureId<QuerySqlFragmentBuilder> where = FeatureId.of(QuerySqlFragmentBuilder.where);
    FeatureId<QuerySqlFragmentBuilder> select = FeatureId.of(QuerySqlFragmentBuilder.selectColumns);
    FeatureId<QuerySqlFragmentBuilder> selectJoin = FeatureId.of(QuerySqlFragmentBuilder.join);
    FeatureId<QuerySqlFragmentBuilder> orderBy = FeatureId.of(QuerySqlFragmentBuilder.sortOrder);

}
