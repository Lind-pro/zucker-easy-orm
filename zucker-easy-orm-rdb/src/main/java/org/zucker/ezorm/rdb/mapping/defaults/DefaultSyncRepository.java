package org.zucker.ezorm.rdb.mapping.defaults;

import org.zucker.ezorm.rdb.executor.wrapper.ResultWrapper;
import org.zucker.ezorm.rdb.mapping.SyncDelete;
import org.zucker.ezorm.rdb.mapping.SyncQuery;
import org.zucker.ezorm.rdb.mapping.SyncRepository;
import org.zucker.ezorm.rdb.mapping.SyncUpdate;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.DatabaseOperator;
import org.zucker.ezorm.rdb.operator.dml.QueryOperator;

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
        this(operator,
                () -> operator
                        .getMetadata()
                        .getTable(table)
                        .orElseThrow(() -> new UnsupportedOperationException("table [" + table + "] doesn't exist")), type, wrapper);
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
    public Optional<E> findById(K primaryKey) {
        return Optional.ofNullable(primaryKey)
                .flatMap(k -> createQuery().where(getIdColumn(), k).fetchOne());
    }

    @Override
    public List<E> findById(Collection<K> primaryKey) {
        if (primaryKey.isEmpty()) {
            return new ArrayList<>();
        }
        return createQuery().where().in(getIdColumn(), primaryKey).fetch();
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
    public SaveResult save(Collection<E> list) {
        return doSave(list).sync();
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
    public void insert(E data) {
        doInsert(data).sync();
    }

    @Override
    public int insertBatch(Collection batch) {
        if (batch.size() == 0) {
            return 0;
        }
        return doInsert(batch).sync();
    }

    @Override
    public SyncQuery<E> createQuery() {
        return new DefaultSyncQuery<>(getTable(), mapping, operator.dml(), wrapper);
    }

    @Override
    public SyncUpdate<E> createUpdate() {
        return new DefaultSyncUpdate<>(getTable(), operator.dml().update(getTable().getFullName()), mapping);
    }

    @Override
    public SyncDelete createDelete() {
        return new DefaultSyncDelete(getTable(), operator.dml().delete(getTable().getFullName()));
    }

    @Override
    public QueryOperator nativeQuery() {
        return operator.dml().query(getTable());
    }
}
