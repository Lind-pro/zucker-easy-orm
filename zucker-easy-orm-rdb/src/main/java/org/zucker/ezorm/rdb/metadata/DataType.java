package org.zucker.ezorm.rdb.metadata;

import org.zucker.ezorm.rdb.utils.DataTypeUtils;

import javax.xml.crypto.Data;
import java.sql.JDBCType;
import java.sql.SQLType;
import java.util.function.Function;

/**
 * @author lind
 * @since 1.0
 */
public interface DataType {

    String getId();

    String getName();

    SQLType getSqlType();

    Class<?> getJavaType();

    default boolean isScaleSupport() {
        return getSqlType() == JDBCType.DECIMAL ||
                getSqlType() == JDBCType.DOUBLE ||
                getSqlType() == JDBCType.NUMERIC ||
                getSqlType() == JDBCType.FLOAT;
    }

    default boolean isLengthSupport() {
        return isScaleSupport() ||
                getSqlType() == JDBCType.VARCHAR ||
                getSqlType() == JDBCType.CHAR ||
                getSqlType() == JDBCType.NVARCHAR;
    }

    default boolean isNumber() {
        return DataTypeUtils.typeIsNumber(this);
    }

    static DataType custom(String id, String name, SQLType sqlType, Class<?> javaType) {
        return CustomDataType.of(id, name, sqlType, javaType);
    }

    static DataType jdbc(JDBCType jdbcType, Class<?> javaType) {
        return JdbcDataType.of(jdbcType, javaType);
    }

    static DataType builder(DataType type, Function<RDBColumnMetadata, String> builder) {
        return DataTypeBuilderSupport.of(type, builder);
    }
}
