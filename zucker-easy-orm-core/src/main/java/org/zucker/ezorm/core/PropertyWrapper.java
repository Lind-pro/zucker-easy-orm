package org.zucker.ezorm.core;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lind
 * @since 1.0
 */
public interface PropertyWrapper extends Serializable {

    <T> T getValue();

    int toInt();

    double toDouble();

    boolean isTrue();

    Date toDate();

    Date toDate(String format);

    Map<String, Object> toMap();

    List<Map> toList();

    <T> T toBean(Class<T> type);

    <T> List<T> toBeanList(Class<T> type);

    boolean isNullOrEmpty();

    boolean valueTypeOf(Class<?> type);
}
