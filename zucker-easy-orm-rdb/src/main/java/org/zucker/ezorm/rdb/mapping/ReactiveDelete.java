package org.zucker.ezorm.rdb.mapping;

import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface ReactiveDelete extends DSLDelete<ReactiveDelete> {

    Mono<Integer> execute();

    ReactiveDelete onExecute(BiFunction<ReactiveDelete, Mono<Integer>, Mono<Integer>> mapper);
}
