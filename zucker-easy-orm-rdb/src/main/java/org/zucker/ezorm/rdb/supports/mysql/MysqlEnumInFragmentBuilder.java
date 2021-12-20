package org.zucker.ezorm.rdb.supports.mysql;

import org.zucker.ezorm.rdb.operator.builder.fragments.PrepareSqlFragments;
import org.zucker.ezorm.rdb.operator.builder.fragments.term.EnumInFragmentBuilder;

/**
 * @auther: lind
 * @since: 1.0
 */
class MysqlEnumInFragmentBuilder extends EnumInFragmentBuilder {

    static MysqlEnumInFragmentBuilder notIn = new MysqlEnumInFragmentBuilder(true);

    static MysqlEnumInFragmentBuilder in = new MysqlEnumInFragmentBuilder(false);

    MysqlEnumInFragmentBuilder(boolean not) {
        super(not);
    }

    @Override
    protected PrepareSqlFragments bitAnd(String column, long value) {
        return PrepareSqlFragments.of().addSql(column, "&", String.valueOf(value));
    }
}
