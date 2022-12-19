package org.zucker.ezorm.rdb.operator.dml.query;

import org.zucker.ezorm.core.Conditional;
import org.zucker.ezorm.core.dsl.Query;
import org.zucker.ezorm.core.param.QueryParam;
import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.rdb.operator.dml.Join;
import org.zucker.ezorm.rdb.operator.dml.JoinType;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author lind
 * @since 1.0
 */
public class JoinOperator implements Supplier<Join> {

    private Join join = new Join();

    public JoinOperator(String target, JoinType type) {
        join.setTarget(target);
        join.setType(type);
    }

    public final JoinOperator as(String alias) {
        join.setAlias(alias);
        return this;
    }

    public final JoinOperator on(String sql) {
        return on(Wheres.sql(sql));
    }

    public final JoinOperator on(Consumer<Conditional<?>> consumer) {
        Query<?, QueryParam> query = Query.of();
        consumer.accept(query);
        join.getTerms().addAll(query.getParam().getTerms());
        return this;
    }

    public final JoinOperator on(Supplier<Term>... conditions) {
        for (Supplier<Term> condition : conditions) {
            join.getTerms().add(condition.get());
        }
        return this;
    }

    @Override
    public Join get() {
        return join;
    }
}
