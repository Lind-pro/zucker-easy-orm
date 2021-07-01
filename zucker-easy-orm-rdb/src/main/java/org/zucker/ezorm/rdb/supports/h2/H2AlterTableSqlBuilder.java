package org.zucker.ezorm.rdb.supports.h2;

import org.zucker.ezorm.core.DefaultValue;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.NativeSql;
import org.zucker.ezorm.rdb.operator.builder.fragments.PrepareSqlFragments;
import org.zucker.ezorm.rdb.operator.builder.fragments.ddl.CommonAlterTableSqlBuilder;

/**
 * @auther: lind
 * @since: 1.0
 */
public class H2AlterTableSqlBuilder extends CommonAlterTableSqlBuilder {

    @Override
    protected PrepareSqlFragments createAlterColumnFragments(RDBColumnMetadata oldColumn, RDBColumnMetadata newColumn) {
        PrepareSqlFragments fragments = PrepareSqlFragments.of();

        fragments.addSql("alter table", oldColumn.getOwner().getFullName(), "alter", oldColumn.getQuoteName());
        if (newColumn.getColumnDefinition() != null) {
            fragments.addSql(newColumn.getColumnDefinition());
        } else {
            fragments.addSql(newColumn.getDataType(), newColumn.isNotNull() ? "not null" : "null");
            DefaultValue defaultValue = newColumn.getDefaultValue();

            if (defaultValue instanceof NativeSql) {
                fragments.addSql("default", ((NativeSql) defaultValue).getSql());
            }
        }
        return fragments;
    }
}
