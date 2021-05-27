package org.zucker.ezorm.rdb.operator.dml.delete;

import org.reactivestreams.Publisher;
import org.zucker.ezorm.rdb.operator.ResultOperator;
import reactor.core.publisher.Mono;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface DeleteResultOperator extends ResultOperator<Integer,Integer> {

    @Override
    Integer sync();

    @Override
    Mono<Integer> reactive();
}
