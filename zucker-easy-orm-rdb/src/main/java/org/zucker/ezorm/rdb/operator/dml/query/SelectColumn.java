package org.zucker.ezorm.rdb.operator.dml.query;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.zucker.ezorm.rdb.operator.dml.FunctionColumn;

/**
 * @author lind
 * @since 1.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class SelectColumn extends FunctionColumn {

    private String alias;

    public static SelectColumn of(String name, String alias) {
        SelectColumn column = new SelectColumn();
        column.setColumn(name);
        column.setAlias(alias);
        return column;
    }

    public static SelectColumn of(String name) {
        SelectColumn column = new SelectColumn();
        column.setColumn(name);
        return column;
    }
}
