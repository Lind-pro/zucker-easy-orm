package org.zucker.ezorm.rdb.mapping.annotation;

import org.zucker.ezorm.rdb.metadata.DataType;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;

import java.lang.annotation.*;
import java.sql.JDBCType;

/**
 * @auther: lind
 * @since: 1.0
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ColumnType {

    /**
     * @return 类型标识
     * @see DataType#getId()
     */
    String typeId() default "";

    /**
     * @return type name
     */
    String name() default "";

    /**
     * @return JDBCType
     * @see DataType#getSqlType()
     */
    JDBCType jdbcType() default JDBCType.VARCHAR;

    /**
     * @return 自定义java类型
     * @see RDBColumnMetadata#getJavaType()
     */
    Class<?> javaType() default Void.class;

    /**
     * @return RDBColumnMetadata#getJavaType()
     * @see DataType
     * @see org.zucker.ezorm.rdb.metadata.JdbcDataType
     * @see RDBColumnMetadata#getJavaType()
     */
    Class<? extends DataType> type() default DataType.class;
}
