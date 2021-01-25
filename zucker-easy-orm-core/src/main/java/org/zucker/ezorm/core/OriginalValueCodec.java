package org.zucker.ezorm.core;

/**
 * @auther: lind
 * @since: 1.0
 */
public class OriginalValueCodec implements ValueCodec {

    public static final OriginalValueCodec INSTANCE = new OriginalValueCodec();
    @Override
    public Object encode(Object value) {
        return value;
    }

    @Override
    public Object decode(Object data) {
        return data;
    }
}
