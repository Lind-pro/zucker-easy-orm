package org.zucker.ezorm.rdb.operator.dml.update;

import lombok.AllArgsConstructor;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.update.UpdateSqlBuilder;

/**
 * @author lind
 * @since 1.0
 */
@AllArgsConstructor(staticName = "of")
public class ExecutableUpdateOperator extends BuildParameterUpdateOperator {

    private RDBTableMetadata table;

    @Override
    public SqlRequest getSql() {
        return table.<UpdateSqlBuilder>findFeatureNow(UpdateSqlBuilder.ID_VALUE).build(getParameter());
    }

    @Override
    public UpdateResultOperator execute() {
        return DefaultUpdateResultOperator.of(table, this::getSql);
    }
}
