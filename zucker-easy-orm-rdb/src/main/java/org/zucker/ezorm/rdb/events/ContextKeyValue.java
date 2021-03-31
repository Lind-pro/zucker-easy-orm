package org.zucker.ezorm.rdb.events;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface ContextKeyValue<T> {

    String getKey();

    T getValue();

    static <T> ContextKeyValue<T> of(String key, T value) {
        return new ContextKeyValue<T>() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public T getValue() {
                return value;
            }
        };
    }

    static <T> ContextKeyValue<T> of(ContextKey<?> key, T value) {
        return of(key.getKey(), value);
    }
}
