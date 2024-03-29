package org.zucker.ezorm.rdb.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.SQLType;

/**
 * @author lind
 * @since 1.0
 */
@AllArgsConstructor(staticName = "of")
public class JdbcDataType implements DataType{

    @Getter
    private SQLType sqlType;

    @Getter
    private Class<?> javaType;

    @Override
    public String getName(){
        return sqlType.getName().toLowerCase();
    }

    @Override
    public String getId() {
        return sqlType.getName().toLowerCase();
    }
}
