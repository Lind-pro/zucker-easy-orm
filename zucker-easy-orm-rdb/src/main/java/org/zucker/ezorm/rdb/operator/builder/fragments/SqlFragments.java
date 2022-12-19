package org.zucker.ezorm.rdb.operator.builder.fragments;

import org.zucker.ezorm.rdb.executor.EmptySqlRequest;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.executor.SqlRequests;

import java.util.Collections;
import java.util.List;

/**
 * @author lind
 * @since 1.0
 */
public interface SqlFragments {

    boolean isEmpty();

    default boolean isNotEmpty() {
        return !isEmpty();
    }

    List<String> getSql();

    List<Object> getParameters();

    default SqlRequest toRequest() {
        if (isEmpty()) {
            return EmptySqlRequest.INSTANCE;
        }
        return SqlRequests.prepare(String.join(" ", getSql()), getParameters().toArray());
    }

    static SqlFragments single(String sql){
        return new SqlFragments() {
            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public List<String> getSql() {
                return Collections.singletonList(sql);
            }

            @Override
            public List<Object> getParameters() {
                return Collections.emptyList();
            }

            @Override
            public String toString() {
                return sql;
            }
        };
    }
}
