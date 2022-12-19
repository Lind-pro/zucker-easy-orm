package org.zucker.ezorm.rdb.supports.postgres;

import org.zucker.ezorm.rdb.metadata.DataType;

import java.sql.JDBCType;
import java.sql.SQLType;

/**
 * @author lind
 * @since 1.0
 */
public class JsonType implements DataType {

    public static JsonType INSTANCE = new JsonType();

    @Override
    public Class<?> getJavaType() {
        return String.class;
    }

    @Override
    public String getId() {
        return "json";
    }

    @Override
    public String getName() {
        return "json";
    }

    @Override
    public SQLType getSqlType() {
        return JDBCType.CLOB;
    }
}
