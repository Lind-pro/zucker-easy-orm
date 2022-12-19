package org.zucker.ezorm.rdb.operator.builder.fragments.ddl;

import org.zucker.ezorm.core.DefaultValue;
import org.zucker.ezorm.rdb.executor.*;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.metadata.RDBIndexMetadata;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.NativeSql;
import org.zucker.ezorm.rdb.operator.builder.fragments.PrepareSqlFragments;

import java.util.Optional;

/**
 * @author lind
 * @since 1.0
 */
public class CommonCreateTableSqlBuilder implements CreateTableSqlBuilder {

    public static final CommonCreateTableSqlBuilder INSTANCE = new CommonCreateTableSqlBuilder();

    @Override
    public SqlRequest build(RDBTableMetadata table) {
        DefaultBatchSqlRequest sql = new DefaultBatchSqlRequest();
        PrepareSqlFragments createTable = PrepareSqlFragments.of();
        createTable.addSql("create table", table.getFullName(), "(");
        int index = 0;
        for (RDBColumnMetadata column : table.getColumns()) {
            if (index++ != 0) {
                createTable.addSql(",");
            }
            createTable.addSql(column.getQuoteName());
            if (column.getColumnDefinition() != null) {
                createTable.addSql(column.getColumnDefinition());
            } else {
                createTable.addSql(column.getDialect().buildColumnDataType(column));
                DefaultValue defaultValue = column.getDefaultValue();
                if (defaultValue instanceof NativeSql) {
                    createTable.addSql("default", ((NativeSql) defaultValue).getSql());
                }
                if (column.isNotNull() || column.isPrimaryKey()) {
                    createTable.addSql("not null");
                }
                if (column.isPrimaryKey()) {
                    createTable.addSql("primary key");
                }
            }
            if (column.getComment() != null) {
                sql.addBatch(SqlRequests.of(String.format("comment on column %s is '%s'", column.getFullTableName(), column.getComment())));
            }

        }
        createTable.addSql(")");
        if (table.getComment() != null) {
            sql.addBatch(SqlRequests.of(String.format("comment on table %s is '%s'", table.getFullName(), table.getComment())));
        }
        table.findFeature(CreateIndexSqlBuilder.ID)
                .ifPresent(builder -> {
                    for (RDBIndexMetadata tableIndex : table.getIndexes()) {
                        sql.addBatch(builder.build(CreateIndexParameter.of(table, tableIndex)));
                    }
                });
        sql.setSql(createTable.toRequest().getSql());
        return sql;
    }
}
