package org.zucker.ezorm.rdb.mapping;

import java.util.List;
import java.util.Optional;

/**
 * @author lind
 * @since 1.0
 */
public interface SyncQuery<T> extends DSLQuery<SyncQuery<T>> {

    List<T> fetch();

    Optional<T> fetchOne();

    int count();
}
