package org.zucker.ezorm.rdb.executor;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface SqlRequest {
    String getSql();

    Object[] getParameters();

    boolean isEmpty();

    default boolean isNotEmpty() {
        return !isEmpty();
    }
}
