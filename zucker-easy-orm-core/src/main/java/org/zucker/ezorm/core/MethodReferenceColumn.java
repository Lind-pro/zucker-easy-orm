package org.zucker.ezorm.core;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * @author lind
 * @since 1.0
 */
public interface MethodReferenceColumn<T> extends Supplier<T>, Serializable {

    default String getColumn(){
        return MethodReferenceConverter.convertToColumn(this);
    }
}
