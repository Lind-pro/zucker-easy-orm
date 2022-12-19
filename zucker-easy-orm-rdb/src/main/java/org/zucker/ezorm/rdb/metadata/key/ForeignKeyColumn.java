package org.zucker.ezorm.rdb.metadata.key;

import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;

/**
 * @author lind
 * @since 1.0
 */
public interface ForeignKeyColumn {
    RDBColumnMetadata getTargetColumn();

    RDBColumnMetadata getSourceColumn();
}
