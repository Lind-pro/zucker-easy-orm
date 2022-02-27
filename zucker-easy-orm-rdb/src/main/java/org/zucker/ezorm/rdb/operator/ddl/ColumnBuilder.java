package org.zucker.ezorm.rdb.operator.ddl;

import org.zucker.ezorm.core.DefaultValue;
import org.zucker.ezorm.core.RuntimeDefaultValue;
import org.zucker.ezorm.rdb.metadata.DataType;
import org.zucker.ezorm.rdb.metadata.NativeSqlDefaultValue;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.util.Date;
import java.util.function.Consumer;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface ColumnBuilder {

    ColumnBuilder custom(Consumer<RDBColumnMetadata> consumer);

    ColumnBuilder name(String name);

    ColumnBuilder alias(String name);

    ColumnBuilder dataType(String dataType);

    ColumnBuilder type(String typeId);

    ColumnBuilder type(DataType type);

    ColumnBuilder comment(String comment);

    ColumnBuilder notNull();

    ColumnBuilder primaryKey();

    ColumnBuilder columnDef(String def);

    ColumnBuilder defaultValue(DefaultValue value);

    ColumnBuilder length(int len);

    ColumnBuilder length(int precision, int scale);

    ColumnBuilder property(String propertyName, Object value);

    TableBuilder commit();

    default ColumnBuilder type(JDBCType jdbcType, Class type) {
        return type(DataType.jdbc(jdbcType, type));
    }

    default ColumnBuilder varchar(int length) {
        return type(JDBCType.VARCHAR, String.class).length(length);
    }

    default ColumnBuilder defaultValueNative(String defaultSql) {
        return defaultValue(NativeSqlDefaultValue.of(defaultSql));
    }

    default ColumnBuilder defaultValueRuntime(RuntimeDefaultValue value) {
        return defaultValue(value);
    }

    default ColumnBuilder number(int precision, int scale) {
        return type(JDBCType.NUMERIC, BigDecimal.class).length(precision, scale);
    }

    default ColumnBuilder number(int len) {
        return type(JDBCType.NUMERIC, Long.class).length(len, 0);
    }

    default ColumnBuilder datetime() {
        return type(JDBCType.TIMESTAMP, Date.class);
    }
}
