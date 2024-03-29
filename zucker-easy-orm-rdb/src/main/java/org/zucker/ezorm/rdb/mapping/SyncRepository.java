package org.zucker.ezorm.rdb.mapping;

import org.zucker.ezorm.rdb.mapping.defaults.SaveResult;
import org.zucker.ezorm.rdb.operator.dml.QueryOperator;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author lind
 * @since 1.0
 */
@SuppressWarnings("all")
public interface SyncRepository<T, K> {

    T newInstance();

    Optional<T> findById(K primaryKey);

    List<T> findById(Collection<K> primaryKey);

    default int deleteById(K... idList) {
        return deleteById(Arrays.asList(idList));
    }

    int deleteById(Collection<K> idList);

    default SaveResult save(T... data) {
        return save(Arrays.asList(data));
    }

    SaveResult save(Collection<T> list);

    int updateById(K id, T data);

    void insert(T data);

    int insertBatch(Collection<T> batch);

    SyncQuery<T> createQuery();

    SyncUpdate<T> createUpdate();

    SyncDelete createDelete();

    /**
     * 获取原始查询操作
     *
     * @return 原始查询操作
     */
    QueryOperator nativeQuery();
}
