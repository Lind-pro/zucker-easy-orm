package org.zucker.ezorm.rdb.operator.dml.update;

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
public class UpdateOperatorParameter {

    List<UpdateColumn> columns = new ArrayList<>();

    List<Term> where = new ArrayList<>();

}
