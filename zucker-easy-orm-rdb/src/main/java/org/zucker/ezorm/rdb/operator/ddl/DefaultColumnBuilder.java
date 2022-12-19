package org.zucker.ezorm.rdb.operator.ddl;

import org.zucker.ezorm.core.DefaultValue;
import org.zucker.ezorm.rdb.metadata.DataType;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.metadata.ValueCodecFactory;

import java.util.function.Consumer;

/**
 * @author lind
 * @since 1.0
 */
public class DefaultColumnBuilder implements ColumnBuilder {

    private RDBColumnMetadata columnMetadata;

    private TableBuilder tableBuilder;

    private RDBTableMetadata tableMetadata;

    public DefaultColumnBuilder(RDBColumnMetadata columnMetadata, TableBuilder tableBuilder, RDBTableMetadata tableMetadata) {
        this.columnMetadata = columnMetadata;
        this.tableBuilder = tableBuilder;
        this.tableMetadata = tableMetadata;
    }

    @Override
    public ColumnBuilder custom(Consumer<RDBColumnMetadata> consumer) {
        consumer.accept(columnMetadata);
        return this;
    }

    @Override
    public ColumnBuilder name(String name) {
        columnMetadata.setName(name);
        return this;
    }

    @Override
    public ColumnBuilder alias(String name) {
        columnMetadata.setAlias(name);
        return this;
    }

    @Override
    public ColumnBuilder dataType(String dataType) {
        columnMetadata.setDataType(dataType);
        return this;
    }

    @Override
    public ColumnBuilder type(String typeId) {
        type(columnMetadata.getDialect().convertDataType(typeId));
        return this;
    }

    @Override
    public ColumnBuilder type(DataType type) {
        columnMetadata.setType(type);
        return this;
    }

    @Override
    public ColumnBuilder comment(String comment) {
        columnMetadata.setComment(comment);
        return this;
    }

    @Override
    public ColumnBuilder notNull() {
        columnMetadata.setNotNull(true);
        return this;
    }

    @Override
    public ColumnBuilder primaryKey() {
        columnMetadata.setPrimaryKey(true);
        return this;
    }

    @Override
    public ColumnBuilder columnDef(String def) {
        columnMetadata.setColumnDefinition(def);
        return this;
    }

    @Override
    public ColumnBuilder defaultValue(DefaultValue value) {
        columnMetadata.setDefaultValue(value);
        return this;
    }

    @Override
    public ColumnBuilder length(int len) {
        columnMetadata.setLength(len);
        return this;
    }

    @Override
    public ColumnBuilder length(int precision, int scale) {
        columnMetadata.setLength(precision);
        columnMetadata.setScale(scale);
        columnMetadata.setPrecision(precision);
        return this;
    }

    @Override
    public ColumnBuilder property(String propertyName, Object value) {
        columnMetadata.setProperty(propertyName, value);
        return this;
    }

    @Override
    public TableBuilder commit() {
        if (columnMetadata.getValueCodec() == null) {
            tableMetadata.findFeature(ValueCodecFactory.ID)
                    .flatMap(factory -> factory.createValueCodec(columnMetadata))
                    .ifPresent(columnMetadata::setValueCodec);
        }
        tableMetadata.addColumn(columnMetadata);
        return tableBuilder;
    }
}
