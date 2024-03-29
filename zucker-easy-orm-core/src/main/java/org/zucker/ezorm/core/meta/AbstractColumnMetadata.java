package org.zucker.ezorm.core.meta;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.zucker.ezorm.core.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lind
 * @since 1.0
 */
@Getter
@Setter
public abstract class AbstractColumnMetadata implements ColumnMetadata {
    protected String name;
    protected String alias;
    protected String comment;
    protected Class javaType;
    protected boolean updatable;
    protected boolean notNull;
    protected DictionaryCodec dictionaryCodec;
    protected ValueCodec valueCodec;
    protected DefaultValue defaultValue;
    protected Map<String, Object> properties = new HashMap<>();

    private Map<String, Feature> features = new HashMap<>();

    @Override
    public String getAlias() {
        if (alias == null) {
            alias = name;
        }
        return alias;
    }

    public Object decode(Object data) {
        if (data == null) {
            return null;
        }
        if (valueCodec != null) {
            data = valueCodec.decode(data);
        }
        if (dictionaryCodec != null) {
            data = dictionaryCodec.decode(data);
        }
        return data;
    }

    public Object encode(Object data) {
        if (data == null) {
            return null;
        }
        if (valueCodec != null) {
            data = valueCodec.encode(data);
        }
        if (dictionaryCodec != null) {
            data = dictionaryCodec.encode(data);
        }
        return data;
    }

    @Override
    public PropertyWrapper getProperty(String property) {
        return new SimplePropertyWrapper(properties.get(property));
    }

    @Override
    public PropertyWrapper getProperty(String property, Object defaultValue) {
        Object value= properties.get(property);
        return new SimplePropertyWrapper(value==null?defaultValue:value);
    }

    public void addFeature(Feature feature){
        if(features==null){
            features=new HashMap<>();
        }
        features.put(feature.getId(),feature);
    }

    @Override
    @SneakyThrows
    public ObjectMetadata clone() {
        return (ObjectMetadata) super.clone();
    }
}
