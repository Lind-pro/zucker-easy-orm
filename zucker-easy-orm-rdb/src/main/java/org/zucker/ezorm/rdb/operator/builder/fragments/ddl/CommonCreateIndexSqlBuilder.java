package org.zucker.ezorm.rdb.operator.builder.fragments.ddl;

import org.zucker.ezorm.rdb.executor.CreateIndexParameter;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.metadata.RDBIndexMetadata;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.PrepareSqlFragments;

/**
 * @auther: lind
 * @since: 1.0
 */
public class CommonCreateIndexSqlBuilder implements CreateIndexSqlBuilder {

    public static final CommonCreateIndexSqlBuilder INSTANCE = new CommonCreateIndexSqlBuilder();

    @Override
    public SqlRequest build(CreateIndexParameter parameter) {
        RDBIndexMetadata index = parameter.getIndex();
        RDBTableMetadata table = parameter.getTable();
        PrepareSqlFragments fragments = PrepareSqlFragments.of()
                .addSql("create ", index.isUnique() ? "unique" : "", " index", index.getName(), "on", table.getFullName(), "(");
        int i = 0;
        for (RDBIndexMetadata.IndexColumn column : index.getColumns()) {
            RDBColumnMetadata columnMetadata = table
                    .getColumn(column.getColumn())
                    .orElseThrow(() -> new UnsupportedOperationException("为定义的索引列:" + column.getColumn()));

            if (i++ != 0) {
                fragments.addSql(",");
            }
            fragments.addSql(table.getDialect().quote(column.getColumn()))
                    .addSql(column.getSort().name());
        }
        return fragments.addSql(")").toRequest();
    }
}
