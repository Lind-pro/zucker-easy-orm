package org.zucker.ezorm.core.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 执行条件
 */
@Getter
@Setter
public class Term implements Cloneable {

    /**
     * 字段
     */
    @Schema(description = "字段名")
    private String column;

    /**
     * 值
     */
    @Schema(description = "条件值")
    private Object value;

    /**
     * 链接类型
     */
    @Schema(description = "多个条件关联类型", defaultValue = "and")
    private Type type = Type.and;

    /**
     * 条件类型
     */
    @Schema(description = "动态条件类型", defaultValue = "eq")
    private String termType = TermType.eq;

    /**
     * 自定义选项,用于在某些自定义的termType中进行自定义生产sql的规则.
     * 可通过column进行设置如: name$like$reverse$func-concat,则column为name,termType为like,options为[reverse,func-concat]
     */
    @Schema(description = "拓展选项")
    private List<String> options = new ArrayList<>();

    /**
     * 嵌套条件
     */
    @Schema(description = "嵌套条件")
    private List<Term> terms = new LinkedList<>();

    public Term or(String term, Object value) {
        return or(term, TermType.eq, value);
    }

    public Term and(String term, Object value) {
        return and(term, TermType.eq, value);
    }

    public Term or(String term, String termType, Object value) {
        Term queryTerm = new Term();
        queryTerm.setTermType(termType);
        queryTerm.setColumn(term);
        queryTerm.setValue(value);
        queryTerm.setType(Type.or);
        terms.add(queryTerm);
        return this;
    }

    public Term and(String term, String termType, Object value) {
        Term queryTerm = new Term();
        queryTerm.setTermType(termType);
        queryTerm.setColumn(term);
        queryTerm.setValue(value);
        queryTerm.setType(Type.and);
        terms.add(queryTerm);
        return this;
    }

    public Term nest() {
        return nest(null, null);
    }

    public Term orNext() {
        return orNext(null, null);
    }

    public Term nest(String term, Object value) {
        Term queryTerm = new Term();
        queryTerm.setType(Type.and);
        queryTerm.setColumn(term);
        queryTerm.setValue(value);
        terms.add(queryTerm);
        return queryTerm;
    }

    public Term orNext(String term, Object value) {
        Term queryTerm = new Term();
        queryTerm.setType(Type.or);
        queryTerm.setColumn(term);
        queryTerm.setValue(value);
        terms.add(queryTerm);
        return queryTerm;
    }

    public Term addTerm(Term term) {
        if (term == this) {
            term = term.clone();
        }
        terms.add(term);
        return this;
    }

    public void setColumn(String column) {
        if (column == null) return;
        if (column.contains("$")) {
            String[] temp = column.split("[$]");
            setTermType(temp[1]);
            column = temp[0];
            if (temp.length > 2) {
                options.addAll(Arrays.asList(temp).subList(2, temp.length));
            }
        }
        this.column = column;
    }

    public void setTermType(String termType) {
        if (termType.contains("$")) {
            String[] temp = termType.split("[$]");
            termType = temp[0];
            if (temp.length > 1) {
                options.addAll(Arrays.asList(temp).subList(1, temp.length));
            }
        }
        this.termType = termType;
    }

    @Override
    @SneakyThrows
    public Term clone() {
        Term term = ((Term) super.clone());
        term.setColumn(column);
        term.setValue(value);
        term.setTermType(termType);
        term.setType(type);
        term.setTerms(terms.stream().map(Term::clone).collect(Collectors.toList()));
        term.setOptions(new ArrayList<>(getOptions()));
        return term;
    }

    public enum Type {
        or, and
    }
}