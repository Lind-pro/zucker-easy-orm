package org.zucker.ezorm.rdb.operator.dml.update;

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
public class BuildParameterUpdateOperator extends UpdateOperator {

    private UpdateOperatorParameter parameter = new UpdateOperatorParameter();

    @Override
    public UpdateOperator set(String column, Object value) {
        UpdateColumn updateColumn = new UpdateColumn();
        updateColumn.setColumn(column);
        updateColumn.setValue(value);
        parameter.getColumns().add(updateColumn);
        return this;
    }

    @Override
    public UpdateOperator set(Object entity) {
        //TODO
        return this;
    }

    @Override
    public UpdateOperator set(UpdateColumn column) {
        parameter.getColumns().add(column);
        return this;
    }

    @Override
    public UpdateOperator where(Consumer<Conditional<?>> dsl) {
        Query<?, QueryParam> query = Query.of();
        dsl.accept(query);
        parameter.getWhere().addAll(query.getParam().getTerms());
        return this;
    }

    @Override
    public UpdateOperator where(Term term) {
        parameter.getWhere().add(term);
        return this;
    }

    @Override
    public SqlRequest getSql() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UpdateResultOperator execute() {
        throw new UnsupportedOperationException();
    }
}
