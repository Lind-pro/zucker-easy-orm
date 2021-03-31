package org.zucker.ezorm.rdb.operator.dml.insert;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.zucker.ezorm.rdb.operator.dml.FunctionColumn;

/**
 * @auther: lind
 * @since: 1.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class InsertColumn extends FunctionColumn {

    public static InsertColumn of(String column) {
        InsertColumn insertColumn = new InsertColumn();
        insertColumn.setColumn(column);
        return insertColumn;
    }
}
