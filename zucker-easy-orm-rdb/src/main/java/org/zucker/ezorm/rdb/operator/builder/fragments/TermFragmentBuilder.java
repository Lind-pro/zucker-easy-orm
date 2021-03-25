package org.zucker.ezorm.rdb.operator.builder.fragments;

import org.zucker.ezorm.core.FeatureId;
import org.zucker.ezorm.core.FeatureType;
import org.zucker.ezorm.core.meta.Feature;
import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.metadata.RDBFeatureType;

/**
 * SQL 条件片段构造器
 * @auther: lind
 * @since: 1.0
 */
public interface TermFragmentBuilder extends Feature {

    static FeatureId<TermFragmentBuilder> createFeatureId(String suffix) {
        return FeatureId.of(RDBFeatureType.termsType.getId().concat(":").concat(suffix.toLowerCase()));
    }

    @Override
    default String getId() {
        return getType().getFeatureId(getTermType().toLowerCase());
    }

    @Override
    default RDBFeatureType getType() {
        return RDBFeatureType.termType;
    }

    String getTermType();

    SqlFragments createFragments(String columnFullName, RDBColumnMetadata column, Term term);
}
