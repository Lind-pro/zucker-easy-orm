package org.zucker.ezorm.rdb.operator.builder.fragments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @auther: lind
 * @since: 1.0
 */
@Setter
@Getter
@NoArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
public class PrepareSqlFragments implements SqlFragments {

    private List<String> sql = new ArrayList<>(64);
    private List<Object> parameters = new ArrayList<>(8);

    @Override
    public boolean isEmpty() {
        return sql.isEmpty();
    }

    public void removeLastSql() {
        if (sql.isEmpty()) {
            return;
        }
        sql.remove(sql.size() - 1);
    }

    public PrepareSqlFragments addFragments(SqlFragments fragments) {
        return addSql(fragments.getSql())
                .addParameter(fragments.getParameters());
    }

    public PrepareSqlFragments addSql(String... sql) {
        for (String _sql : sql) {
            if (null != _sql) {
                this.sql.add(_sql);
            }
        }
        return this;
    }

    public PrepareSqlFragments addSql(Collection<String> sql) {
        this.sql.addAll(sql);
        return this;
    }

    public PrepareSqlFragments addParameter(Collection<Object> parameter) {
        this.parameters.addAll(parameter);
        return this;
    }

    public PrepareSqlFragments addParameter(Object... parameter) {
        return this.addParameter(Arrays.asList(parameter));
    }

    @Override
    public String toString() {
        return toRequest().getSql();
    }


    public static PrepareSqlFragments of(String sql, Object... parameter) {
        return PrepareSqlFragments.of()
                .addSql(sql)
                .addParameter(parameter);
    }

}
