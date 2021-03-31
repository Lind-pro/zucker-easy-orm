package org.zucker.ezorm.rdb.events;

import java.util.Optional;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface EventContext {

    Object get(String key);

    <T> Optional<T> get(ContextKey<T> key);

    <T> EventContext set(ContextKey<T> key, T value);

    <T> EventContext set(String key, T value);

    EventContext set(ContextKeyValue<?>... keyValue);

    static EventContext create() {
        return new DefaultEventContext();
    }

}
