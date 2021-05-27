package org.zucker.ezorm.rdb.operator.dml.delete;

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
public class DefaultDeleteResultOperator implements DeleteResultOperator {

    private RDBTableMetadata table;

    private Supplier<SqlRequest> sql;

    @Override
    public Integer sync() {
        return ExceptionUtils.translation(() -> table.findFeature(SyncSqlExecutor.ID)
                .map(executor -> executor.update(sql.get()))
                .orElseThrow(() -> new UnsupportedOperationException("unsupported SyncSqlExecutor")), table);
    }

    @Override
    public Mono<Integer> reactive() {
        return Mono.defer(() -> table.findFeatureNow(ReactiveSqlExecutor.ID)
                .update(Mono.fromSupplier(sql))
                .onErrorMap(error -> ExceptionUtils.translation(table, error)));
    }
}
