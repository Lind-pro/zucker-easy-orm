package org.zucker.ezorm.rdb.operator.dml.insert;

import lombok.AllArgsConstructor;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.insert.InsertSqlBuilder;

/**
 * @auther: lind
 * @since: 1.0
 */
@AllArgsConstructor(staticName = "of")
public class ExecutableInsertOperator extends BuildParameterInsertOperator {

    private RDBTableMetadata table;

    @Override
    public SqlRequest getSql() {
        return table.findFeatureNow(InsertSqlBuilder.ID)
                .build(getParameter());
    }

    @Override
    public InsertResultOperator execute() {
        return DefaultInsertResultOperator.of(table, this::getSql);
    }
}
