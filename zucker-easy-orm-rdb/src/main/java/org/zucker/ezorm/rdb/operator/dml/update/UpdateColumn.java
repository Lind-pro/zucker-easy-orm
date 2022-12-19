package org.zucker.ezorm.rdb.operator.dml.update;

import lombok.Getter;
import lombok.Setter;
import org.zucker.ezorm.rdb.operator.dml.FunctionColumn;

/**
 * @author lind
 * @since 1.0
 */
@Getter
@Setter
public class UpdateColumn extends FunctionColumn {

    private Object value;
}
