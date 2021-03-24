package org.zucker.ezorm.rdb.operator.dml;

import lombok.Getter;
import lombok.Setter;
import org.zucker.ezorm.core.param.Term;

import java.util.Map;

/**
 * @auther: lind
 * @since: 1.0
 */
@Getter
@Setter
public class FunctionTerm extends Term {

    private String function;

    private Map<String, String> opts;
}
