package org.zucker.ezorm.rdb.mapping.events;

import org.reactivestreams.Publisher;
import org.zucker.ezorm.rdb.events.ContextKeyValue;
import org.zucker.ezorm.rdb.events.EventListener;
import org.zucker.ezorm.rdb.events.EventType;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.ResultOperator;
import reactor.core.publisher.Mono;

import java.lang.reflect.Proxy;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author lind
 * @since 1.0
 */
public class EventResultOperator {

    @SuppressWarnings("all")
    public static <T extends ResultOperator> T create(Supplier<T> operator,
                                                      Class<T> target,
                                                      RDBTableMetadata tableMetadata,
                                                      EventType before,
                                                      EventType after,
                                                      ContextKeyValue<?>... keyValue) {
        if (!tableMetadata.findFeature(EventListener.ID).isPresent()) {
            return operator.get();
        }
        return (T) Proxy
                .newProxyInstance(EventResultOperator.class.getClassLoader(),
                        new Class[]{target},
                        ((proxy, method, args) -> {
                            ContextKeyValue<Boolean> isReactive = MappingContextKeys.reactive(Publisher.class.isAssignableFrom(method.getReturnType()));

                            try {
                                DefaultReactiveResultHolder holder = new DefaultReactiveResultHolder();
                                tableMetadata.fireEvent(before, ctx -> {
                                    ctx.set(keyValue).set(isReactive, MappingContextKeys.reactiveResult(holder));
                                });

                                if (!isReactive.getValue()) {
                                    Object result = method.invoke(operator.get(), args);
                                    tableMetadata.fireEvent(after, ctx -> {
                                        ctx.set(keyValue).set(MappingContextKeys.result(result), isReactive);
                                    });
                                }
                                boolean isMono = Mono.class.isAssignableFrom(method.getReturnType());
                                return holder
                                        .doBefore()
                                        .then(Mono.fromCallable(() -> method.invoke(operator.get(), args)))
                                        .flatMapMany(result -> {
                                            return holder
                                                    .doInvoke()
                                                    .thenMany((Publisher<Object>) result)
                                                    // 有返回值
                                                    .map(holder::doAfter)
                                                    // 无返回值
                                                    .defaultIfEmpty(holder.doAfterNoResult())
                                                    .flatMap(Function.identity());
                                        })
                                        .as(isMono ? Mono::from : flux -> flux);
                            } catch (Throwable e) {
                                tableMetadata.fireEvent(after, ctx -> {
                                    ctx.set(keyValue).set(MappingContextKeys.error(e), isReactive);
                                });
                                throw e;
                            }
                        }));
    }
}
