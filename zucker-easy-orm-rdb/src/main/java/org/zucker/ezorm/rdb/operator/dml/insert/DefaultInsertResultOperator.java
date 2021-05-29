package org.zucker.ezorm.rdb.operator.dml.insert;

import lombok.AllArgsConstructor;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.executor.SyncSqlExecutor;
import org.zucker.ezorm.rdb.executor.reactive.ReactiveSqlExecutor;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.utils.ExceptionUtils;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

/**
 * @auther: lind
 * @since: 1.0
 */
@AllArgsConstructor(staticName = "of")
public class DefaultInsertResultOperator implements InsertResultOperator {

    private RDBTableMetadata table;

    private Supplier<SqlRequest> sql;

    @Override
    public Integer sync() {
        return ExceptionUtils.translation(() -> table.findFeatureNow(SyncSqlExecutor.ID).update(sql.get()), table);
    }

    @Override
    public Mono<Integer> reactive() {
        return table.findFeatureNow(ReactiveSqlExecutor.ID)
                .update(Mono.fromSupplier(sql))
                .onErrorMap((err) -> ExceptionUtils.translation(table, err));
    }
}
