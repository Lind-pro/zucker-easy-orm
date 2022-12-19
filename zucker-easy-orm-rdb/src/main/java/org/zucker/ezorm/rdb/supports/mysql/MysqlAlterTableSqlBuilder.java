package org.zucker.ezorm.rdb.supports.mysql;

import org.zucker.ezorm.rdb.executor.DefaultBatchSqlRequest;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.PrepareSqlFragments;
import org.zucker.ezorm.rdb.operator.builder.fragments.ddl.CommonAlterTableSqlBuilder;

/**
 * @author lind
 * @since 1.0
 */
public class MysqlAlterTableSqlBuilder extends CommonAlterTableSqlBuilder {

    @Override
    protected PrepareSqlFragments createAlterColumnFragments(RDBColumnMetadata oldColumn, RDBColumnMetadata newColumn) {
        if (newColumn.getComment() != null) {
            return super.createAlterColumnFragments(oldColumn, newColumn)
                    .addSql("comment", "'".concat(newColumn.getComment()).concat("'"));
        }
        return super.createAlterColumnFragments(oldColumn, newColumn);
    }

    @Override
    protected PrepareSqlFragments createAddColumnFragments(RDBColumnMetadata column) {
        if (column.getComment() != null) {
            return super.createAddColumnFragments(column)
                    .addSql("comment", "'".concat(column.getComment()).concat("'"));
        }
        return super.createAddColumnFragments(column);
    }

    @Override
    protected void appendAddColumnCommentSql(DefaultBatchSqlRequest batch, RDBColumnMetadata column) {

    }
}
