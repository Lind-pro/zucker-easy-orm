package org.zucker.ezorm.rdb.operator.builder;

/**
 * @author lind
 * @since 1.0
 */
public enum FragmentBlock {

    before,
    selectColumn,
    selectFrom,
    join,
    where,
    term,
    groupBy,
    orderBy,
    having,
    paging,
    other,
    after,
}
