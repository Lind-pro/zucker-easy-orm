package org.zucker.ezorm.rdb.executor.wrapper;

/**
 * @author lind
 * @since 1.0
 */
public interface ColumnWrapperContext<T> {

    int getColumnIndex();

    String getColumnLabel();

    Object getResult();

    T getRowInstance();

    void setRowInstance(T instance);
}
