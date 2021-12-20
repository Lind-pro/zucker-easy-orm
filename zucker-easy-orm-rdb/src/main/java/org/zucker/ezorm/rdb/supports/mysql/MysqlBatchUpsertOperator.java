package org.zucker.ezorm.rdb.supports.mysql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.collections.CollectionUtils;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.executor.SyncSqlExecutor;
import org.zucker.ezorm.rdb.executor.reactive.ReactiveSqlExecutor;
import org.zucker.ezorm.rdb.mapping.defaults.SaveResult;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.PrepareSqlFragments;
import org.zucker.ezorm.rdb.operator.builder.fragments.insert.BatchInsertSqlBuilder;
import org.zucker.ezorm.rdb.operator.dml.insert.InsertColumn;
import org.zucker.ezorm.rdb.operator.dml.insert.InsertOperatorParameter;
import org.zucker.ezorm.rdb.operator.dml.upsert.SaveOrUpdateOperator;
import org.zucker.ezorm.rdb.operator.dml.upsert.SaveResultOperator;
import org.zucker.ezorm.rdb.operator.dml.upsert.UpsertColumn;
import org.zucker.ezorm.rdb.operator.dml.upsert.UpsertOperatorParameter;
import org.zucker.ezorm.rdb.utils.ExceptionUtils;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.function.Supplier;

/**
 * @auther: lind
 * @since: 1.0
 */
@SuppressWarnings("all")
public class MysqlBatchUpsertOperator implements SaveOrUpdateOperator {

    private RDBTableMetadata table;

    private SaveOrUpdateOperator fallback;

    private MysqlUpsertBatchInsertSqlBuilder builder;

    private Boolean fallbacked;

    protected boolean doFallback() {
        if (fallbacked != null) {
            return fallbacked;
        }
        RDBColumnMetadata idColumn = table.getColumns()
                .stream()
                .filter(RDBColumnMetadata::isPrimaryKey)
                .findFirst()
                .orElse(null);

        // 没有ID
        if (idColumn == null) {
            return fallbacked = true;
        }
        // 有指定唯一索引
        if (table
                .getIndexes()
                .stream()
                .anyMatch(index -> index.isUnique() && !index.isPrimaryKey())) {
            return fallbacked = true;
        }
        // 有指定唯一约束
        if (CollectionUtils.isNotEmpty(table.getConstraints())) {
            return fallbacked = true;
        }
        return fallbacked = false;
    }

    @Override
    public SaveResultOperator execute(UpsertOperatorParameter parameter) {
        if (doFallback()) {
            return fallback.execute(parameter);
        }
        return new MysqlSaveResultOperator(() -> builder
                .build(new MysqlUpsertOperatorParameter(parameter)), parameter.getValues().size());
    }

    class MysqlUpsertOperatorParameter extends InsertOperatorParameter {
        private boolean doNothingOnConflict;

        public MysqlUpsertOperatorParameter(UpsertOperatorParameter parameter) {
            doNothingOnConflict = parameter.isDoNothingOnConflict();
            setColumns(parameter.toInsertColumns());
            setValues(parameter.getValues());
        }
    }

    @AllArgsConstructor
    private class MysqlSaveResultOperator implements SaveResultOperator {
        Supplier<SqlRequest> sqlRequest;
        int total;

        @Override
        public SaveResult sync() {
            return ExceptionUtils.translation(() -> {
                SyncSqlExecutor sqlExecutor = table.findFeatureNow(SyncSqlExecutor.ID);
                sqlExecutor.update(sqlRequest.get());
                return SaveResult.of(0, total);
            }, table);
        }

        @Override
        public Mono<SaveResult> reactive() {
            return Mono.defer(() -> {
                return Mono.just(sqlRequest.get())
                        .as(table.findFeatureNow(ReactiveSqlExecutor.ID)::update)
                        .map(i -> SaveResult.of(0, total))
                        .as(ExceptionUtils.translation(table));
            });
        }
    }

    private class MysqlUpsertBatchInsertSqlBuilder extends BatchInsertSqlBuilder {

        public MysqlUpsertBatchInsertSqlBuilder(RDBTableMetadata table) {
            super(table);
        }

        @Override
        protected PrepareSqlFragments beforeBuild(InsertOperatorParameter parameter, PrepareSqlFragments fragments) {
            if (((MysqlUpsertOperatorParameter) parameter).doNothingOnConflict) {
                return fragments.addSql("insert ignore into")
                        .addSql(table.getFullName());
            }
            return super.beforeBuild(parameter, fragments);
        }

        @Override
        protected PrepareSqlFragments afterBuild(Set<InsertColumn> columns, InsertOperatorParameter parameter, PrepareSqlFragments sql) {

            if (((MysqlUpsertOperatorParameter) parameter).doNothingOnConflict) {
                return sql;
            }
            sql.addSql("on duplicate key update");

            int index = 0;
            boolean more = false;
            for (InsertColumn column : columns) {
                index++;
                if (column instanceof UpsertColumn && ((UpsertColumn) column).isUpdateIgnore()) {
                    continue;
                }
                RDBColumnMetadata columnMetadata = table.getColumn(column.getColumn()).orElse(null);
                if (columnMetadata == null
                        || columnMetadata.isPrimaryKey()
                        || !columnMetadata.isUpdatable()
                        || !columnMetadata.isSaveable()) {
                    continue;
                }
                if (more) {
                    sql.addSql(",");
                }
                more = true;
                sql.addSql(columnMetadata.getQuoteName()).addSql("=");
                sql.addSql("coalesce(", "VALUES(", columnMetadata.getQuoteName(), ")", ",", columnMetadata.getFullName(), ")");
            }
            return sql;
        }
    }

}
