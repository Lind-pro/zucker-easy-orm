package org.zucker.ezorm.rdb.operator.dml.query;

import org.zucker.ezorm.rdb.operator.dml.FunctionColumn;
import org.zucker.ezorm.rdb.operator.dml.Functions;
import org.zucker.ezorm.rdb.operator.dml.SortOrderSupplier;

import java.util.function.Supplier;

/**
 * @author lind
 * @since 1.0
 */
public interface Orders {

    static OrderOperator function(Supplier<FunctionColumn> column) {
        return new OrderOperator(column.get());
    }

    static OrderOperator count(String name) {
        return function(Functions.count(name));
    }

    static SortOrderSupplier asc(String column) {
        return new OrderOperator(column).asc();
    }

    static SortOrderSupplier desc(String column) {
        return new OrderOperator(column).desc();
    }
}
