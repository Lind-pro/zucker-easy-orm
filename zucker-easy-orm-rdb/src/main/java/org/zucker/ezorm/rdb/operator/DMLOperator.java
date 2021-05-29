package org.zucker.ezorm.rdb.operator;

import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.metadata.TableOrViewMetadata;
import org.zucker.ezorm.rdb.operator.dml.QueryOperator;
import org.zucker.ezorm.rdb.operator.dml.delete.DeleteOperator;
import org.zucker.ezorm.rdb.operator.dml.insert.InsertOperator;
import org.zucker.ezorm.rdb.operator.dml.update.UpdateOperator;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface DMLOperator {

    QueryOperator query(TableOrViewMetadata tableOrView);

    DeleteOperator delete(RDBTableMetadata table);

    UpdateOperator update(RDBTableMetadata table);

    InsertOperator insert(RDBTableMetadata table);
}
