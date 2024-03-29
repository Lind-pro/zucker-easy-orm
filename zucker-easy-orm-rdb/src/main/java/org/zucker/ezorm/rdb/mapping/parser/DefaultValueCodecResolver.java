package org.zucker.ezorm.rdb.mapping.parser;

import org.zucker.ezorm.core.ValueCodec;
import org.zucker.ezorm.rdb.codec.BooleanValueCodec;
import org.zucker.ezorm.rdb.codec.EnumValueCodec;
import org.zucker.ezorm.rdb.codec.JsonValueCodec;
import org.zucker.ezorm.rdb.codec.NumberValueCodec;
import org.zucker.ezorm.rdb.mapping.EntityPropertyDescriptor;
import org.zucker.ezorm.rdb.mapping.annotation.Codec;
import org.zucker.ezorm.rdb.mapping.annotation.DateTimeCodec;
import org.zucker.ezorm.rdb.mapping.annotation.EnumCodec;
import org.zucker.ezorm.rdb.mapping.annotation.JsonCodec;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author lind
 * @since 1.0
 */
public class DefaultValueCodecResolver implements ValueCodecResolver {

    private Map<Class<? extends Annotation>, BiFunction<EntityPropertyDescriptor, Annotation, ValueCodec>> annotationStrategies = new ConcurrentHashMap<>();

    private Map<Class, Function<EntityPropertyDescriptor, ValueCodec>> typeStrategies = new ConcurrentHashMap<>();

    private Map<Predicate<Class>, Function<EntityPropertyDescriptor, ValueCodec>> predicateStrategies = new ConcurrentHashMap<>();

    public <T extends Annotation> void register(Class<T> ann, BiFunction<EntityPropertyDescriptor, T, ValueCodec> codecBiFunction) {
        annotationStrategies.put(ann, (BiFunction) codecBiFunction);
    }

    public void register(Class ann, Function<EntityPropertyDescriptor, ValueCodec> codecFunction) {
        typeStrategies.put(ann, codecFunction);
    }

    public void register(Predicate<Class> ann, Function<EntityPropertyDescriptor, ValueCodec> codecFunction) {
        predicateStrategies.put(ann, codecFunction);
    }

    public static final DefaultValueCodecResolver COMMONS = new DefaultValueCodecResolver();

    static {
        COMMONS.register(DateTimeCodec.class, (field, ann) -> new org.zucker.ezorm.rdb.codec.DateTimeCodec(ann.format(), field.getPropertyType()));

        COMMONS.register(JsonCodec.class, (field, jsonCodec) -> JsonValueCodec.ofField(field.getField()));

        COMMONS.register(EnumCodec.class, (field, jsonCodec) -> new EnumValueCodec(field.getPropertyType(), jsonCodec.toMask()));

        COMMONS.register(Enumerated.class, (field, jsonCodec) -> new EnumValueCodec(field.getPropertyType(), jsonCodec.value() == EnumType.ORDINAL));

        COMMONS.register(Date.class::isAssignableFrom, field -> new org.zucker.ezorm.rdb.codec.DateTimeCodec("yyyy-MM-dd HH", field.getPropertyType()));

        COMMONS.register(Number.class::isAssignableFrom, field -> new NumberValueCodec(field.getPropertyType()));

        COMMONS.register(Boolean.class::isAssignableFrom, field -> new BooleanValueCodec(field.getColumn().getSqlType()));

        COMMONS.register(type ->
                type.isPrimitive()
                        && type == Byte.TYPE
                        && type == Boolean.TYPE
                        && type == Short.TYPE
                        && type == Float.TYPE
                        && type == Double.TYPE
                        && type == Long.TYPE, field -> new NumberValueCodec(field.getPropertyType())
        );

        COMMONS.register(Enum.class::isAssignableFrom, field -> new EnumValueCodec(field.getPropertyType()));

        COMMONS.register(Enum[].class::isAssignableFrom, field -> new EnumValueCodec(field.getPropertyType()));

    }

    @Override
    public Optional<ValueCodec> resolve(EntityPropertyDescriptor descriptor) {
        Set<Annotation> annotations = descriptor.getAnnotations()
                .stream()
                .filter(ann -> null != ann.annotationType().getAnnotation(Codec.class))
                .collect(Collectors.toSet());
        ArrayList<ValueCodec> codec = new ArrayList<>();
        for (Annotation annotation : annotations) {
            BiFunction<EntityPropertyDescriptor, Annotation, ValueCodec> function = annotationStrategies.get(annotation.annotationType());
            if (function != null) {
                codec.add(function.apply(descriptor, annotation));
            }
        }
        if (codec.size() == 1) {
            return Optional.of(codec.get(0));
        }

        return Optional.ofNullable(typeStrategies.get(descriptor.getPropertyType()))
                .map(func -> func.apply(descriptor))
                .map(Optional::of)
                .orElseGet(() ->
                        predicateStrategies.entrySet().stream()
                                .filter(e -> e.getKey().test(descriptor.getPropertyType()))
                                .map(e -> e.getValue().apply(descriptor))
                                .findFirst());
    }
}
