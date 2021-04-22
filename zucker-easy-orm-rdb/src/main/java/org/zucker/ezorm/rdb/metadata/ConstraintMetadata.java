package org.zucker.ezorm.rdb.metadata;

import org.zucker.ezorm.core.meta.ObjectMetadata;
import org.zucker.ezorm.core.meta.ObjectType;

import java.util.Set;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface ConstraintMetadata extends ObjectMetadata {

    @Override
    default ObjectType getObjectType() {
        return RDBObjectType.constraint;
    }

    Set<String> getColumns();

    boolean isPrimaryKey();
}
