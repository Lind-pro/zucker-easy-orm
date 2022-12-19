package org.zucker.ezorm.rdb.mapping.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lind
 * @since 1.0
 * @see org.zucker.ezorm.rdb.codec.EnumValueCodec
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumCodec {

    /**
     * @return 是否使用将枚举的序号进行掩码以实现多选
     * @see java.sql.JDBCType#NUMERIC
     * @see Long
     */
    boolean toMask() default false;
}
