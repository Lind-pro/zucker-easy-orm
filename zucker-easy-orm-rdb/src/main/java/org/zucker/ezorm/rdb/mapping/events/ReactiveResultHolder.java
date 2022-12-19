package org.zucker.ezorm.rdb.mapping.events;

import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * @author lind
 * @since 1.0
 */
public interface ReactiveResultHolder {

    void after(Function<Object, Mono<Void>> listener);

    void before(Mono<Void> listener);

    void invoke(Mono<Void> listener);
}
