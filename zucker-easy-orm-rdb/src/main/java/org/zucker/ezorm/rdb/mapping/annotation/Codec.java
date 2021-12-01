package org.zucker.ezorm.rdb.mapping.annotation;

import java.lang.annotation.*;

/**
 * @see JsonCodec
 * @see EnumCodec
 * @see DateTimeCodec
 * @auther: lind
 * @since: 1.0
 */
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Codec {
}
