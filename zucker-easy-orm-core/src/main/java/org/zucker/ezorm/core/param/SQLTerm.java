package org.zucker.ezorm.core.param;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

/**
 * 直接拼接SQL的方式
 *
 * @author lind
 * @since 1.0
 */
@Getter
@Setter
public class SQLTerm extends Term {
    private String sql;

    private SQLTerm() {
    }

    public static SQLTerm of(String sql, Object... parameters) {
        return new SQLTerm(sql, parameters);
    }

    public SQLTerm(String sql, Object... value) {
        this.sql = sql;
        setValue(value);
    }

    @Override
    @SneakyThrows
    public SQLTerm clone() {
        SQLTerm term = (SQLTerm) super.clone();
        term.setSql(getSql());
        return term;
    }
}
