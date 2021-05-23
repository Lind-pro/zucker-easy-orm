package org.zucker.ezorm.rdb.mapping.events;

import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface ReactiveResultHolder {

    void after(Function<Object, Mono<Void>> result);

    void before(Mono<Void> listener);
}
