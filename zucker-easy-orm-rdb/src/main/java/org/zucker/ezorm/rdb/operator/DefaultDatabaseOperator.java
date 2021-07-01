package org.zucker.ezorm.rdb.operator;

import lombok.AllArgsConstructor;
import org.zucker.ezorm.rdb.mapping.ReactiveRepository;
import org.zucker.ezorm.rdb.mapping.SyncRepository;
import org.zucker.ezorm.rdb.mapping.defaults.record.Record;
import org.zucker.ezorm.rdb.metadata.RDBDatabaseMetadata;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.metadata.TableOrViewMetadata;
import org.zucker.ezorm.rdb.operator.ddl.TableBuilder;
import org.zucker.ezorm.rdb.operator.dml.QueryOperator;
import org.zucker.ezorm.rdb.operator.dml.delete.DeleteOperator;
import org.zucker.ezorm.rdb.operator.dml.insert.InsertOperator;
import org.zucker.ezorm.rdb.operator.dml.query.ExecutableQueryOperator;
import org.zucker.ezorm.rdb.operator.dml.update.UpdateOperator;
import org.zucker.ezorm.rdb.operator.dml.upsert.UpsertOperator;

/**
 * @auther: lind
 * @since: 1.0
 */
@AllArgsConstructor(staticName = "of")
public class DefaultDatabaseOperator implements DatabaseOperator, DMLOperator, SQLOperator, DDLOperator {

    private final RDBDatabaseMetadata metadata;


    @Override
    public TableBuilder createOrAlter(String name) {
        return null;
    }

    @Override
    public TableBuilder createOrAlter(RDBTableMetadata newTable) {
        return null;
    }

    @Override
    public QueryOperator query(TableOrViewMetadata tableOrView) {
        return new ExecutableQueryOperator(tableOrView);
    }

    @Override
    public DeleteOperator delete(RDBTableMetadata table) {
        return null;
    }

    @Override
    public UpdateOperator update(RDBTableMetadata table) {
        return null;
    }

    @Override
    public InsertOperator insert(RDBTableMetadata table) {
        return null;
    }

    @Override
    public UpsertOperator upsert(RDBTableMetadata table) {
        return null;
    }

    @Override
    public QueryOperator query(String tableOrView) {
        return null;
    }

    @Override
    public UpdateOperator update(String table) {
        return null;
    }

    @Override
    public InsertOperator insert(String table) {
        return null;
    }

    @Override
    public DeleteOperator delete(String table) {
        return null;
    }

    @Override
    public UpsertOperator upsert(String table) {
        return null;
    }

    @Override
    public <K> SyncRepository<Record, K> createRepository(String tableName) {
        return null;
    }

    @Override
    public <K> ReactiveRepository<Record, K> createReactiveRepository(String tableName) {
        return null;
    }

    @Override
    public RDBDatabaseMetadata getMetadata() {
        return metadata;
    }

    @Override
    public DMLOperator dml() {
        return this;
    }

    @Override
    public DDLOperator ddl() {
        return this;
    }

    @Override
    public SQLOperator sql() {
        return this;
    }
}
