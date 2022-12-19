package org.zucker.ezorm.rdb.exception;

import lombok.Getter;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;

import java.util.List;

/**
 * @author lind
 * @since 1.0
 */
@Getter
public class DuplicateKeyException extends RuntimeException {

    private boolean primaryKey;

    private List<RDBColumnMetadata> columns;

    public DuplicateKeyException(boolean primaryKey, List<RDBColumnMetadata> columns, Throwable cause) {
        super(cause);
        this.primaryKey = primaryKey;
        this.columns = columns;
    }
}
