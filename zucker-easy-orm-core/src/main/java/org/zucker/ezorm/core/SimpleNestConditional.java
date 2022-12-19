package org.zucker.ezorm.core;

import org.zucker.ezorm.core.param.Term;

/**
 * @author lind
 * @since 1.0
 */
public class SimpleNestConditional<T extends TermTypeConditionalSuppport> implements NestConditional<T> {

    private Term term;

    private T target;

    private Accepter<NestConditional<T>,Object> accepter = this::and;

    public SimpleNestConditional(T target,Term term){
        this.term= term;
        this.target= target;
    }

    @Override
    public T end() {
        return target;
    }

    @Override
    public NestConditional<NestConditional<T>> nest() {
        return new SimpleNestConditional<>(this,this.term.nest());
    }

    @Override
    public NestConditional<NestConditional<T>> nest(String column, Object value) {
        return new SimpleNestConditional<>(this,this.term.nest(column,value));
    }

    @Override
    public NestConditional<NestConditional<T>> orNest() {
        return new SimpleNestConditional<>(this,this.term.orNext());
    }

    @Override
    public NestConditional<NestConditional<T>> orNest(String column, Object value) {
        return new SimpleNestConditional<>(this,this.term.orNext(column,value));
    }

    @Override
    public NestConditional<T> and() {
        accepter=this::and;
        return this;
    }

    @Override
    public NestConditional<T> or() {
        accepter=this::or;
        return this;
    }

    @Override
    public NestConditional<T> and(String column, String termType, Object value) {
        term.and(column,termType,value);
        return this;
    }

    @Override
    public NestConditional<T> or(String column, String termType, Object value) {
        term.and(column,termType,value);
        return this;
    }

    @Override
    public Accepter<NestConditional<T>, Object> getAccepter() {
        return accepter;
    }

    @Override
    public NestConditional<T> accept(Term term) {
        this.term.addTerm(term);
        return this;
    }
}
