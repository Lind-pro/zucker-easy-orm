package org.zucker.ezorm.rdb.mapping.parser;

import org.zucker.ezorm.rdb.mapping.EntityPropertyDescriptor;
import org.zucker.ezorm.rdb.metadata.DataType;

/**
 * @author lind
 * @since 1.0
 */
public interface DataTypeResolver {

    DataType resolve(EntityPropertyDescriptor descriptor);
}
