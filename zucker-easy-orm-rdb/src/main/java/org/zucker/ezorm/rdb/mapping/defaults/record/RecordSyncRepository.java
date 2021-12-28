package org.zucker.ezorm.rdb.mapping.defaults.record;

import org.zucker.ezorm.rdb.executor.wrapper.ResultWrapper;
import org.zucker.ezorm.rdb.mapping.defaults.DefaultSyncRepository;
import org.zucker.ezorm.rdb.mapping.defaults.SimpleColumnMapping;
import org.zucker.ezorm.rdb.mapping.events.MappingContextKeys;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.DatabaseOperator;

import java.util.function.Supplier;

/**
 * @auther: lind
 * @since: 1.0
 */
public class RecordSyncRepository<K> extends DefaultSyncRepository<Record, K> {
    public RecordSyncRepository(DatabaseOperator operator, String table) {
        this(operator, () -> operator.getMetadata().getTable(table).orElseThrow(() -> new UnsupportedOperationException("table [" + table + "] doesn't exist")));
    }

    public RecordSyncRepository(DatabaseOperator operator, Supplier<RDBTableMetadata> table) {
        super(operator, table, Record.class, RecordResultWrapper.of(SimpleColumnMapping.of(DefaultRecord.class, table)));
    }

    @Override
    protected void initMapping(Class<Record> entityType) {
        this.mapping = SimpleColumnMapping.of(entityType, tableSupplier);
        defaultContextKeyValue.add(MappingContextKeys.columnMapping(mapping));
    }
}
