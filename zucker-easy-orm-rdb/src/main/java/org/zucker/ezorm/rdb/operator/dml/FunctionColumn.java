package org.zucker.ezorm.rdb.operator.dml;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @auther: lind
 * @since: 1.0
 */
@Getter
@Setter
@EqualsAndHashCode
public class FunctionColumn {

    private String column;

    private String function;

    private Map<String, Object> opts;

    @Override
    public String toString() {
        return function + "(" + column + ")";
    }
}
