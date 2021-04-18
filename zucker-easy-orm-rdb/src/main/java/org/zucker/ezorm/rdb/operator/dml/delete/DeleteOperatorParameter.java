package org.zucker.ezorm.rdb.operator.dml.delete;

import lombok.Getter;
import lombok.Setter;
import org.zucker.ezorm.core.param.Term;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther: lind
 * @since: 1.0
 */
@Getter
@Setter
public class DeleteOperatorParameter {

    private List<Term> where = new ArrayList<>();
}
