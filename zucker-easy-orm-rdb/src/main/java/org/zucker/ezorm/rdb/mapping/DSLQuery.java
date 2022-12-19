package org.zucker.ezorm.rdb.mapping;

import org.zucker.ezorm.core.Conditional;
import org.zucker.ezorm.core.MethodReferenceColumn;
import org.zucker.ezorm.core.StaticMethodReferenceColumn;
import org.zucker.ezorm.core.param.QueryParam;
import org.zucker.ezorm.rdb.operator.dml.SortOrderSupplier;
import org.zucker.ezorm.rdb.operator.dml.query.SortOrder;

/**
 * 动态DSL查询接口，用于通过DSL方式狗仔动态查询条件
 *
 * @param <ME> 实现次接口的类型
 * @author lind
 * @since 1.0
 * @see QueryParam
 */
@SuppressWarnings("all")
public interface DSLQuery<ME extends DSLQuery<?>> extends Conditional<ME> {

    /**
     * 查询制定的属性（列）
     *
     * @param columns
     * @return
     */
    ME select(String... columns);

    /**
     * 不查询制定的属性(列)
     *
     * @param columns 属性(列)
     * @return this
     */
    ME selectExcludes(String... columns);

    /**
     * 使用getter静态方法引用来指定查询的属性
     *
     * @param column
     * @param <T>
     * @return
     */
    <T> ME select(StaticMethodReferenceColumn<T>... column);

    /**
     * 使用getter 方法引用来制定查询的属性
     * <pre>
     *     createQeury()
     *      .select(user::getName)
     *      .fetch()
     * </pre>
     *
     * @param column 列
     * @param <T>    type
     * @return this
     */
    <T> ME select(MethodReferenceColumn<T>... column);

    /**
     * 使用getter静态方法引用来指定不查询的属性
     * <pre>
     *     createQuery()
     *      .selectExcludes(User::getName)
     *      .fetch()
     * </pre>
     *
     * @param column 列
     * @param <T>    type
     * @return this
     */
    <T> ME selectExcludes(StaticMethodReferenceColumn<T>... column);

    /**
     * 使用getter 方法引用来指定不查询的属性
     * <pre>
     *     createQuery()
     *      .selectExcludes(user::getName)
     *      .fetch()
     * </pre>
     *
     * @param column 列
     * @param <T>    type
     * @return this
     */
    <T> ME selectExcludes(MethodReferenceColumn<T>... column);

    /**
     * 指定分页条件
     *
     * @param pageIndex 页码,从0 开始
     * @param pageSize  每页数量
     * @return this
     */
    ME paging(int pageIndex, int pageSize);

    /**
     * 指定排序，支持多列排序
     *
     * @param orders 排序
     * @return this
     * @see SortOrder
     * @see SortOrder#desc(String)
     * @see SortOrder#asc(String)
     */
    ME orderBy(SortOrder... orders);

    /**
     * 指定排序，支持多列排序
     * @param orders
     * @return this
     * @see Orders
     */
    ME orderBy(SortOrderSupplier... orders);

    ME setParam(QueryParam param);
}
