package org.zucker.ezorm.rdb.operator.dml.delete;

import lombok.Getter;
import org.zucker.ezorm.core.Conditional;
import org.zucker.ezorm.core.dsl.Query;
import org.zucker.ezorm.core.param.QueryParam;
import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.rdb.executor.SqlRequest;

import java.util.function.Consumer;

/**
 * @auther: lind
 * @since: 1.0
 */
@Getter
public class BuildParameterDeleteOperator extends DeleteOperator {

    private DeleteOperatorParameter parameter = new DeleteOperatorParameter();

    @Override
    public DeleteOperator where(Consumer<Conditional<?>> dsl) {
        Query<?, QueryParam> query = Query.of();
        dsl.accept(query);
        parameter.getWhere().addAll(query.getParam().getTerms());
        return this;
    }

    @Override
    public DeleteOperator where(Term term) {
        parameter.getWhere().add(term);
        return this;
    }

    @Override
    public SqlRequest getSql() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DeleteResultOperator execute() {
        throw new UnsupportedOperationException();
    }
}
