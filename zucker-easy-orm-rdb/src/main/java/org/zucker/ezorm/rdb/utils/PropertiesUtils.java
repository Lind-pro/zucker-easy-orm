package org.zucker.ezorm.rdb.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @auther: lind
 * @since: 1.0
 */
@Slf4j
public class PropertiesUtils {

    @SuppressWarnings("all")
    public static List<Object> convertList(Object object) {
        if (object == null) {
            return Collections.emptyList();
        }
        if (object instanceof Object[]) {
            return Arrays.asList((Object[]) object);
        }
        if (object instanceof Collection) {
            return new ArrayList<>((Collection) object);
        }
        return Arrays.asList(object);
    }

    public static Optional<Field> getPropertyField(Class clazz, String fieldName) {
        Field field = null;
        Class tmp = clazz;
        do {
            try {
                field = tmp.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                tmp = tmp.getSuperclass();
                if (tmp == null || tmp == Object.class) {
                    break;
                }
            }
        } while (field == null);
        return Optional.ofNullable(field);
    }
}
