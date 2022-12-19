package org.zucker.ezorm.rdb.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.SQLType;

/**
 * @author lind
 * @since 1.0
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class CustomDataType implements DataType {

    private String id;

    private String name;

    private SQLType sqlType;

    private Class<?> javaType;
}
