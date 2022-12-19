package org.zucker.ezorm.rdb.operator.dml.upsert;

import org.zucker.ezorm.rdb.mapping.defaults.SaveResult;
import org.zucker.ezorm.rdb.operator.ResultOperator;
import reactor.core.publisher.Mono;

/**
 * @author lind
 * @since 1.0
 */
public interface SaveResultOperator extends ResultOperator<SaveResult, SaveResult> {

    @Override
    SaveResult sync();

    @Override
    Mono<SaveResult> reactive();
}
