package org.zucker.ezorm.rdb.mapping.defaults;

import org.zucker.ezorm.core.NestConditional;
import org.zucker.ezorm.core.SimpleNestConditional;
import org.zucker.ezorm.core.param.QueryParam;
import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.rdb.events.ContextKeyValue;
import org.zucker.ezorm.rdb.events.ContextKeys;
import org.zucker.ezorm.rdb.mapping.DSLDelete;
import org.zucker.ezorm.rdb.mapping.events.EventResultOperator;
import org.zucker.ezorm.rdb.mapping.events.MappingContextKeys;
import org.zucker.ezorm.rdb.mapping.events.MappingEventTypes;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.dml.delete.DeleteOperator;
import org.zucker.ezorm.rdb.operator.dml.delete.DeleteResultOperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lind
 * @since 1.0
 */
public class DefaultDelete<ME extends DSLDelete<?>> implements DSLDelete<ME> {

    protected List<Term> terms = new ArrayList<>();
    protected Accepter<ME, Object> accepter = this::and;
    protected DeleteOperator operator;

    private RDBTableMetadata metadata;
    protected List<ContextKeyValue<?>> contextKeyValues = new ArrayList<>();

    public DefaultDelete(RDBTableMetadata tableMetadata, DeleteOperator operator, ContextKeyValue<?>... mapping) {
        this.operator = operator;
        this.metadata = tableMetadata;
        contextKeyValues.add(ContextKeys.source(this));
        contextKeyValues.add(MappingContextKeys.delete(operator));
        contextKeyValues.add(ContextKeys.tableMetadata(tableMetadata));
        contextKeyValues.addAll(Arrays.asList(mapping));
    }

    protected DeleteResultOperator doExecute() {
        return EventResultOperator.create(
                () -> {
                    return operator
                            .where(dsl -> terms.forEach(dsl::accept))
                            .execute();
                },
                DeleteResultOperator.class,
                metadata,
                MappingEventTypes.delete_before,
                MappingEventTypes.delete_after,
                contextKeyValues.toArray(new ContextKeyValue[0])
        );
    }

    public QueryParam toQueryParam() {
        QueryParam param = new QueryParam();
        param.setTerms(terms);
        param.setPaging(false);
        return param;
    }

    @Override
    public NestConditional<ME> nest() {
        Term term = new Term();
        term.setType(Term.Type.and);
        terms.add(term);
        return new SimpleNestConditional<>((ME) this, term);
    }

    @Override
    public NestConditional<ME> orNest() {
        Term term = new Term();
        term.setType(Term.Type.or);
        terms.add(term);
        return new SimpleNestConditional<>((ME) this, term);
    }

    @Override
    public ME and() {
        this.accepter = this::and;
        return (ME) this;
    }

    @Override
    public ME or() {
        this.accepter = this::or;
        return (ME) this;
    }

    @Override
    public ME and(String column, String termType, Object value) {
        if (value != null) {
            Term term = new Term();
            term.setColumn(column);
            term.setTermType(termType);
            term.setValue(value);
            term.setType(Term.Type.and);
            terms.add(term);
        }
        return (ME) this;
    }

    @Override
    public ME or(String column, String termType, Object value) {
        if (value != null) {
            Term term = new Term();
            term.setColumn(column);
            term.setTermType(termType);
            term.setValue(value);
            term.setType(Term.Type.or);
            terms.add(term);
        }
        return (ME) this;
    }

    @Override
    public Accepter<ME, Object> getAccepter() {
        return accepter;
    }

    @Override
    public ME accept(Term term) {
        terms.add(term);
        return (ME) this;
    }

}
