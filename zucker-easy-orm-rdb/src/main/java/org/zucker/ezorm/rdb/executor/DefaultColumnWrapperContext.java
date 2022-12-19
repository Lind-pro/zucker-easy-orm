package org.zucker.ezorm.rdb.executor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.zucker.ezorm.rdb.executor.wrapper.ColumnWrapperContext;

/**
 * @author lind
 * @since 1.0
 */
@Setter
@Getter
@AllArgsConstructor
public class DefaultColumnWrapperContext<T> implements ColumnWrapperContext<T> {

    private int columnIndex;

    private String columnLabel;

    private Object result;

    private T rowInstance;
}
