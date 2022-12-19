package org.zucker.ezorm.rdb.operator;

import org.zucker.ezorm.rdb.mapping.ReactiveRepository;
import org.zucker.ezorm.rdb.mapping.SyncRepository;
import org.zucker.ezorm.rdb.mapping.defaults.record.Record;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.metadata.TableOrViewMetadata;
import org.zucker.ezorm.rdb.operator.dml.QueryOperator;
import org.zucker.ezorm.rdb.operator.dml.delete.DeleteOperator;
import org.zucker.ezorm.rdb.operator.dml.insert.InsertOperator;
import org.zucker.ezorm.rdb.operator.dml.update.UpdateOperator;
import org.zucker.ezorm.rdb.operator.dml.upsert.UpsertOperator;

/**
 * @author lind
 * @since 1.0
 */
public interface DMLOperator {

    QueryOperator query(TableOrViewMetadata tableOrView);

    DeleteOperator delete(RDBTableMetadata table);

    UpdateOperator update(RDBTableMetadata table);

    InsertOperator insert(RDBTableMetadata table);

    UpsertOperator upsert(RDBTableMetadata table);

    QueryOperator query(String tableOrView);

    UpdateOperator update(String table);

    InsertOperator insert(String table);

    DeleteOperator delete(String table);

    UpsertOperator upsert(String table);

    <K> SyncRepository<Record, K> createRepository(String tableName);

    <K> ReactiveRepository<Record,K> createReactiveRepository(String tableName);
}
