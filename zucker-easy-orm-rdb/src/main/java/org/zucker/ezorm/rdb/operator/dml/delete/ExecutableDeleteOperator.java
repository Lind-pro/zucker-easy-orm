package org.zucker.ezorm.rdb.operator.dml.delete;

import lombok.AllArgsConstructor;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.delete.DeleteSqlBuilder;

/**
 * @author lind
 * @since 1.0
 */
@AllArgsConstructor(staticName = "of")
public class ExecutableDeleteOperator extends BuildParameterDeleteOperator {

    private RDBTableMetadata table;

    @Override
    public SqlRequest getSql() {
        return table.findFeatureNow(DeleteSqlBuilder.ID).build(getParameter());
    }

    @Override
    public DeleteResultOperator execute() {
        return DefaultDeleteResultOperator.of(table, this::getSql);
    }
}
