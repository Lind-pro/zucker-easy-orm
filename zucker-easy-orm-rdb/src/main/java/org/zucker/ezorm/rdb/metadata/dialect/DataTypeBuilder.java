package org.zucker.ezorm.rdb.metadata.dialect;

import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;

/**
 * @author lind
 * @since 1.0
 */
public interface DataTypeBuilder {
    String createColumnDataType(RDBColumnMetadata columnMetadata);
}
