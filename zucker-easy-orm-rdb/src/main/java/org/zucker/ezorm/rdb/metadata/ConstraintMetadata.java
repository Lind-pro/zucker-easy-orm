package org.zucker.ezorm.rdb.metadata;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.zucker.ezorm.core.meta.ObjectMetadata;
import org.zucker.ezorm.core.meta.ObjectType;

import java.util.Set;

/**
 * @author lind
 * @since 1.0
 */
@Getter
@Setter
public class ConstraintMetadata implements ObjectMetadata {

    private String name;
    private String alias;
    private String tableName;
    private Set<String> columns;
    private ConstraintType type;

    @Override
    public ObjectType getObjectType() {
        return RDBObjectType.constraint;
    }

    @Override
    @SneakyThrows
    public ObjectMetadata clone() {
        return (ConstraintMetadata) super.clone();
    }
}
