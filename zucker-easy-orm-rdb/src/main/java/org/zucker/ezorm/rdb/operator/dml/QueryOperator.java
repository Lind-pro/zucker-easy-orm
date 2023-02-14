package org.zucker.ezorm.rdb.operator.dml;

import org.zucker.ezorm.core.Conditional;
import org.zucker.ezorm.core.LogicalOperation;
import org.zucker.ezorm.core.MethodReferenceColumn;
import org.zucker.ezorm.core.StaticMethodReferenceColumn;
import org.zucker.ezorm.core.param.QueryParam;
import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrapper;
import org.zucker.ezorm.rdb.operator.dml.query.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 查询操作抽象类,提供DSL查询操作
 *
 * @author lind
 * @since 1.0
 * <pre>
 * database
 * .dml()
 * .query()
 * .select(count("id","total")
 * .from("user")
 * .where("dsl->dsl.is("name","1")
 * .execute()
 * .reactive(map())
 * .subscribe(data->)
 * </pre>
 */
public abstract class QueryOperator implements LogicalOperation<QueryOperator> {

    /**
     * 指定查询列
     *
     * @param columns 列名
     * @return QueryOperator
     */
    public abstract QueryOperator select(String... columns);

    /**
     * 指定查询列
     *
     * @param columns 列名
     * @return QueryOperator
     */
    public abstract QueryOperator select(Collection<String> columns);

    /**
     * 指定查询列
     *
     * @param column 列名
     * @return QueryOperator
     */
    public abstract QueryOperator select(SelectColumn... column);

    /**
     * 通过{@link SelectColumnSupplier} 来指定查询列
     *
     * @param operators SelectColumnSupplier
     * @return QueryOperator
     * @see Selects
     */
    public QueryOperator select(SelectColumnSupplier... operators) {
        for (SelectColumnSupplier operator : operators) {
            select(operator.get());
        }
        return this;
    }

    /**
     * 使用方法引用来指定查询列
     * <pre>
     *      {@code select(userEntity::getId,userEntity::getName)}
     *  </pre>
     *
     * @param columns 方法引用
     * @param <T>     泛型
     * @return QueryOperator
     */
    @SafeVarargs
    public final <T> QueryOperator select(MethodReferenceColumn<T>... columns) {
        return select(Arrays.stream(columns)
                .map(MethodReferenceColumn::getColumn)
                .toArray(String[]::new));
    }

    /**
     * 使用静态方法引用来指定查询列
     * <pre>
     *      {@code select(UserEntity::getId,UserEntity::getName)}
     *  </pre>
     *
     * @param columns 方法引用
     * @param <T>     泛型
     * @return QueryOperator
     */
    @SafeVarargs
    public final <T> QueryOperator select(StaticMethodReferenceColumn<T>... columns) {
        return select(Arrays.stream(columns)
                .map(StaticMethodReferenceColumn::getColumn)
                .toArray(String[]::new));
    }

    /**
     * 指定忽略查询的列
     *
     * @param columns 列名
     * @return QueryOperator
     */
    public abstract QueryOperator selectExcludes(Collection<String> columns);

    /**
     * 指定忽略查询的列
     *
     * @param columns 列名
     * @return QueryOperator
     */
    public QueryOperator selectExcludes(String... columns) {
        return selectExcludes(Arrays.asList(columns));
    }

    /**
     * 使用静态方法引用来指定不查询的列
     * <pre>
     *     {@code selectExcludes(UserEntity::getId,UserEntity::getName)}
     * </pre>
     *
     * @param columns 方法引用
     * @param <T>     泛型
     * @return QueryOperator
     */
    @SafeVarargs
    public final <T> QueryOperator selectedExcludes(StaticMethodReferenceColumn<T>... columns) {
        return selectExcludes(Arrays.stream(columns)
                .map(StaticMethodReferenceColumn::getColumn)
                .collect(Collectors.toSet()));
    }

    /**
     * 通过回调函数来指定where条件
     * <pre>
     *     where(dsl->dsl.and("name","name")
     * </pre>
     *
     * @param conditionalConsumer conditionalConsumer
     * @return QueryOperator
     */
    public abstract QueryOperator where(Consumer<Conditional<?>> conditionalConsumer);

    /**
     * 添加过滤条件
     *
     * @param term term
     * @return QueryOperator
     */
    public abstract QueryOperator where(Term term);

    /**
     * 添加多个过滤条件
     *
     * @param terms 过滤条件
     * @return QueryOperator
     */
    public abstract QueryOperator where(Collection<Term> terms);

    /**
     * 通过{@link QueryParam} 来设置查询
     *
     * @param param param
     * @return QueryOperator
     * @see QueryParam#getTerms()
     * @see QueryParam#getContext()
     * @see QueryParam#getSorts()
     * @see QueryParam#getPageSize()
     * @see QueryParam#getPageIndex()
     */
    public abstract QueryOperator setParam(QueryParam param);


    /**
     * 根据TermSupplier来添加过滤条件
     *
     * @param condition TermSupplier
     * @return QueryOperator
     */
    public QueryOperator where(TermSupplier... condition) {
        for (TermSupplier operator : condition) {
            where(operator.get());
        }
        return this;
    }

    /**
     * 表关联操作
     *
     * @param on join
     * @return QueryOperator
     * @see Joins
     */
    public abstract QueryOperator join(Join... on);

    /**
     * 表关联操作
     *
     * @param on Join
     * @return QueryOperator
     * @see Joins
     */
    @SafeVarargs
    public final QueryOperator join(Supplier<Join>... on) {
        for (Supplier<Join> joinOperator : on) {
            join(joinOperator.get());
        }
        return this;
    }

    public final QueryOperator leftJoin(String target, Consumer<JoinOperator> joinOperatorConsumer) {
        JoinOperator operator = Joins.left(target);
        joinOperatorConsumer.accept(operator);
        return join(operator.get());
    }

    public final QueryOperator innerJoin(String target, Consumer<JoinOperator> joinOperatorConsumer) {
        JoinOperator operator = Joins.inner(target);
        joinOperatorConsumer.accept(operator);
        return join(operator.get());
    }

    public final QueryOperator rightJoin(String target, Consumer<JoinOperator> joinOperatorConsumer) {
        JoinOperator operator = Joins.right(target);
        joinOperatorConsumer.accept(operator);
        return join(operator.get());
    }

    public final QueryOperator fullJoin(String target, Consumer<JoinOperator> joinOperatorConsumer) {
        JoinOperator operator = Joins.right(target);
        joinOperatorConsumer.accept(operator);
        return join(operator.get());
    }

    public final QueryOperator orderBy(SortOrderSupplier... operators) {
        for (Supplier<SortOrder> operator : operators) {
            orderBy(operator.get());
        }
        return this;
    }

    public abstract QueryOperator orderBy(SortOrder... operators);

    public abstract QueryOperator groupBy(Operator<?>... operators);

    public abstract QueryOperator having(Operator<?>... operators);

    public abstract QueryOperator paging(int pageIndex, int pageSize);

    public abstract QueryOperator forUpdate();

    public abstract QueryOperator context(Map<String, Object> context);

    /**
     * 获取SQL请求
     *
     * @return 获取SQL请求
     */
    public abstract SqlRequest getSql();

    /**
     * 执行查询操作
     *
     * @param wrapper 查询结果包装器
     * @param <E>     行类型
     * @param <R>     结果类型
     * @return 查询结果
     */
    public abstract <E, R> QueryResultOperator<E, R> fetch(ResultWrapper<E, R> wrapper);
}
