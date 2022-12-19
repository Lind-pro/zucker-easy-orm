package org.zucker.ezorm.rdb.operator.dml.update;

import lombok.Getter;
import lombok.Setter;
import org.zucker.ezorm.rdb.operator.builder.fragments.NativeSql;

/**
 * @author lind
 * @since 1.0
 */
@Getter
@Setter
public class NativeSqlUpdateColumn extends UpdateColumn implements NativeSql {
    private String sql;

    private Object[] parameters;

    public static NativeSqlUpdateColumn of(String column, String sql, Object... parameters) {
        NativeSqlUpdateColumn updateColumn = new NativeSqlUpdateColumn();
        updateColumn.setSql(sql);
        updateColumn.setColumn(column);
        updateColumn.setParameters(parameters);
        return updateColumn;
    }
}
