package org.zucker.ezorm.rdb.operator.builder.fragments.query;

import lombok.AllArgsConstructor;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.metadata.TableOrViewMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.EmptySqlFragments;
import org.zucker.ezorm.rdb.operator.builder.fragments.PrepareSqlFragments;
import org.zucker.ezorm.rdb.operator.builder.fragments.SqlFragments;
import org.zucker.ezorm.rdb.operator.builder.fragments.function.FunctionFragmentBuilder;
import org.zucker.ezorm.rdb.operator.dml.query.QueryOperatorParameter;
import org.zucker.ezorm.rdb.operator.dml.query.SortOrder;

import java.util.Optional;

/**
 * @author lind
 * @since 1.0
 */
@AllArgsConstructor(staticName = "of")
public class SortOrderFragmentBuilder implements QuerySqlFragmentBuilder {

    private TableOrViewMetadata metadata;

    @Override
    public SqlFragments createFragments(QueryOperatorParameter parameter) {
        PrepareSqlFragments fragments = PrepareSqlFragments.of();

        int index = 0;
        for (SortOrder sortOrder : parameter.getOrderBy()) {
            SqlFragments orderFragments = createOrder(sortOrder, parameter);
            if (orderFragments.isNotEmpty()) {
                if (index++ != 0) {
                    fragments.addSql(",");
                }
                fragments.addFragments(orderFragments);
            }
        }
        return fragments;
    }


    private SqlFragments createOrder(String fullName, RDBColumnMetadata column, SortOrder order) {
        SqlFragments fragments = Optional.ofNullable(order.getFunction())
                .flatMap(function -> column.findFeature(FunctionFragmentBuilder.createFeatureId(function)))
                .map(builder -> builder.create(fullName, column, order.getOpts()))
                .orElseGet(() -> PrepareSqlFragments.of().addSql(fullName));
        return PrepareSqlFragments.of()
                .addFragments(fragments)
                .addSql(order.getOrder().name());
    }

    private SqlFragments createOrder(SortOrder order, QueryOperatorParameter parameter) {
        String column = order.getColumn();
        if (column.contains(".")) {
            String[] arr = column.split("[.]");
            if (arr[0].equals(parameter.getFrom()) || arr[0].equals(parameter.getFormAlias())) {
                column = arr[1];
            } else {
                // 关联表
                return parameter.findJoin(arr[0])
                        .flatMap(join ->
                                metadata.getSchema()
                                        .getTableOrView(join.getTarget())
                                        .flatMap(table -> table.getColumn(arr[1]))
                                        .map(joinColumn -> createOrder(joinColumn.getFullName(join.getAlias()), joinColumn, order)))
                        .orElse(EmptySqlFragments.INSTANCE);
            }
        }
        return metadata.getColumn(column)
                .map(orderColumn->createOrder(orderColumn.getFullName(parameter.getFormAlias()),orderColumn,order))
                .orElse(EmptySqlFragments.INSTANCE);
    }

    @Override
    public String getId() {
        return sortOrder;
    }

    @Override
    public String getName() {
        return "排序";
    }
}
