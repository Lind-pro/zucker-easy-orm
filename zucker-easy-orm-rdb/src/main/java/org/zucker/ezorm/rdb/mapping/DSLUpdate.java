package org.zucker.ezorm.rdb.mapping;

import org.zucker.ezorm.core.Conditional;
import org.zucker.ezorm.core.MethodReferenceColumn;
import org.zucker.ezorm.core.StaticMethodReferenceColumn;
import org.zucker.ezorm.core.param.QueryParam;

import java.util.Arrays;

/**
 * @author lind
 * @since 1.0
 */
@SuppressWarnings("all")
public interface DSLUpdate<E, ME extends DSLUpdate> extends Conditional<ME> {

    ME includes(String... properties);

    ME excludes(String... properties);

    ME set(E entity);

    ME set(String column, Object value);

    ME setNull(String column);

    default <R> ME set(MethodReferenceColumn<R> columnAndValue) {
        return set(columnAndValue.getColumn(), columnAndValue.get());
    }

    default ME set(StaticMethodReferenceColumn<E> column, Object value) {
        return set(column.getColumn(), value);
    }

    default ME setNull(StaticMethodReferenceColumn<E> column) {
        return setNull(column.getColumn());
    }

    default ME setNull(MethodReferenceColumn<E> column) {
        return setNull(column.getColumn());
    }

    default ME includes(StaticMethodReferenceColumn<E>... columns) {
        return includes(Arrays.stream(columns).map(StaticMethodReferenceColumn::getColumn).toArray(String[]::new));
    }

    default ME excludes(StaticMethodReferenceColumn<E>... columns) {
        return excludes(Arrays.stream(columns).map(StaticMethodReferenceColumn::getColumn).toArray(String[]::new));
    }

    default ME includes(MethodReferenceColumn<E>... columns) {
        return includes(Arrays.stream(columns).map(MethodReferenceColumn::getColumn).toArray(String[]::new));
    }

    default ME excludes(MethodReferenceColumn<E>... columns) {
        return excludes(Arrays.stream(columns).map(MethodReferenceColumn::getColumn).toArray(String[]::new));
    }

    public QueryParam toQueryParam();
}
