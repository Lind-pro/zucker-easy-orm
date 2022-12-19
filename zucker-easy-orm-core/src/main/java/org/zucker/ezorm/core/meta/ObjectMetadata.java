package org.zucker.ezorm.core.meta;


/**
 * @author lind
 * @since 1.0
 */
public interface ObjectMetadata extends Cloneable {

    String getName();

    String getAlias();

    ObjectType getObjectType();

    default boolean equalsNameOrAlias(String nameOrAlias) {
        return nameOrAlias != null
                && (nameOrAlias.equalsIgnoreCase(getName()) || nameOrAlias.equalsIgnoreCase(getAlias()));
    }

    ObjectMetadata clone();
}
