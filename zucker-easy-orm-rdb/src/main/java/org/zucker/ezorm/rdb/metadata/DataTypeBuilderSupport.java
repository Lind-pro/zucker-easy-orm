package org.zucker.ezorm.rdb.metadata;

import lombok.AllArgsConstructor;
import org.zucker.ezorm.rdb.metadata.dialect.DataTypeBuilder;

import java.sql.SQLType;
import java.util.function.Function;

/**
 * @author lind
 * @since 1.0
 */
@AllArgsConstructor(staticName = "of")
public class DataTypeBuilderSupport implements DataType, DataTypeBuilder {

    private DataType parent;

    private Function<RDBColumnMetadata, String> builder;

    @Override
    public String getId() {
        return parent.getId();
    }

    @Override
    public String getName() {
        return parent.getName();
    }

    @Override
    public SQLType getSqlType() {
        return parent.getSqlType();
    }

    @Override
    public Class<?> getJavaType() {
        return parent.getJavaType();
    }

    @Override
    public String createColumnDataType(RDBColumnMetadata columnMetadata) {
        return builder.apply(columnMetadata);
    }
}
