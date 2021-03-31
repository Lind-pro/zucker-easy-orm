package org.zucker.ezorm.rdb.operator.builder;

import org.zucker.ezorm.core.meta.Feature;
import org.zucker.ezorm.rdb.metadata.RDBFeatureType;
import org.zucker.ezorm.rdb.operator.builder.fragments.SqlFragments;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface Paginator extends Feature {

    @Override
    default String getId() {
        return getType().getId();
    }

    @Override
    default String getName() {
        return getType().getName();
    }

    @Override
    default RDBFeatureType getType() {
        return RDBFeatureType.paginator;
    }

    SqlFragments doPaging(SqlFragments fragments, int pageIndex, int PageSize);
}
