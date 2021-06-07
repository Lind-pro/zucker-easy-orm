package org.zucker.ezorm.rdb.mapping;

import org.zucker.ezorm.core.Conditional;
import org.zucker.ezorm.core.MethodReferenceColumn;
import org.zucker.ezorm.core.StaticMethodReferenceColumn;
import org.zucker.ezorm.core.param.QueryParam;
import org.zucker.ezorm.rdb.operator.dml.SortOrderSupplier;
import org.zucker.ezorm.rdb.operator.dml.query.SortOrder;

/**
 * @auther: lind
 * @since: 1.0
 */
@SuppressWarnings("all")
public interface DSLQuery<ME extends DSLQuery> extends Conditional<ME> {

    ME select(String... columns);

    ME selectExcludes(String... columns);

    <T> ME select(StaticMethodReferenceColumn<T>... column);

    <T> ME select(MethodReferenceColumn<T>... column);

    <T> ME selectExcludes(StaticMethodReferenceColumn<T>... column);

    <T> ME selectExcludes(MethodReferenceColumn<T>... column);

    ME paging(int pageIndex, int pageSize);

    ME orderBy(SortOrder... orders);

    ME orderBy(SortOrderSupplier... orders);

    ME setParam(QueryParam param);
}
