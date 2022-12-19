package org.zucker.ezorm.rdb.operator.dml.query;

import org.zucker.ezorm.core.param.SQLTerm;
import org.zucker.ezorm.core.param.Term;

import java.util.function.Supplier;

/**
 * @author lind
 * @since 1.0
 */
public interface Wheres {

    static Supplier<Term> sql(String sql) {
        SQLTerm term = new SQLTerm(sql);
        return () -> term;
    }
}
