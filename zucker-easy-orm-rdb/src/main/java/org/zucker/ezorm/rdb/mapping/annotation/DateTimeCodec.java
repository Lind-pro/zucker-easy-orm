package org.zucker.ezorm.rdb.mapping.annotation;

import java.lang.annotation.*;

/**
 * @auther: lind
 * @since: 1.0
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Codec
public @interface DateTimeCodec {
    String format() default "yyyy-MM-dd HH:mm:ss";
}
