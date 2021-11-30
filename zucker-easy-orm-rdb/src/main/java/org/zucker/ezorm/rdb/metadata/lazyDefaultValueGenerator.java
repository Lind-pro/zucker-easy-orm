package org.zucker.ezorm.rdb.metadata;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.zucker.ezorm.core.DefaultValue;
import org.zucker.ezorm.core.DefaultValueGenerator;
import org.zucker.ezorm.core.RuntimeDefaultValue;
import org.zucker.ezorm.core.meta.ObjectMetadata;

import java.util.function.Supplier;

/**
 * @auther: lind
 * @since: 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class lazyDefaultValueGenerator<T extends ObjectMetadata> implements DefaultValueGenerator<T> {

    private Supplier<DefaultValueGenerator<T>> generator;

    public static <T extends ObjectMetadata> lazyDefaultValueGenerator<T> of(Supplier<DefaultValueGenerator<T>> generator) {
        lazyDefaultValueGenerator<T> valueGenerator = new lazyDefaultValueGenerator<>();
        valueGenerator.generator = generator;
        return valueGenerator;
    }

    private volatile DefaultValue defaultValue;

    @Override
    public String getSortId() {
        return generator.get().getSortId();
    }

    @Override
    public RuntimeDefaultValue generate(T metadata) {
        return () -> {
            if (defaultValue == null) {
                defaultValue = generator.get().generate(metadata);
            }
            return defaultValue.get();
        };
    }

    @Override
    public String getName() {
        return generator.get().getName();
    }
}
