package org.zucker.ezorm.rdb.operator.dml.query;

import lombok.Getter;
import lombok.Setter;
import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.rdb.operator.dml.FunctionColumn;
import org.zucker.ezorm.rdb.operator.dml.FunctionTerm;
import org.zucker.ezorm.rdb.operator.dml.Join;

import java.util.*;

/**
 * @auther: lind
 * @since: 1.0
 */
@Getter
@Setter
public class QueryOperatorParameter {

    private List<SelectColumn> select = new ArrayList<>();

    private Set<String> selectExcludes = new HashSet<>();

    private String from;

    private String fromAlias;

    private List<Term> where = new ArrayList<>();

    private List<Join> joins = new ArrayList<>();

    private List<SortOrder> orderBy = new ArrayList<>();

    private List<FunctionColumn> groupBy = new ArrayList<>();

    private List<FunctionTerm> having = new ArrayList<>();

    private Integer pageIndex;

    private Integer pageSize;

    private Boolean forUpdate;

    public Optional<Join> findJoin(String targetName) {
        return Optional.ofNullable(joins)
                .flatMap(_joins -> _joins
                        .stream().filter(join -> join.equalsTargetOrAlias(targetName))
                        .findFirst());
    }

    public String getFormAlias() {
        if (fromAlias == null) {
            return from;
        }
        return fromAlias;
    }
}
