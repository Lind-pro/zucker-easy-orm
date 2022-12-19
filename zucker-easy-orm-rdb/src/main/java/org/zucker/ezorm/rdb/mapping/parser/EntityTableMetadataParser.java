package org.zucker.ezorm.rdb.mapping.parser;

import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;

import java.util.Optional;

/**
 * @author lind
 * @since 1.0
 */
public interface EntityTableMetadataParser {

    Optional<RDBTableMetadata> parseTableMetadata(Class<?> entityType);
}
