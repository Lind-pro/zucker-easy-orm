package org.zucker.ezorm.rdb.supports.h2;

import org.zucker.ezorm.rdb.operator.builder.fragments.PrepareSqlFragments;
import org.zucker.ezorm.rdb.operator.builder.fragments.term.EnumInFragmentBuilder;

/**
 * @author lind
 * @since 1.0
 */
class H2EnumInFragmentBuilder extends EnumInFragmentBuilder {

    static H2EnumInFragmentBuilder notIn = new H2EnumInFragmentBuilder(true);
    static H2EnumInFragmentBuilder in = new H2EnumInFragmentBuilder(false);


    H2EnumInFragmentBuilder(boolean not) {
        super(not);
    }

    @Override
    protected PrepareSqlFragments bitAnd(String column, long value) {
        return PrepareSqlFragments.of("BITAND(", column, ",", "? )").addParameter(value);
    }
}
