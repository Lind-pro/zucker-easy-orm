package org.zucker.ezorm.rdb.mapping.defaults;

import org.apache.commons.collections.CollectionUtils;
import org.reactivestreams.Publisher;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrapper;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrappers;
import org.zucker.ezorm.rdb.mapping.ReactiveDelete;
import org.zucker.ezorm.rdb.mapping.ReactiveQuery;
import org.zucker.ezorm.rdb.mapping.ReactiveRepository;
import org.zucker.ezorm.rdb.mapping.ReactiveUpdate;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.DatabaseOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @auther: lind
 * @since: 1.0
 */
public class DefaultReactiveRepository<E, K> extends DefaultRepository<E> implements ReactiveRepository<E, K> {

    public DefaultReactiveRepository(DatabaseOperator operator, String table, Class<E> type, ResultWrapper<E, ?> wrapper) {
        this(operator,
                () -> operator.getMetadata()
                        .getTable(table)
                        .orElseThrow(() -> new UnsupportedOperationException("table [" + table + "] doesn't exist")), type, wrapper);
    }

    public DefaultReactiveRepository(DatabaseOperator operator, RDBTableMetadata table, Class<E> type, ResultWrapper<E, ?> wrapper) {
        this(operator, () -> table, type, wrapper);
    }

    public DefaultReactiveRepository(DatabaseOperator operator, Supplier<RDBTableMetadata> table, Class<E> type, ResultWrapper<E, ?> wrapper) {
        super(operator, table, wrapper);
        initMapping(type);
    }

    @Override
    public Mono<E> newInstance() {
        return Mono.just(wrapper.newRowInstance());
    }

    @Override
    public Mono<E> findById(Mono<K> key) {
        return key.flatMap(k -> createQuery().where(getIdColumn(), k).fetchOne());
    }

    @Override
    public Flux<E> findById(Flux<K> key) {
        return key.collectList()
                .filter(CollectionUtils::isNotEmpty)
                .flatMapMany(idList -> createQuery().where().in(getIdColumn(), idList).fetch());
    }

    @Override
    public Mono<Integer> deleteById(Publisher<K> key) {
        return Flux.from(key)
                .collectList()
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(list -> createDelete().where().in(getIdColumn(), list).execute())
                .defaultIfEmpty(0);
    }

    @Override
    public Mono<SaveResult> save(Publisher<E> data) {
        return Flux.from(data)
                .collectList()
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(list -> doSave(list).reactive())
                .defaultIfEmpty(SaveResult.of(0, 0));
    }

    @Override
    public Mono<Integer> updateById(K id, Mono<E> data) {
        return data.flatMap(_data -> createUpdate()
                .where(getIdColumn(), id)
                .set(_data)
                .execute());
    }

    @Override
    public Mono<Integer> insert(Publisher<E> data) {
        return Flux.from(data)
                .flatMap(e -> doInsert(e).reactive())
                .reduce(Math::addExact)
                .defaultIfEmpty(0);
    }

    @Override
    public Mono<Integer> insertBatch(Publisher<? extends Collection<E>> data) {
        return Flux.from(data)
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(e -> doInsert(e).reactive())
                .reduce(Math::addExact)
                .defaultIfEmpty(0);
    }

    @Override
    public ReactiveQuery<E> createQuery() {
        return new DefaultReactiveQuery<>(getTable(), mapping, operator.dml(), wrapper, getDefaultContextKeyValue());
    }

    @Override
    public ReactiveUpdate<E> createUpdate() {
        // TODO
        return new ;
    }

    @Override
    public ReactiveDelete createDelete() {
        return null;
    }
}
