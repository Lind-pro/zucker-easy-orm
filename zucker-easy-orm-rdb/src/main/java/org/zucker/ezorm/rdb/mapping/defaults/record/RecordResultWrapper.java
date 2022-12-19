package org.zucker.ezorm.rdb.mapping.defaults.record;

import org.zucker.ezorm.rdb.executor.wrapper.AbstractMapResultWrapper;
import org.zucker.ezorm.rdb.executor.wrapper.ColumnWrapperContext;
import org.zucker.ezorm.rdb.mapping.EntityColumnMapping;

import java.util.Optional;

/**
 * @author lind
 * @since 1.0
 */
public class RecordResultWrapper extends AbstractMapResultWrapper<Record> {

    public static RecordResultWrapper INSTANCE = new RecordResultWrapper();

    private EntityColumnMapping mapping;

    public static RecordResultWrapper of(EntityColumnMapping mapping) {
        RecordResultWrapper wrapper = new RecordResultWrapper();
        wrapper.mapping = mapping;
        return wrapper;
    }

    @Override
    public Record newRowInstance() {
        return new DefaultRecord();
    }

    @Override
    public void wrapColumn(ColumnWrapperContext<Record> context) {
        String property = Optional.ofNullable(mapping)
                .flatMap(mapping -> mapping.getPropertyByColumnName(context.getColumnLabel()))
                .orElse(context.getColumnLabel());

        Object value = Optional.ofNullable(mapping)
                .flatMap(mapping -> mapping.getColumnByProperty(property))
                .map(columnMetadata -> columnMetadata.decode(context.getResult()))
                .orElseGet(context::getResult);
        Record record = context.getRowInstance();

        super.doWrap(record, property, value);
    }

    @Override
    public Record getResult() {
        throw new UnsupportedOperationException();
    }
}
