package org.zucker.ezorm.rdb.operator.dml;

import org.zucker.ezorm.core.Conditional;
import org.zucker.ezorm.core.LogicalOperation;
import org.zucker.ezorm.core.MethodReferenceColumn;
import org.zucker.ezorm.core.StaticMethodReferenceColumn;
import org.zucker.ezorm.core.param.QueryParam;
import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.rdb.operator.dml.query.JoinOperator;
import org.zucker.ezorm.rdb.operator.dml.query.Joins;
import org.zucker.ezorm.rdb.operator.dml.query.SelectColumn;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @auther: lind
 * @since: 1.0
 * <p>
 * database
 * .dml()
 * .query()
 * .select(count("id","total")
 * .from("user")
 * .where("dsl->dsl.is("name","1")
 * .execute()
 * .reactive(map())
 * .subscribe(data->)
 */
public abstract class QueryOperator implements LogicalOperation<QueryOperator> {

    public abstract QueryOperator select(String... columns);

    public abstract QueryOperator select(Collection<String> columns);

    public abstract QueryOperator select(SelectColumn... column);

    public QueryOperator select(SelectColumnSupplier... operators) {
        for (SelectColumnSupplier operator : operators) {
            select(operator.get());
        }
        return this;
    }

    @SafeVarargs
    public final <T> QueryOperator select(MethodReferenceColumn<T>... columns) {
        return select(Arrays.stream(columns)
                .map(MethodReferenceColumn::getColumn)
                .toArray(String[]::new));
    }

    @SafeVarargs
    public final <T> QueryOperator select(StaticMethodReferenceColumn<T>... columns) {
        return select(Arrays.stream(columns)
                .map(StaticMethodReferenceColumn::getColumn)
                .toArray(String[]::new));
    }

    public abstract QueryOperator selectExcludes(Collection<String> columns);

    public QueryOperator selectExcludes(String... columns) {
        return selectExcludes(Arrays.asList(columns));
    }

    @SafeVarargs
    public final <T> QueryOperator selectedExcludes(StaticMethodReferenceColumn<T>... columns) {
        return selectExcludes(Arrays.stream(columns)
                .map(StaticMethodReferenceColumn::getColumn)
                .collect(Collectors.toSet()));
    }

    public abstract QueryOperator where(Consumer<Conditional<?>> conditionalConsumer);

    public abstract QueryOperator where(Term term);

    public abstract QueryOperator setParam(QueryParam param);

    public abstract QueryOperator where(Collection<Term> term);

    public QueryOperator where(TermSupplier... condition) {
        for (TermSupplier operator : condition) {
            where(operator.get());
        }
        return this;
    }

    public abstract QueryOperator join(Join... on);

    @SafeVarargs
    public final QueryOperator join(Supplier<Join>... on) {
        for (Supplier<Join> joinOperator : on) {
            join(joinOperator.get());
        }
        return this;
    }

    public final QueryOperator leftJoin(String target, Consumer<JoinOperator> joinOperatorConsumer) {
        JoinOperator operator = Joins.left(target);
        joinOperatorConsumer.accept(operator);
        return join(operator.get());
    }
}
