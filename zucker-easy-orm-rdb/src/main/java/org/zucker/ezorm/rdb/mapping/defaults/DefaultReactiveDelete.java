package org.zucker.ezorm.rdb.mapping.defaults;

import org.zucker.ezorm.rdb.events.ContextKeyValue;
import org.zucker.ezorm.rdb.mapping.ReactiveDelete;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.dml.delete.DeleteOperator;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

/**
 * @author lind
 * @since 1.0
 */
public class DefaultReactiveDelete extends DefaultDelete<ReactiveDelete> implements ReactiveDelete {

    public DefaultReactiveDelete(RDBTableMetadata tableMetadata,
                                 DeleteOperator operator,
                                 ContextKeyValue<?>... keyValues) {
        super(tableMetadata, operator, keyValues);
    }

    public BiFunction<ReactiveDelete, Mono<Integer>, Mono<Integer>> mapper = (reactiveDelete, interMono) -> interMono;

    @Override
    public Mono<Integer> execute() {
        return mapper.apply(this, doExecute().reactive());
    }

    @Override
    public ReactiveDelete onExecute(BiFunction<ReactiveDelete, Mono<Integer>, Mono<Integer>> mapper) {
        this.mapper = this.mapper.andThen(r -> mapper.apply(this, r));
        return this;
    }
}
