package org.zucker.ezorm.rdb.mapping.annotation;

import java.lang.annotation.*;

/**
 * @auther: lind
 * @since: 1.0
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Comment {
    String value();
}
