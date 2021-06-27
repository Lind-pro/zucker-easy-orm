package org.zucker.ezorm.rdb.operator.ddl;

import org.reactivestreams.Publisher;
import org.zucker.ezorm.rdb.operator.ResultOperator;
import reactor.core.publisher.Mono;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface TableDDLResultOperator extends ResultOperator<Boolean, Boolean> {

    @Override
    Boolean sync();

    @Override
    Mono<Boolean> reactive();
}
