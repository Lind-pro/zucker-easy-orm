package org.zucker.ezorm.rdb.operator.builder.fragments.ddl;

import org.zucker.ezorm.rdb.executor.CreateIndexParameter;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.operator.builder.fragments.PrepareSqlFragments;

/**
 * @author lind
 * @since 1.0
 */
public class CommonDropIndexSqlBuilder implements DropIndexSqlBuilder {

    public static final CommonDropIndexSqlBuilder INSTANCE = new CommonDropIndexSqlBuilder();

    @Override
    public SqlRequest build(CreateIndexParameter parameter) {
        return PrepareSqlFragments.of("drop index")
                .addSql(parameter.getIndex().getName(), "on", parameter.getTable().getFullName())
                .toRequest();
    }
}
