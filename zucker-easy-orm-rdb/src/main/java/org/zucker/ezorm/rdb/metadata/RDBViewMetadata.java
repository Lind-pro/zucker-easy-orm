package org.zucker.ezorm.rdb.metadata;

import org.zucker.ezorm.core.meta.ObjectType;

/**
 * @author lind
 * @since 1.0
 */
public class RDBViewMetadata extends AbstractTableOrViewMetadata{

    @Override
    public ObjectType getObjectType() {
        return RDBObjectType.view;
    }
}
