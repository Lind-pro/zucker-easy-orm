package org.zucker.ezorm.core;

import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.core.param.TermType;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @auther: lind
 * @since: 1.0
 */
@SuppressWarnings("all")
public interface NestConditional<T extends TermTypeConditionalSuppport> extends LogicalOperation<NestConditional<T>>, TermTypeConditionalSuppport {

    T end();

    NestConditional<NestConditional<T>> nest();

    NestConditional<NestConditional<T>> nest(String column, Object value);

    NestConditional<NestConditional<T>> orNest();

    NestConditional<NestConditional<T>> orNest(String column, Object value);

    NestConditional<T> and();

    NestConditional<T> or();

    NestConditional<T> and(String column, String termType, Object value);

    NestConditional<T> or(String column, String termType, Object value);

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

    default <B> NestConditional<T> and(StaticMethodReferenceColumn<B> column, String termType, Object value) {
        return and(column.getColumn(), termType, value);
    }

    default <B> NestConditional<T> or(StaticMethodReferenceColumn<B> column, String termType, Object value) {
        return or(column.getColumn(), termType, value);
    }

    default NestConditional<T> and(String column, Object value) {
        and();
        return and(column, TermType.eq, value);
    }

    default NestConditional<T> or(String column, Object value) {
        or();
        return or(column, TermType.eq, value);
    }

    default NestConditional<T> like(String column, Object value) {
        return accept(column, TermType.like, value);
    }

    default NestConditional<T> like$(String column, Object value) {
        if (value == null) {
            return like(column, null);
        }
        return accept(column, TermType.like, String.valueOf(value).concat("%"));
    }

    default NestConditional<T> $like(String column, Object value) {
        if (value == null) {
            return like(column, null);
        }
        return accept(column, TermType.like, "%".concat(String.valueOf(value)));
    }

    default NestConditional<T> $like$(String column, Object value) {
        if (value == null) {
            return like(column, null);
        }
        return accept(column, TermType.like, "%".concat(String.valueOf(value)).concat("%"));
    }

    default NestConditional<T> is(String column, Object value) {
        return accept(column, TermType.eq, value);
    }

    default NestConditional<T> notLike(String column, Object value) {
        return accept(column, TermType.nLike, value);
    }

    default NestConditional<T> gt(String column, Object value) {
        return accept(column, TermType.gt, value);
    }

    default NestConditional<T> lt(String column, Object value) {
        return accept(column, TermType.lt, value);
    }

    default NestConditional<T> gte(String column, Object value) {
        return accept(column, TermType.gte, value);
    }

    default NestConditional<T> lte(String column, Object value) {
        return accept(column, TermType.lte, value);
    }

    default NestConditional<T> in(String column, Object... values) {
        return accept(column, TermType.in, values);
    }

    default NestConditional<T> in(String column, Object value) {
        return accept(column, TermType.in, value);
    }

    default NestConditional<T> in(String column, Collection values) {
        return accept(column, TermType.in, values);
    }

    default NestConditional<T> notIn(String column, Object value) {
        return accept(column, TermType.nin, value);
    }

    default NestConditional<T> isEmpty(String column) {
        return accept(column, TermType.empty, 1);
    }

    default <B> NestConditional<T> notEmpty(StaticMethodReferenceColumn<B> column) {
        return accept(column, TermType.empty, 1);
    }

    default <B> NestConditional<T> isNull(StaticMethodReferenceColumn<B> column) {
        return accept(column, TermType.isnull, 1);
    }

    default <B> NestConditional<T> notNull(StaticMethodReferenceColumn<B> column) {
        return accept(column, TermType.notnull, 1);
    }

    default <B> NestConditional<T> not(StaticMethodReferenceColumn<B> column, Object value) {
        return accept(column, TermType.not, value);
    }

    default <B> NestConditional<T> not(MethodReferenceColumn<B> column) {
        return accept(column, TermType.not);
    }

    default <B> NestConditional<T> between(MethodReferenceColumn<B> column, Function<B, Object> between, Function<B, Object> and) {
        B value = column.get();
        return accept(column.getColumn(), TermType.btw, Arrays.asList(between.apply(value), and.apply(value)));
    }

    default <B> NestConditional<T> between(StaticMethodReferenceColumn<B> column,Object between,Object and){
        return accept(column,TermType.btw,Arrays.asList(between,and));
    }

    default <B> NestConditional<T> notBetween(StaticMethodReferenceColumn<B> column,Object between,Object and){
        return accept(column,TermType.nbtw,Arrays.asList(between,and));
    }

    default <B> NestConditional<T> accept(StaticMethodReferenceColumn<B> column, String termType, Object value) {
        return getAccepter().accept(column.getColumn(), termType, value);
    }

    default NestConditional<T> accept(String column, String termType, Object value) {
        return getAccepter().accept(column, termType, value);
    }

    default <B> NestConditional<T> accept(MethodReferenceColumn<B> column, String termType) {
        return getAccepter().accept(column.getColumn(), termType, column.get());
    }

    Accepter<NestConditional<T>, Object> getAccepter();

    NestConditional<T> accept(Term term);
}