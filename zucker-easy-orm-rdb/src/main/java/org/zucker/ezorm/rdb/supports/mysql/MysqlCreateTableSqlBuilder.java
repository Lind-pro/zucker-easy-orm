package org.zucker.ezorm.rdb.supports.mysql;

import lombok.Getter;
import lombok.Setter;
import org.zucker.ezorm.core.DefaultValue;
import org.zucker.ezorm.rdb.executor.CreateIndexParameter;
import org.zucker.ezorm.rdb.executor.DefaultBatchSqlRequest;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.metadata.RDBIndexMetadata;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.NativeSql;
import org.zucker.ezorm.rdb.operator.builder.fragments.PrepareSqlFragments;
import org.zucker.ezorm.rdb.operator.builder.fragments.ddl.CreateIndexSqlBuilder;
import org.zucker.ezorm.rdb.operator.builder.fragments.ddl.CreateTableSqlBuilder;

/**
 * @author lind
 * @since 1.0
 */
@Getter
@Setter
@SuppressWarnings("all")
public class MysqlCreateTableSqlBuilder implements CreateTableSqlBuilder {

    private String engine = "InnoDB";

    private String charset = "utf8mb4";

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
                if (column.isNotNull() || column.isPrimaryKey()) {
                    createTable.addSql("not null");
                }
                if (column.isPrimaryKey()) {
                    createTable.addSql("primary key");
                }
                DefaultValue defaultValue = column.getDefaultValue();
                if (defaultValue instanceof NativeSql) {
                    createTable.addSql("default", ((NativeSql) defaultValue).getSql());
                }
                if (column.getComment() != null) {
                    createTable.addSql(" comment '".concat(column.getComment().concat("'")));
                }
            }
        }
        createTable.addSql(") ENGINE=", engine, "DEFAULT CHARTSET=", charset);
        if (table.getComment() != null) {
            createTable.addSql("COMMENT=", "'".concat(table.getComment()).concat("'"));
        }

        System.out.println(table.findFeature(CreateIndexSqlBuilder.ID));
        table.findFeature(CreateIndexSqlBuilder.ID)
                .ifPresent(builder -> {
                    System.out.println(builder + "===builder");
                    for (RDBIndexMetadata tableIndex : table.getIndexes()) {
                        sql.addBatch(builder.build(CreateIndexParameter.of(table, tableIndex)));
                    }
                });
        sql.setSql(createTable.toRequest().getSql());
        return sql;
    }
}
