package org.zucker.ezorm.core;

public interface Encoder<T> {

    T encode(Object payload);
}
