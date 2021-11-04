package org.zucker.ezorm.rdb.mapping.annotation;

import org.zucker.ezorm.core.DefaultValueGenerator;
import org.zucker.ezorm.core.RuntimeDefaultValue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @auther: lind
 * @since: 1.0
 * @see org.zucker.ezorm.core.DefaultValueGenerator
 */

@Target({ElementType.FIELD, ElementType.METHOD})
public @interface DefaultValue {

    /**
     * @return 生成器ID
     * @see DefaultValueGenerator#getId()
     */
    String generator() default "";

    /**
     * @return 固定默认值
     * @see RuntimeDefaultValue#get()
     */
    String value() default "";

}
