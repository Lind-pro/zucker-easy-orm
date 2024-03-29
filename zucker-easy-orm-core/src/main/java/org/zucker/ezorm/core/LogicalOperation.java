package org.zucker.ezorm.core;

import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.core.param.TermType;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 常用逻辑操作接口，通过继承或实现此接口，可提供逻辑操作支持如：when,each,
 * 常用操作方法:<br/>
 * {@link LogicalOperation#when(boolean, Consumer)} <br>
 * {@link LogicalOperation#when(String, Object, Function, Function}<br>
 * {@link LogicalOperation#each}
 *
 * @author lind
 * @since 1.0
 */
public interface LogicalOperation<T extends LogicalOperation> extends TermTypeConditionalSuppport {

    /**
     * 遍历一个集合，进行条件追加
     * 例如：<br>
     * <pre>
     * query.or().each("areaId",[1,2,3],(query->query::$like$)
     * </pre>
     * 将追加SQL
     * <pre>
     * areaId like '%1%' or areaId like '%2%' or areaId like '%3%'
     * </pre>
     *
     * @param column         要追加到的列名
     * @param list           集合
     * @param accepterGetter 追加方式函数
     * @param <E>            集合中元素类型
     * @return this {@link T}
     */
    default <E> T each(String column, Collection<E> list, Function<T, TermTypeConditionalSuppport.SimpleAccepter<T, E>> accepterGetter) {
        if (null != list)
            list.forEach(o -> accepterGetter.apply((T) this).accept(column, o));
        return (T) this;
    }

    /**
     * 功能与{@link this#each(String, Collection, Function)}类似
     * 例如:<br>
     * <pre>
     * query.or().each("name","like",["%张三%","%李四%"],(query)->query::or)
     * </pre>
     * 将追加SQL
     * <pre>
     * name like '%张三% or name like '%李四%'
     * </pre>
     *
     * @param column         要追加到的列名
     * @param termType       条件类型
     * @param list           集合
     * @param accepterGetter 追加方式函数
     * @param <E>            集合中元素类型
     * @return this {@link T}
     */
    default <E> T each(String column, String termType, Collection<E> list, Function<T, TermTypeConditionalSuppport.Accepter<T, E>> accepterGetter) {
        if (null != list)
            list.forEach(o -> accepterGetter.apply((T) this).accept(column, termType, o));
        return (T) this;
    }

    /**
     * 参照 {@link Conditional#each(String, Collection, Function)}
     * 提供了一个valueMapper 进行值转换如:
     * <br>
     * query.or().each("areaId",[1,2,3],(query)->query::$like$,(value)->","+value+","<br>
     * 将追加SQL<br>
     * areaId like '%,1,%' or areaId like '%,2,%' or areaId like '%,3,%'
     *
     * @param column         要追加到的列名
     * @param list           集合
     * @param accepterGetter 追加方式函数
     * @param valueMapper    值转换函数 {@link Function}
     * @param <E>            集合中元素类型
     * @param <V>
     * @return this {@link T}
     */
    default <E, V> T each(String column, Collection<E> list, Function<T, TermTypeConditionalSuppport.SimpleAccepter<T, V>> accepterGetter, Function<E, V> valueMapper) {
        if (null != list)
            list.forEach(o -> accepterGetter.apply((T) this).accept(column, valueMapper.apply(o)));
        return (T) this;
    }


    /**
     * 功能与 {@link this#each(String, Collection, Function, Function)} 类似，只是多了一个termType.使用{@link Accepter}进行操作
     *
     * @param column         要追加的列名
     * @param termType       条件类型
     * @param list           集合
     * @param accepterGetter 自定义操作实现
     * @param valueMapper    值转换函数 {@link Function}
     * @param <E>            集合中元素类型
     * @param <V>
     * @return this {@link T}
     */
    default <E, V> T each(String column, String termType, Collection<E> list, Function<T, TermTypeConditionalSuppport.Accepter<T, V>> accepterGetter, Function<E, V> valueMapper) {
        if (null != list)
            list.forEach(o -> accepterGetter.apply((T) this).accept(column, termType, valueMapper.apply(o)));
        return (T) this;
    }

    /**
     * 直接遍历一个集合，并提供一个消费者进行自定义操作
     * 如：
     * <pre>
     *     query.or().each([1,2,3],(query,i)->query.is('age',i)
     * </pre>
     * 生成条件如下：
     * <pre>
     *     where age=1 or age=2 or age=3
     * </pre>
     *
     * @param list     遍历的集合
     * @param consumer 自定义操作实现
     * @param <E>      集合中元素的类型
     * @return this {@link T}
     * @see BiConsumer
     */
    default <E> T each(Collection<E> list, BiConsumer<T, E> consumer) {
        if (null != list)
            list.forEach(o -> consumer.accept((T) this, o));
        return (T) this;
    }

    /**
     * 遍历一个Map，进行条件追加
     * 例如:
     * <pre>
     *     query.each({name:"test"},(query)->query::$like$)
     * </pre>
     * 生成条件如下:
     * <pre>
     *     where name like '%test%'
     * </pre>
     *
     * @param mapParam map参数
     * @param accepter 追加方式函数
     * @return this {@link T}
     */
    default T each(Map<String, Object> mapParam, Function<T, TermTypeConditionalSuppport.SimpleAccepter<T, Object>> accepter) {
        if (null != mapParam)
            mapParam.forEach((k, v) -> accepter.apply((T) this).accept(k, v));
        return (T) this;
    }

    /**
     * 功能与{@link this#each(Map, Function)}类似
     * 例如:
     * <pre>
     *     query.each({name:"test",age:30},'is',(query)->query::or)
     * </pre>
     * 生成条件如下
     * <pre>
     *     where name ='test' or age =30
     * </pre>
     *
     * @param mapParam map参数
     * @param termType 条件类型 {@link TermType}
     * @param accepter 拼接类型函数
     * @return
     */
    default T each(Map<String, Object> mapParam, String termType, Function<T, TermTypeConditionalSuppport.Accepter<T, Object>> accepter) {
        if (null != mapParam)
            mapParam.forEach((k, v) -> accepter.apply((T) this).accept(k, termType, v));
        return (T) this;
    }

    /**
     * 指定一个前置条件，当条件满足的时候，调用回调进行自定义参数<br>
     * 如:query(age>10,query->query.gt("age",10))
     *
     * @param condition 前置条件
     * @param consumer  回调
     * @return this {@link T}
     */
    default T when(boolean condition, Consumer<T> consumer) {
        if (condition) {
            consumer.accept((T) this);
        }
        return (T) this;
    }

    /**
     * 功能与 {@link this#when(boolean, Consumer)} 类似
     * 通过BooleanSupplier获取条件，例如
     * <pre>
     *     query.when(()->age>10,query->query.gt("age",10));
     * </pre>
     *
     * @param condition
     * @param consumer
     * @return this {@link T}
     */
    default T when(BooleanSupplier condition, Consumer<T> consumer) {
        return when(condition.getAsBoolean(), consumer);
    }

    /**
     * 指定前置条件，列名，参数值，条件构造函数。当条件满足的时候，执行构造器添加条件，例如:
     * <pre>
     *     query.when(age>10,"age",10,query->query:gt);
     * </pre>
     * 等同于
     * <pre>
     *     if(age>10)query.gt(age,10);
     * </pre>
     *
     * @param condition 前置条件
     * @param column    要查询的列名
     * @param value     参数
     * @param accepter  条件构造函数
     * @param <V>       参数类型
     * @return this {@link T}
     */
    default <V> T when(boolean condition, String column, V value, Function<T, TermTypeConditionalSuppport.SimpleAccepter<T, V>> accepter) {
        if (condition) {
            accepter.apply((T) this).accept(column, value);
        }
        return (T) this;
    }

    /**
     * 指定列名，参数值，条件判断函数，条件构造函数进行条件添加，例如:
     * <pre>
     *     query.when("age",10,value->value>10,query->query::gt)
     * </pre>
     *
     * @param column    列名
     * @param value     参数
     * @param condition 条件判断函数
     * @param accepter  条件构造函数
     * @param <V>       参数类型
     * @return this {@link T}
     */
    default <V> T when(String column, V value, Function<V, Boolean> condition, Function<T, TermTypeConditionalSuppport.SimpleAccepter<T, V>> accepter) {
        return when(condition.apply(value), column, value, accepter);
    }

    /**
     * 功能与{@link Condition#when(boolean, String, Object, Function)} 类似，可自定义termType 例如:
     * <pre>
     *     query.when(true,"age","like",10,query->query::or)
     * </pre>
     *
     * @param condition 条件
     * @param column    列名
     * @param termType  条件类型
     * @param value     参数
     * @param accepter  条件构造函数
     * @param <V>       参数类型
     * @return this{@link T}
     */
    default <V> T when(boolean condition, String column, String termType, V value, Function<T, TermTypeConditionalSuppport.Accepter<T, V>> accepter) {
        if (condition) {
            accepter.apply((T) this).accept(column, termType, value);
        }
        return (T) this;
    }

    /**
     * 功能与 {@link Conditional#when(String, Object, Function, Function)}类似，可自定义termType，如:
     * <pre>
     *  query.when("age","like",10,value->value==10,query->query::or)
     * </pre>
     *
     * @param column
     * @param termType
     * @param value
     * @param condition
     * @param accepter
     * @param <V>
     * @return
     */
    default <V> T when(String column, String termType, V value, Function<V, Boolean> condition, Function<T, TermTypeConditionalSuppport.Accepter<T, V>> accepter) {
        return when(condition.apply(value), column, termType, value, accepter);
    }

    default <V> T when(Optional<V> value, BiConsumer<T, V> consumer) {
        value.ifPresent(v -> consumer.accept((T) this, v));
        return (T) this;
    }

    default <R> R as(Function<T, R> function) {
        return function.apply((T) this);
    }

    default T accept(Consumer<T> consumer) {
        consumer.accept((T) this);
        return (T) this;
    }

    default <V> T accept(V v, BiConsumer<T, V> consumer) {
        consumer.accept((T) this, v);
        return (T) this;
    }

    default <V> T accept(MethodReferenceColumn<V> column, BiConsumer<T, V> consumer) {
        V v = column.get();
        if (v != null) {
            consumer.accept((T) this, v);
        }
        return (T) this;
    }
}

