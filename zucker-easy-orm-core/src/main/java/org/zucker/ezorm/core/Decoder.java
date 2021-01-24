package org.zucker.ezorm.core;

public interface Decoder<T> {
    T decode(Object decode);
}
