package org.zucker.ezorm.rdb.supports.postgres;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.zucker.ezorm.rdb.metadata.DataType;

import java.sql.JDBCType;
import java.sql.SQLType;

/**
 * @auther: lind
 * @since: 1.0
 */
@Getter
@AllArgsConstructor
public class JsonbType implements DataType {

    public static JsonbType INSTANCE = new JsonbType();

    @Override
    public Class<?> getJavaType() {
        return String.class;
    }

    @Override
    public String getId() {
        return "jsonb";
    }

    @Override
    public String getName() {
        return "jsonb";
    }

    @Override
    public SQLType getSqlType() {
        return JDBCType.CLOB;
    }
}
