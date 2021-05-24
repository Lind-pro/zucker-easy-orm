package org.zucker.ezorm.rdb.operator.dml;

import org.zucker.ezorm.rdb.operator.dml.query.SelectColumn;

import java.util.function.Supplier;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface SelectColumnSupplier extends Supplier<SelectColumn> {
}
