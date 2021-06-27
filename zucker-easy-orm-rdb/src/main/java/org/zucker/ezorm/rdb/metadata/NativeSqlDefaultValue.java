package org.zucker.ezorm.rdb.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.zucker.ezorm.core.DefaultValue;
import org.zucker.ezorm.rdb.operator.builder.fragments.NativeSql;

/**
 * @auther: lind
 * @since: 1.0
 */
@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class NativeSqlDefaultValue implements DefaultValue, NativeSql {

    private String sql;

    @Override
    public String getSql() {
        return sql;
    }

    @Override
    public Object get() {
        return this;
    }
}
