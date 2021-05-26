package org.zucker.ezorm.rdb.operator.dml.query;

import org.reactivestreams.Publisher;
import org.zucker.ezorm.rdb.operator.ResultOperator;
import reactor.core.publisher.Flux;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface QueryResultOperator<E, R> extends ResultOperator<E, R> {

    @Override
    R sync();

    @Override
    Flux<E> reactive();
}
