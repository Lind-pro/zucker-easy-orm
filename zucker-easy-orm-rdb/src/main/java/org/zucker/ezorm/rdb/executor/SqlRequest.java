package org.zucker.ezorm.rdb.executor;

import org.zucker.ezorm.rdb.utils.SqlUtils;

/**
 * @author lind
 * @since 1.0
 */
public interface SqlRequest {
    String getSql();

    Object[] getParameters();

    boolean isEmpty();

    default boolean isNotEmpty() {
        return !isEmpty();
    }

    default String toNativeSql() {
        return SqlUtils.toNativeSql(getSql(), getParameters());
    }
}
