package org.zucker.ezorm.core;

import java.util.function.Supplier;

/**
 * @author lind
 * @since 1.0
 */
public interface ObjectConverter {

    <T> T convert(Object from ,Class<T> to);

    <T> T convert(Object from , Supplier<T> to);
}
