package org.zucker.ezorm.rdb.operator.dml.upsert;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.zucker.ezorm.rdb.operator.dml.insert.InsertColumn;

/**
 * @auther: lind
 * @since: 1.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class UpsertColumn extends InsertColumn {

    private boolean updateIgnore;

    public static UpsertColumn of(String column, boolean updateIgnore) {
        UpsertColumn insertColumn = new UpsertColumn();
        insertColumn.setUpdateIgnore(updateIgnore);
        insertColumn.setColumn(column);
        return insertColumn;
    }
}
