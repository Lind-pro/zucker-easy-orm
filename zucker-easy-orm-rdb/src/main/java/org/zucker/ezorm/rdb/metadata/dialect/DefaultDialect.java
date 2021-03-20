package org.zucker.ezorm.rdb.metadata.dialect;

import lombok.extern.slf4j.Slf4j;
import org.zucker.ezorm.rdb.metadata.CustomDataType;
import org.zucker.ezorm.rdb.metadata.DataType;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.sql.SQLType;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @auther: lind
 * @since: 1.0
 */
@Slf4j
public abstract class DefaultDialect implements Dialect {

    protected Map<String, DataTypeBuilder> dataTypeMappers = new HashMap<>();

    protected DataTypeBuilder defaultDataTypeBuilder;

    protected Map<String, DataType> dataTypeMapping = new HashMap<>();

    protected Map<Class, JDBCType> classJDBCTypeMapping = new HashMap<>();

    protected void registerDataType(String symbol, DataType dataType) {
        dataTypeMapping.put(symbol, dataType instanceof DataTypeBuilder ? dataType : DataType.builder(dataType, meta -> symbol));
    }

    public DefaultDialect() {
        defaultDataTypeBuilder = meta -> meta.getType().getName().toLowerCase();

        registerDataType("varchar", DataType.builder(DataType.jdbc(JDBCType.VARCHAR, String.class),
                column -> "varchar(" + column.getLength() + ")"));

        registerDataType("nvarchar", DataType.builder(DataType.jdbc(JDBCType.NVARCHAR, String.class),
                column -> "nvarchar(" + column.getLength() + ")"));

        registerDataType("decimal",DataType.builder(DataType.jdbc(JDBCType.DECIMAL, BigDecimal.class),
                column->"decimal("+column.getPrecision(32)+","+column.getScale()+")"));
    }

    protected void addDataTypeBuilder(JDBCType jdbcType, DataTypeBuilder mapper) {
        addDataTypeBuilder(jdbcType.getName().toLowerCase(), mapper);
    }

    @Override
    public void addDataTypeBuilder(String typeId, DataTypeBuilder mapper) {
        dataTypeMappers.put(typeId.toLowerCase(), mapper);
    }

    @Override
    public Optional<SQLType> convertSqlType(Class<?> type) {
        return Optional
                .ofNullable(classJDBCTypeMapping.get(type))
                .map(JDBCType.class::cast);
    }

    @Override
    public String buildColumnDataType(RDBColumnMetadata columnMetadata) {
        if (columnMetadata.getType() == null) {
            throw new UnsupportedOperationException("unknown column type : " + columnMetadata);
        }
        DataType dataType = columnMetadata.getType();
        if (dataType instanceof DataTypeBuilder) {
            return ((DataTypeBuilder) dataType).createColumnDataType(columnMetadata);
        }
        DataTypeBuilder mapper = dataTypeMappers.get(dataType.getId().toLowerCase());
        if (null == mapper) {
            mapper = defaultDataTypeBuilder;
        }
        return mapper.createColumnDataType(columnMetadata);
    }

    @Override
    public DataType convertDataType(String dataType) {
        if (dataType.contains("(")) {
            dataType = dataType.substring(0, dataType.indexOf("("));
        }
        return dataTypeMapping.getOrDefault(dataType.toLowerCase(), convertUnknownDataType(dataType));
    }

    protected DataType convertUnknownDataType(String dataType) {
        JDBCType type;
        try {
            type = JDBCType.valueOf(dataType.toUpperCase());
        } catch (Exception e) {
            type = JDBCType.OTHER;
        }
        return CustomDataType.of(dataType, dataType, type, String.class);
    }

    protected String doClearQuote(String string) {
        if (string.startsWith(getQuoteStart())) {
            string = string.substring(getQuoteStart().length());
        }
        if (string.endsWith(getQuoteEnd())) {
            string = string.substring(0, string.length() - getQuoteEnd().length());
        }
        return string;
    }

    @Override
    public String clearQuote(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }
        if (string.contains(".")) {
            String[] arr = string.split("[.]");
            for (int i = 0; i < arr.length; i++) {
                arr[i] = doClearQuote(arr[i]);
            }
            return String.join(".", arr);
        }
        return doClearQuote(string);
    }
}
