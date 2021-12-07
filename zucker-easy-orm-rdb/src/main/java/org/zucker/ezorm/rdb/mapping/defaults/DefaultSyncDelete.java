package org.zucker.ezorm.rdb.mapping.defaults;

import org.zucker.ezorm.rdb.mapping.SyncDelete;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.dml.delete.DeleteOperator;

/**
 * @auther: lind
 * @since: 1.0
 */
public class DefaultSyncDelete extends DefaultDelete<SyncDelete> implements SyncDelete {
    public DefaultSyncDelete(RDBTableMetadata table, DeleteOperator operator) {
        super(table, operator);
    }

    @Override
    public int execute() {
        return doExecute().sync();
    }
}
