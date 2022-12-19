package org.zucker.ezorm.rdb.executor;

import java.util.List;

/**
 * @author lind
 * @since 1.0
 */
public interface BatchSqlRequest extends SqlRequest {
    List<SqlRequest> getBatch();
}
