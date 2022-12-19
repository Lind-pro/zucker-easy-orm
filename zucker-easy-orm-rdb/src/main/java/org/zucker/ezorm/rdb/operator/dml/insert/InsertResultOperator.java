package org.zucker.ezorm.rdb.operator.dml.insert;

import org.reactivestreams.Publisher;
import org.zucker.ezorm.rdb.operator.ResultOperator;
import reactor.core.publisher.Mono;

/**
 * @author lind
 * @since 1.0
 */
public interface InsertResultOperator extends ResultOperator<Integer, Integer> {

    @Override
    Integer sync();

    @Override
    Mono<Integer> reactive();
}
