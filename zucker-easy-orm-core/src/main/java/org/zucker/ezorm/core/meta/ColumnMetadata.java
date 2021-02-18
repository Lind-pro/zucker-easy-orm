package org.zucker.ezorm.core.meta;

import org.zucker.ezorm.core.DefaultValue;
import org.zucker.ezorm.core.DictionaryCodec;
import org.zucker.ezorm.core.PropertyWrapper;
import org.zucker.ezorm.core.ValueCodec;

/**
 * @auther: lind
 * @since: 1.0
 */
@SuppressWarnings("all")
public interface ColumnMetadata extends FeatureSupportedMetadata, ObjectMetadata, Cloneable {

    String getName();

    String getAlias();

    String getComment();

    Class getJavaType();

    ValueCodec getValueCodec();

    DictionaryCodec getDictionaryCodec();

    DefaultValue getDefaultValue();

    PropertyWrapper getProperty(String property);

    PropertyWrapper getProperty(String property, Object defaultValue);

    PropertyWrapper setProperty(String property, Object value);
}
