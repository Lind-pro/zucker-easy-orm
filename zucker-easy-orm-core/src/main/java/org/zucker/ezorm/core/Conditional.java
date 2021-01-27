package org.zucker.ezorm.core;

import org.hswebframework.utils.StringUtils;
import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.core.param.TermType;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface Conditional<T extends Conditional> extends LogicalOperation<T>, TermTypeConditionalSuppport {

    /**
     * 嵌套条件，如：where name=? or (age>18 and age<90)
     *
     * @return
     */
    NestConditional<T> nest();

    NestConditional<T> orNest();

    default T nest(Consumer<NestConditional<T>> consumer) {
        NestConditional<T> nest = nest();
        consumer.accept(nest);
        return nest.end();
    }

    /**
     * and or 切换
     */

    T and();

    T or();

    /**
     * 自定义and 和or 的操作
     */

    default T and(Consumer<T> consumer) {
        consumer.accept(this.and());
        return (T) this;
    }

    default T or(Consumer<T> consumer) {
        consumer.accept(this.or());
        return (T) this;
    }

    /**
     * 自定义条件类型 and 和or的操作
     */
    T and(String column, String termType, Object value);

    T or(String column, String termType, Object value);

    default <B> T and(StaticMethodReferenceColumn<B> column, String termType, Object value) {
        return and(column.getColumn(), termType, value);
    }

    default <B> T or(StaticMethodReferenceColumn<B> column, String termType, Object value) {
        return or(column.getColumn(), termType, value);
    }

    default T where(String column, Object value) {
        return and(column, TermType.eq, value);
    }

    default <B> T where(StaticMethodReferenceColumn<B> column, Object value) {
        return and(column, TermType.eq, value);
    }

    default <B> T where(MethodReferenceColumn<B> column) {
        return and(column.getColumn(), TermType.eq, column.get());
    }

    default T where() {
        return (T) this;
    }

    default T where(Consumer<Conditional<T>> consumer) {
        consumer.accept(this);
        return (T) this;
    }

    default T and(Supplier<Term> termSupplier) {
        Term term = termSupplier.get();
        term.setType(Term.Type.and);
        accept(term);
        return (T) this;
    }

    default T or(Supplier<Term> termSupplier) {
        Term term = termSupplier.get();
        term.setType(Term.Type.or);
        accept(term);
        return (T) this;
    }

    default T and(String column, Object value) {
        return and(column, TermType.eq, value);
    }

    default T is(String column, Object value) {
        return accept(column, TermType.eq, value);
    }

    default T or(String column, Object value) {
        return or(column, TermType.eq, value);
    }

    default T like(String column, Object value) {
        return accept(column, TermType.like, value);
    }

    default T like$(String column, Object value) {
        if (value == null) {
            return like(column, null);
        }
        return accept(column, TermType.like, StringUtils.concat(value, "%"));
    }

    default T $like(String column, Object value) {
        if (value == null) {
            return like(column, null);
        }
        return accept(column, TermType.like, StringUtils.concat("%", value));
    }

    default T $like$(String column, Object value) {
        if (value == null) {
            return like(column, null);
        }
        return accept(column, TermType.like, StringUtils.concat("%", value, "%"));
    }

    default T accept(String column, String termType, Object value) {
        return getAccepter().accept(column, termType, value);
    }

    Accepter<T, Object> getAccepter();

    T accept(Term term);
}
