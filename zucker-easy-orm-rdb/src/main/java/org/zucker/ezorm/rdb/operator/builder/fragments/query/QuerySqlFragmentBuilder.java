package org.zucker.ezorm.rdb.operator.builder.fragments.query;

import org.zucker.ezorm.core.FeatureType;
import org.zucker.ezorm.core.meta.Feature;
import org.zucker.ezorm.rdb.metadata.RDBFeatureType;
import org.zucker.ezorm.rdb.operator.builder.fragments.SqlFragments;
import org.zucker.ezorm.rdb.operator.dml.query.QueryOperatorParameter;

/**
 * @author lind
 * @since 1.0
 */
public interface QuerySqlFragmentBuilder extends Feature {

    // feature id
    String where = "queryTermsFragmentBuilder";
    String selectColumns = "selectColumnFragmentBuilder";
    String join = "joinSqlFragmentBuilder";
    String sortOrder = "sortOrderFragmentBuilder";

    @Override
    default FeatureType getType() {
        return RDBFeatureType.fragment;
    }

    SqlFragments createFragments(QueryOperatorParameter parameter);
}
