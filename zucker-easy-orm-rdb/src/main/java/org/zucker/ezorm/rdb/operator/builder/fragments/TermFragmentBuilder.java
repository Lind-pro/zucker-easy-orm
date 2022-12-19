package org.zucker.ezorm.rdb.operator.builder.fragments;

import org.zucker.ezorm.core.FeatureId;
import org.zucker.ezorm.core.FeatureType;
import org.zucker.ezorm.core.meta.Feature;
import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.metadata.RDBFeatureType;

/**
 * SQL 条件片段构造器
 *
 * @author lind
 * @since 1.0
 */
public interface TermFragmentBuilder extends Feature {

    /**
     * 根据termType来创建featureId
     *
     * @param termType termType
     * @return FeatureId
     */
    static FeatureId<TermFragmentBuilder> createFeatureId(String termType) {
        return FeatureId.of(RDBFeatureType.termType.getFeatureId(termType.toLowerCase()));
    }

    @Override
    default String getId() {
        return getType().getFeatureId(getTermType().toLowerCase());
    }

    @Override
    default RDBFeatureType getType() {
        return RDBFeatureType.termType;
    }

    /**
     * @return 条件类型
     * @see Term#getTermType()
     */
    String getTermType();

    /**
     * 创建SQL条件片段
     *
     * @param columnFullName 列全名，如:schema.table
     * @param column         列对应的元数据。{@link Term#getColumn()}
     * @param term           条件
     * @return SQL片段
     * @see Term
     */
    SqlFragments createFragments(String columnFullName, RDBColumnMetadata column, Term term);
}
