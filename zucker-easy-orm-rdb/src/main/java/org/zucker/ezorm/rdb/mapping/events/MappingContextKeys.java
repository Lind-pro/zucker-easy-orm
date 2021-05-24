package org.zucker.ezorm.rdb.mapping.events;

import org.zucker.ezorm.rdb.events.ContextKey;
import org.zucker.ezorm.rdb.events.ContextKeyValue;
import org.zucker.ezorm.rdb.mapping.EntityColumnMapping;
import org.zucker.ezorm.rdb.operator.DMLOperator;

import java.util.Map;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface MappingContextKeys {

    ContextKey<?> result = ContextKey.of("result");
    ContextKey repository = ContextKey.of("repository");

    ContextKey<?> instance = ContextKey.of("instance");

    ContextKey<Map<String, Object>> updateColumnInstance = ContextKey.of("updateColumnInstance");

    ContextKey<?> context = ContextKey.of("context");
    ContextKey<String> type = ContextKey.of("type");
    ContextKey<String> executorType = ContextKey.of("executorType");

    ContextKey<Boolean> reactive = ContextKey.of("reactive");

    ContextKey<ReactiveResultHolder> reactiveResultHolder = ContextKey.of("reactive-holder");

    ContextKey<DMLOperator> dml = ContextKey.of("dml");
    ContextKey<EntityColumnMapping> columnMapping = ContextKey.of("columnMapping");

    ContextKey<Throwable> error = ContextKey.of("error");

    static ContextKeyValue<String> type(String val) {
        return ContextKeyValue.of(type, val);
    }

    static ContextKeyValue<String> executorType(String val) {
        return ContextKeyValue.of(executorType, val);
    }

    static <T> ContextKeyValue<DMLOperator> dml(DMLOperator operator) {
        return ContextKeyValue.of(dml, operator);
    }

    static <T> ContextKeyValue<EntityColumnMapping> columnMapping(EntityColumnMapping val) {
        return ContextKeyValue.of(columnMapping, val);
    }
}
