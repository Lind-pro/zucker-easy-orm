package org.zucker.ezorm.rdb.mapping;

/**
 * @author lind
 * @since 1.0
 */
public interface SyncDelete extends DSLDelete<SyncDelete> {

    int execute();
}
