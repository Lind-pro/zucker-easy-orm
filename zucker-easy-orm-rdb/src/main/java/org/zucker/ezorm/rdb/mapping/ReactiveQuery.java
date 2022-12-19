package org.zucker.ezorm.rdb.mapping;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 响应式动态查询接口
 *
 * @author lind
 * @since 1.0
 */
public interface ReactiveQuery<T> extends DSLQuery<ReactiveQuery<T>> {

    /**
     * 执行查询并获取返回数据流，如果未查询到结果将返回{@lin Flux#empty()}
     *
     * @return 查询结果流
     */
    Flux<T> fetch();

    /**
     * 执行count 查询，并返回count 查询结果
     *
     * @return count 结果
     */
    Mono<Integer> count();

    /**
     * 执行查询并返回单个数据流
     *
     * @return 如果未查询到结果将返回{@link Mono#empty()}
     */
    Mono<T> fetchOne();
}
