package org.zucker.ezorm.rdb.mapping.defaults;

import org.zucker.ezorm.rdb.mapping.EntityColumnMapping;
import org.zucker.ezorm.rdb.mapping.SyncUpdate;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.dml.update.UpdateOperator;

/**
 * @auther: lind
 * @since: 1.0
 */
public class DefaultSyncUpdate<E> extends DefaultUpdate<E, SyncUpdate<E>> implements SyncUpdate<E> {
    public DefaultSyncUpdate(RDBTableMetadata table, UpdateOperator operator, EntityColumnMapping mapping) {
        super(table, operator, mapping);
    }

    @Override
    public int execute() {
        return doExecute().sync();
    }
}
