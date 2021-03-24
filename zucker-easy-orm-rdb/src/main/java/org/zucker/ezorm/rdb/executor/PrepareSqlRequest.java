package org.zucker.ezorm.rdb.executor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zucker.ezorm.rdb.utils.SqlUtils;

import java.util.Arrays;

/**
 * @auther: lind
 * @since: 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class PrepareSqlRequest implements SqlRequest {

    private String sql;

    private Object[] parameters;

    @Override
    public boolean isEmpty() {
        return sql == null || sql.isEmpty();
    }

    public String toNativeSql() {
        return SqlUtils.toNativeSql(sql, parameters);
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "empty sql";
        }
        return toNativeSql();
    }

}
