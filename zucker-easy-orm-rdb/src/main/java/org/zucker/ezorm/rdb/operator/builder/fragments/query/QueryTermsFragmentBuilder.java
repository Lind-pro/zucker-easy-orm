package org.zucker.ezorm.rdb.operator.builder.fragments.query;

import lombok.AllArgsConstructor;
import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.metadata.TableOrViewMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.AbstractTermFragmentBuilder;
import org.zucker.ezorm.rdb.operator.builder.fragments.EmptySqlFragments;
import org.zucker.ezorm.rdb.operator.builder.fragments.SqlFragments;
import org.zucker.ezorm.rdb.operator.dml.query.QueryOperatorParameter;

import java.util.Collections;
import java.util.Set;

/**
 * @auther: lind
 * @since: 1.0
 */
@AllArgsConstructor(staticName = "of")
public class QueryTermsFragmentBuilder extends AbstractTermFragmentBuilder<QueryOperatorParameter> implements QuerySqlFragmentBuilder{

    private final TableOrViewMetadata metadata;

    private final Set<String> alias;

    public static QueryTermsFragmentBuilder of(TableOrViewMetadata metadata){
        return of(metadata, Collections.emptySet());
    }

    @Override
    public String getId() {
        return where;
    }

    @Override
    public String getName() {
        return "查询条件";
    }

    protected SqlFragments createTermFragments(QueryOperatorParameter parameter,Term term){
        String columnName = term.getColumn();
        if(columnName==null){
            return EmptySqlFragments.INSTANCE;
        }
        if(columnName.contains(".")){
            String[] arr = columnName.split("[.]");
            if(metadata.equalsNameOrAlias(arr[0])||arr[0].equals(parameter.getFormAlias())||alias.contains(arr[0])){
                columnName=arr[1];
            }else {
                return parameter.findJoin(arr[0])
                        .flatMap(join ->metadata.getSchema()
                        .getTableOrView(join.getTarget())
                        .flatMap(tableOrView->tableOrView.getColumn(arr[1])))
            }
        }
    }

    @Override
    public SqlFragments createFragments(String columnFullName, RDBColumnMetadata column, Term term) {
        return null;
    }

    @Override
    public SqlFragments createFragments(QueryOperatorParameter parameter) {
        return null;
    }
}
