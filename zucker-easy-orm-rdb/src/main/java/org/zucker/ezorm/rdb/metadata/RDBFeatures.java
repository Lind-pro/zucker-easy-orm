package org.zucker.ezorm.rdb.metadata;

import org.zucker.ezorm.core.FeatureId;
import org.zucker.ezorm.core.meta.Feature;
import org.zucker.ezorm.rdb.operator.builder.fragments.query.QuerySqlFragmentBuilder;
import org.zucker.ezorm.rdb.operator.builder.fragments.term.SymbolTermFragmentBuilder;

/**
 * @auther: lind
 * @since: 1.0
 */
public class RDBFeatures {

    FeatureId<QuerySqlFragmentBuilder> where = FeatureId.of(QuerySqlFragmentBuilder.where);
    FeatureId<QuerySqlFragmentBuilder> select = FeatureId.of(QuerySqlFragmentBuilder.selectColumns);
    FeatureId<QuerySqlFragmentBuilder> selectJoin = FeatureId.of(QuerySqlFragmentBuilder.join);
    FeatureId<QuerySqlFragmentBuilder> orderBy = FeatureId.of(QuerySqlFragmentBuilder.sortOrder);

    /**
     * 通用查询条件
     */
    SymbolTermFragmentBuilder eq = new SymbolTermFragmentBuilder("eq","等于","=");

    SymbolTermFragmentBuilder not = new SymbolTermFragmentBuilder("not","不等于","!=");

    SymbolTermFragmentBuilder like = new SymbolTermFragmentBuilder("like","模糊匹配","like");
    SymbolTermFragmentBuilder nlike =new SymbolTermFragmentBuilder("nlike","不模糊匹配","not like");

    SymbolTermFragmentBuilder gt = new SymbolTermFragmentBuilder("gt","大于",">");
    SymbolTermFragmentBuilder lt = new SymbolTermFragmentBuilder("lt","小于","<");
    SymbolTermFragmentBuilder gte = new SymbolTermFragmentBuilder("gte","大于等于",">=");
    SymbolTermFragmentBuilder lte = new SymbolTermFragmentBuilder("lte","小于等于","<=");

}
