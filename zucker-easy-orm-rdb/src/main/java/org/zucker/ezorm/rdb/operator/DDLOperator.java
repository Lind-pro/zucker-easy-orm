package org.zucker.ezorm.rdb.operator;

import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.ddl.TableBuilder;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface DDLOperator {

    TableBuilder createOrAlter(String name);

    TableBuilder createOrAlter(RDBTableMetadata newTable);
}
