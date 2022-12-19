package org.zucker.ezorm.rdb.operator;

import org.reactivestreams.Publisher;

/**
 * @author lind
 * @since 1.0
 */
public interface ResultOperator<E, R> {

    default R block() {
        return sync();
    }

    R sync();

    Publisher<E> reactive();
}
