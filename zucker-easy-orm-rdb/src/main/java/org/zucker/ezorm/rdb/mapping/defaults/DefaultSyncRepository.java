package org.zucker.ezorm.rdb.mapping.defaults;

import org.zucker.ezorm.rdb.executor.wrapper.ResultWrapper;
import org.zucker.ezorm.rdb.mapping.SyncDelete;
import org.zucker.ezorm.rdb.mapping.SyncQuery;
import org.zucker.ezorm.rdb.mapping.SyncRepository;
import org.zucker.ezorm.rdb.mapping.SyncUpdate;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.DatabaseOperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @auther: lind
 * @since: 1.0
 */
public class DefaultSyncRepository<E, K> extends DefaultRepository<E> implements SyncRepository<E, K> {
    public DefaultSyncRepository(DatabaseOperator operator, String table, Class<E> type, ResultWrapper<E, ?> wrapper) {
        this(operator, () -> operator.getMetadata().getTable(table).orElseThrow(() -> new UnsupportedOperationException("table [" + table + "] doesn't exist")), type, wrapper);
    }

    public DefaultSyncRepository(DatabaseOperator operator, RDBTableMetadata table, Class<E> type, ResultWrapper<E, ?> wrapper) {
        this(operator, () -> table, type, wrapper);
    }

    public DefaultSyncRepository(DatabaseOperator operator, Supplier<RDBTableMetadata> table, Class<E> type, ResultWrapper<E, ?> wrapper) {
        super(operator, table, wrapper);
        initMapping(type);
    }

    @Override
    public E newInstance() {
        return wrapper.newRowInstance();
    }

    @Override
    public Optional findById(Object primaryKey) {
        return Optional.empty();
    }

    @Override
    public List<E> findById(Collection<K> primaryKey) {
        if(primaryKey.isEmpty()){
            return new ArrayList<>();
        }
        return createQuery().where().in(getIdColumn(),primaryKey).fetch();
    }

    @Override
    public int deleteById(Collection<K> idList) {
        if (idList == null || idList.isEmpty()) {
            return 0;
        }
        return createDelete()
                .where()
                .in(getIdColumn(), idList)
                .execute();
    }

    @Override
    public SaveResult save(Collection list) {
        return null;
    }

    @Override
    public int updateById(K id, E data) {
        if (id == null || data == null) {
            return 0;
        }
        return createUpdate()
                .set(data)
                .where(getIdColumn(), id)
                .execute();

    }

    @Override
    public void insert(Object data) {

    }

    @Override
    public int insertBatch(Collection batch) {
        return 0;
    }

    @Override
    public SyncQuery<E> createQuery() {
        // todo
        return new DefaultSyncQ;
    }

    @Override
    public SyncUpdate<E> createUpdate() {
        return new DefaultSyncUpdate<>(getTable(), operator.dml().update(getTable().getFullName()), mapping);
    }

    @Override
    public SyncDelete createDelete() {
        return new DefaultSyncDelete(getTable(), operator.dml().delete(getTable().getFullName()));
    }
}
