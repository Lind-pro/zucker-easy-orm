package org.zucker.ezorm.rdb.operator.dml;

import org.zucker.ezorm.rdb.operator.dml.query.SortOrder;

import java.util.function.Supplier;

/**
 * @author lind
 * @since 1.0
 */
public interface SortOrderSupplier extends Supplier<SortOrder> {
}
