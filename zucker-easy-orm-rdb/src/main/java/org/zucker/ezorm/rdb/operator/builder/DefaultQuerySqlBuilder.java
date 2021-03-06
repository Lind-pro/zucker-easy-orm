package org.zucker.ezorm.rdb.operator.builder;

import lombok.AllArgsConstructor;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.metadata.RDBFeatureType;
import org.zucker.ezorm.rdb.metadata.RDBFeatures;
import org.zucker.ezorm.rdb.metadata.RDBSchemaMetadata;
import org.zucker.ezorm.rdb.metadata.TableOrViewMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.BlockSqlFragments;
import org.zucker.ezorm.rdb.operator.builder.fragments.PrepareSqlFragments;
import org.zucker.ezorm.rdb.operator.builder.fragments.SqlFragments;
import org.zucker.ezorm.rdb.operator.dml.query.QueryOperatorParameter;
import org.zucker.ezorm.rdb.operator.dml.query.QuerySqlBuilder;

import java.util.Optional;

/**
 * @auther: lind
 * @since: 1.0
 */
@AllArgsConstructor(staticName = "of")
public class DefaultQuerySqlBuilder implements QuerySqlBuilder {

    protected RDBSchemaMetadata schema;

    protected Optional<SqlFragments> select(QueryOperatorParameter parameter, TableOrViewMetadata metadata) {
        return metadata.getFeature(RDBFeatures.select)
                .map(builder -> builder.createFragments(parameter))
                .filter(SqlFragments::isNotEmpty);
    }

    protected Optional<SqlFragments> where(QueryOperatorParameter parameter, TableOrViewMetadata metadata) {
        return metadata.getFeature(RDBFeatures.where)
                .map(builder -> builder.createFragments(parameter))
                .filter(SqlFragments::isNotEmpty);
    }

    protected Optional<SqlFragments> join(QueryOperatorParameter parameter, TableOrViewMetadata metadata) {
        return metadata.getFeature(RDBFeatures.selectJoin)
                .map(builder -> builder.createFragments(parameter))
                .filter(SqlFragments::isNotEmpty);
    }

    protected Optional<SqlFragments> orderBy(QueryOperatorParameter parameter, TableOrViewMetadata metadata) {
        return metadata.getFeature(RDBFeatures.orderBy)
                .map(builder -> builder.createFragments(parameter))
                .filter(SqlFragments::isNotEmpty);
    }

    protected SqlRequest build(TableOrViewMetadata metadata, QueryOperatorParameter parameter) {
        BlockSqlFragments fragments = BlockSqlFragments.of();

        fragments.addBlock(FragmentBlock.before, "select");
        fragments.addBlock(FragmentBlock.selectColumn, select(parameter, metadata)
                .orElseGet(() -> PrepareSqlFragments.of().addSql("*")));

        fragments.addBlock(FragmentBlock.selectFrom, PrepareSqlFragments.of()
                .addSql("from")
                .addSql(metadata.getFullName())
                .addSql(parameter.getFormAlias()));


        join(parameter, metadata)
                .ifPresent(join -> fragments.addBlock(FragmentBlock.join, join));

        where(parameter, metadata)
                .ifPresent(where ->
                        fragments.addBlock(FragmentBlock.where, "where")
                                .addBlock(FragmentBlock.where, where));

        orderBy(parameter, metadata)
                .ifPresent(order -> fragments.addBlock(FragmentBlock.orderBy, "order by")
                        .addBlock(FragmentBlock.orderBy, order));

        if (Boolean.TRUE.equals(parameter.getForUpdate())) {
            fragments.addBlock(FragmentBlock.after, PrepareSqlFragments.of().addSql("for update"));
        } else if (parameter.getPageIndex() != null && parameter.getPageSize() != null) {
            return metadata.<Paginator>findFeature(RDBFeatureType.paginator.getId())
                    .map(paginator -> paginator.doPaging(fragments, parameter.getPageIndex(), parameter.getPageSize()))
                    .map(SqlFragments::toRequest)
                    .orElseGet(fragments::toRequest);
        }
        return fragments.toRequest();
    }

    @Override
    public SqlRequest build(QueryOperatorParameter parameter) {
        String from = parameter.getFrom();
        if(from ==null || from.isEmpty()){
            throw  new UnsupportedOperationException("from table or view not set");
        }
        TableOrViewMetadata metadata = schema.find
        return null;
    }
}
