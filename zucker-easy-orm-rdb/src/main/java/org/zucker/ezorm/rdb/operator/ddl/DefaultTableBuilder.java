package org.zucker.ezorm.rdb.operator.ddl;

import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.executor.SyncSqlExecutor;
import org.zucker.ezorm.rdb.executor.reactive.ReactiveSqlExecutor;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.metadata.RDBSchemaMetadata;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.ddl.AlterRequest;
import org.zucker.ezorm.rdb.operator.builder.fragments.ddl.AlterTableSqlBuilder;
import org.zucker.ezorm.rdb.operator.builder.fragments.ddl.CreateTableSqlBuilder;
import org.zucker.ezorm.rdb.utils.ExceptionUtils;
import reactor.core.publisher.Mono;

import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @auther: lind
 * @since: 1.0
 */
public class DefaultTableBuilder implements TableBuilder {

    private final RDBTableMetadata table;

    private final RDBSchemaMetadata schema;

    private boolean dropColumn = false;
    private boolean allowAlter = true;
    private boolean autoLoad = true;
    private boolean merge = true;

    public DefaultTableBuilder(RDBTableMetadata table) {
        this.table = table;
        this.schema = table.getSchema();
    }

    @Override
    public TableBuilder addColumn(RDBColumnMetadata column) {
        table.addColumn(column);
        return this;
    }

    @Override
    public TableBuilder custom(Consumer<RDBTableMetadata> consumer) {
        consumer.accept(table);
        return this;
    }

    @Override
    public ColumnBuilder addColumn() {
        RDBColumnMetadata rdbColumnMetadata = table.newColumn();
        return new DefaultColumnBuilder(rdbColumnMetadata, this, table);
    }

    @Override
    public ColumnBuilder addColumn(String name) {
        RDBColumnMetadata rdbColumnMetadata = table.getColumn(name)
                .orElseGet(() -> {
                    RDBColumnMetadata columnMetadata = table.newColumn();
                    columnMetadata.setName(name);
                    return columnMetadata;
                });
        return new DefaultColumnBuilder(rdbColumnMetadata, this, table);
    }

    @Override
    public TableBuilder removeColumn(String name) {
        table.removeColumn(name);
        return this;
    }

    @Override
    public TableBuilder dropColumn(String name) {
        table.removeColumn(name);
        dropColumn = true;
        return this;
    }

    @Override
    public TableBuilder comment(String comment) {
        table.setComment(comment);
        return this;
    }

    @Override
    public TableBuilder alias(String name) {
        table.setAlias(name);
        return this;
    }

    @Override
    public TableBuilder allowAlter(boolean allow) {
        allowAlter = allow;
        return this;
    }

    @Override
    public TableBuilder autoLoad(boolean autoLoad) {
        this.autoLoad = autoLoad;
        return this;
    }

    @Override
    public TableBuilder merge(boolean merge) {
        this.merge = merge;
        return this;
    }

    @Override
    public IndexBuilder index() {
        return new IndexBuilder(this, table);
    }

    @Override
    public ForeignKeyDSLBuilder foreignKey() {
        return new ForeignKeyDSLBuilder(table);
    }

    private SqlRequest buildAlterSql(RDBTableMetadata oldTable) {
        return schema
                .findFeatureNow(AlterTableSqlBuilder.ID)
                .build(AlterRequest.builder()
                        .allowDrop(dropColumn)
                        .newTable(table)
                        .allowAlter(allowAlter)
                        .oldTable(oldTable)
                        .build());
    }

    @Override
    public TableDDLResultOperator commit() {
        return new TableDDLResultOperator() {
            @Override
            public Boolean sync() {
                RDBTableMetadata oldTable = schema.getTable(table.getName(), autoLoad).orElse(null);
                SqlRequest sqlRequest;
                Runnable whenComplete;
                // alter
                if (oldTable != null) {
                    sqlRequest = buildAlterSql(oldTable);
                    if (merge) {
                        whenComplete = () -> oldTable.merge(table);
                    } else {
                        whenComplete = () -> oldTable.replace(table);
                    }
                } else {
                    // create
                    sqlRequest = schema.findFeatureNow(CreateTableSqlBuilder.ID).build(table);
                    whenComplete = () -> schema.addTable(table);
                }
                if (sqlRequest.isEmpty()) {
                    whenComplete.run();
                    return true;
                }
                ExceptionUtils.translation(() -> {
                    schema.findFeatureNow(SyncSqlExecutor.ID).execute(sqlRequest);
                    return true;
                }, schema);
                whenComplete.run();
                return true;
            }

            @Override
            public Mono<Boolean> reactive() {
                ReactiveSqlExecutor sqlExecutor = schema.findFeatureNow(ReactiveSqlExecutor.ID);

                return schema.getTableReactive(table.getName(), autoLoad)
                        .map(oldTable -> {
                            SqlRequest request = buildAlterSql(oldTable);
                            if (request.isEmpty()) {
                                if (merge) {
                                    oldTable.merge(table);
                                } else {
                                    oldTable.replace(table);
                                }
                                return Mono.just(true);
                            }
                            return sqlExecutor.execute(request)
                                    .doOnSuccess(ignore -> oldTable.merge(table))
                                    .thenReturn(true);
                        })
                        .switchIfEmpty(Mono.fromSupplier(() -> {
                            SqlRequest request = schema.findFeatureNow(CreateTableSqlBuilder.ID).build(table);
                            if (request.isEmpty()) {
                                return Mono.just(true);
                            }
                            return sqlExecutor.execute(request)
                                    .doOnSuccess(ignore -> schema.addTable(table))
                                    .thenReturn(true);
                        }))
                        .flatMap(Function.identity());
            }
        };
    }
}
