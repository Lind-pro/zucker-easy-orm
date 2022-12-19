package org.zucker.ezorm.rdb.operator.builder.fragments.query;

import lombok.AllArgsConstructor;
import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.metadata.TableOrViewMetadata;
import org.zucker.ezorm.rdb.metadata.key.ForeignKeyMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.AbstractTermsFragmentBuilder;
import org.zucker.ezorm.rdb.operator.builder.fragments.EmptySqlFragments;
import org.zucker.ezorm.rdb.operator.builder.fragments.SqlFragments;
import org.zucker.ezorm.rdb.operator.builder.fragments.term.ForeignKeyTermFragmentBuilder;
import org.zucker.ezorm.rdb.operator.dml.query.QueryOperatorParameter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.zucker.ezorm.rdb.operator.builder.fragments.TermFragmentBuilder.createFeatureId;

/**
 * @author lind
 * @since 1.0
 */
public class QueryTermsFragmentBuilder extends AbstractTermsFragmentBuilder<QueryOperatorParameter> implements QuerySqlFragmentBuilder {

    private final TableOrViewMetadata metadata;

    private final Set<String> alias;

    protected QueryTermsFragmentBuilder(TableOrViewMetadata metadata, Set<String> alias) {
        this.metadata = metadata;
        this.alias = alias;
    }

    public static QueryTermsFragmentBuilder of(TableOrViewMetadata metadata) {
        return of(metadata, Collections.emptySet());
    }

    public static QueryTermsFragmentBuilder of(TableOrViewMetadata metadata, Set<String> alias) {
        return new QueryTermsFragmentBuilder(metadata, alias);
    }

    @Override
    public String getId() {
        return where;
    }

    @Override
    public String getName() {
        return "查询条件";
    }

    protected SqlFragments createTermFragments(QueryOperatorParameter parameter, Term term) {
        String columnName = term.getColumn();
        if (columnName == null) {
            return EmptySqlFragments.INSTANCE;
        }
        if (columnName.contains(".")) {
            String[] arr = columnName.split("[.]");
            if (metadata.equalsNameOrAlias(arr[0]) || arr[0].equals(parameter.getFormAlias()) || alias.contains(arr[0])) {
                columnName = arr[1];
            } else {
                return parameter
                        .findJoin(arr[0])// 找到join 的表
                        .flatMap(join -> metadata
                                .getSchema()
                                .getTableOrView(join.getTarget())
                                .flatMap(tableOrView -> tableOrView.getColumn(arr[1]))
                                .flatMap(column -> column
                                        .findFeature(createFeatureId(term.getTermType()))
                                        .map(termFragment -> termFragment.createFragments(createColumnFullName(column, parameter.getFormAlias()), column, term))))
                        .orElseGet(() -> {
                            return metadata
                                    .getForeignKey(arr[0])
                                    .flatMap(key -> key
                                            .getSource()
                                            .findFeature(ForeignKeyTermFragmentBuilder.ID)
                                            .map(builder -> builder.createFragments(parameter.getFormAlias(), key, createForeignKeyTerm(key, term))))
                                    .orElse(EmptySqlFragments.INSTANCE);
                        });
            }
        }
        return metadata
                .getColumn(columnName)
                .flatMap(column -> column
                        .findFeature(createFeatureId(term.getTermType()))
                        .map(termFragment -> termFragment.createFragments(createColumnFullName(column, parameter.getFormAlias()), column, term)))
                .orElse(EmptySqlFragments.INSTANCE);
    }

    protected List<Term> createForeignKeyTerm(ForeignKeyMetadata keyMetadata, Term term) {
        Term copy = term.clone();
        // 只要是嵌套到外键表的条件则认为是关联表的条件
        term.setTerms(new LinkedList<>());
        return Collections.singletonList(copy);
    }

    @Override
    public SqlFragments createFragments(QueryOperatorParameter parameter) {
        return createTermFragments(parameter, parameter.getWhere());
    }

    protected String createColumnFullName(RDBColumnMetadata column, String fromAlias) {
        return column.getFullName(fromAlias);
    }
}
