package org.zucker.ezorm.rdb.mapping;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface SyncUpdate<E> extends DSLUpdate<E, SyncUpdate<E>> {

    int execute();
}
