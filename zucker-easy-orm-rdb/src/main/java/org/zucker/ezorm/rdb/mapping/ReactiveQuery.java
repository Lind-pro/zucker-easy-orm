package org.zucker.ezorm.rdb.mapping;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface ReactiveQuery<T> extends DSLQuery<ReactiveQuery<T>> {

    Flux<T> fetch();

    Mono<Integer> count();

    Mono<T> fetchOne();
}
