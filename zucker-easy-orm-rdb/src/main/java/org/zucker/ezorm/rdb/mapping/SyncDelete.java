package org.zucker.ezorm.rdb.mapping;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface SyncDelete extends DSLDelete<SyncDelete> {

    int execute();
}
