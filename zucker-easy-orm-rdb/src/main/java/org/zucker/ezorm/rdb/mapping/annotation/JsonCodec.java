package org.zucker.ezorm.rdb.mapping.annotation;

import java.lang.annotation.*;

/**
 * @auther: lind
 * @since: 1.0
 * @see Codec
 * @see org.zucker.ezorm.rdb.codec.JsonValueCodec
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Codec
public @interface JsonCodec {
}
