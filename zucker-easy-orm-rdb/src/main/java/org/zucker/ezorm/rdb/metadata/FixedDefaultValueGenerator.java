package org.zucker.ezorm.rdb.metadata;

import lombok.Getter;
import org.zucker.ezorm.core.DefaultValue;
import org.zucker.ezorm.core.DefaultValueGenerator;
import org.zucker.ezorm.core.RuntimeDefaultValue;
import org.zucker.ezorm.core.meta.ObjectMetadata;

/**
 * @author lind
 * @since 1.0
 */
public class FixedDefaultValueGenerator<M extends ObjectMetadata> implements DefaultValueGenerator<M> {

    private DefaultValue value;

    @Getter
    private String sortId;

    @Getter
    private String name;

    public static <M extends ObjectMetadata> FixedDefaultValueGenerator<M> of(Object value) {
        FixedDefaultValueGenerator<M> generator = new FixedDefaultValueGenerator<>();
        generator.sortId = "fixed" + value;
        generator.name = "固定值" + value;
        generator.value = (RuntimeDefaultValue) () -> value;
        return generator;
    }

    @Override
    public DefaultValue generate(M metadata) {
        return value;
    }
}
