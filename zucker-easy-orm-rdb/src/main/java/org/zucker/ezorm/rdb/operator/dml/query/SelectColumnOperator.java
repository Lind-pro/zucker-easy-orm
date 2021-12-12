package org.zucker.ezorm.rdb.operator.dml.query;

import org.zucker.ezorm.rdb.operator.dml.SelectColumnSupplier;

import java.util.Map;

/**
 * @auther: lind
 * @since: 1.0
 */
public class SelectColumnOperator implements SelectColumnSupplier {

    private SelectColumn column = new SelectColumn();

    public SelectColumnOperator(String name) {
        column.setColumn(name);
    }

    public SelectColumnOperator(String name, String function) {
        column.setFunction(function);
        column.setColumn(name);
    }

    public SelectColumnOperator(String name, String function, Map<String, Object> opts) {
        column.setFunction(function);
        column.setColumn(name);
        column.setOpts(opts);
    }


    @Override
    public SelectColumn get() {
        return column;
    }

    public SelectColumnOperator as(String alias) {
        column.setAlias(alias);
        return this;
    }
}
