package org.zucker.ezorm.rdb.metadata;

import org.zucker.ezorm.core.PropertyWrapper;
import org.zucker.ezorm.core.meta.AbstractColumnMetadata;
import org.zucker.ezorm.core.meta.ObjectType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @auther: lind
 * @since: 1.0
 */
public class RDBSchemaMetadata extends AbstractColumnMetadata {

    private final List<ObjectType> allObjectType = Arrays.asList(RDBObjectType.table,RDBObjectType.view);

    public RDBSchemaMetadata(String name){
        {
            // 查询
//            addFeature();

            addFeature();
        }

    }
    @Override
    public PropertyWrapper setProperty(String property, Object value) {
        return null;
    }

    @Override
    public ObjectType getObjectType() {
        return null;
    }

    public Optional<TableOrViewMetadata> getTableOrView(String name,boolean autoLoad){
        return Optional.of()
    }
}
