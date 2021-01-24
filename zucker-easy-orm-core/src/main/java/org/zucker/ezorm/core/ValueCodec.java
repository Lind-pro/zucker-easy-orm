package org.zucker.ezorm.core;

public interface ValueCodec<E, D> extends Encoder<E>, Decoder<D> {

    E encode(Object value);

    D decode(Object data);
}
