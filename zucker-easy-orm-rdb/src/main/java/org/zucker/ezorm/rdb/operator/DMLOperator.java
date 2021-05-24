package org.zucker.ezorm.rdb.operator;

import org.zucker.ezorm.rdb.metadata.TableOrViewMetadata;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface DMLOperator {

    QueryOperator query(TableOrViewMetadata tableOrView);
}
