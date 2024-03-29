package org.zucker.ezorm.core;

import com.alibaba.fastjson.JSON;
import org.hswebframework.utils.ClassUtils;
import org.hswebframework.utils.DateTimeUtils;
import org.hswebframework.utils.StringUtils;
import org.hswebframework.utils.time.DateFormatter;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lind
 * @since 1.0
 */
public class SimplePropertyWrapper implements PropertyWrapper {

    private Object value;

    public SimplePropertyWrapper(Object value) {
        this.value = value;
    }

    @Override
    public <T> T getValue() {
        return (T) value;
    }

    @Override
    public int toInt() {
        return StringUtils.toInt(value);
    }

    @Override
    public double toDouble() {
        return StringUtils.toDouble(value);
    }

    @Override
    public boolean isTrue() {
        return StringUtils.isTrue(value);
    }

    @Override
    public Date toDate() {
        if (value instanceof Date) {
            return (Date) value;
        }
        return DateFormatter.fromString(toString());
    }

    @Override
    public Date toDate(String format) {
        if (value instanceof Date) {
            return (Date) value;
        }
        return DateTimeUtils.formatDateString(toString(), format);
    }

    @Override
    public Map<String, Object> toMap() {
        return toBean(Map.class);
    }

    @Override
    public List<Map> toList() {
        return toBeanList(Map.class);
    }

    @Override
    public <T> T toBean(Class<T> type) {
        if (valueTypeOf(type)) {
            return (T) getValue();
        }
        return JSON.parseObject(toString(), type);
    }

    @Override
    public <T> List<T> toBeanList(Class<T> type) {
        if (getValue() instanceof List) {
            return (List) getValue();
        }
        return JSON.parseArray(toString(), type);
    }

    @Override
    public boolean isNullOrEmpty() {
        return StringUtils.isNullOrEmpty(value);
    }

    @Override
    public boolean valueTypeOf(Class<?> type) {
        if (value == null) {
            return false;
        }
        return ClassUtils.instanceOf(value.getClass(), type);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
