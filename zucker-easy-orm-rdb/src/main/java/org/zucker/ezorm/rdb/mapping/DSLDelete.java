package org.zucker.ezorm.rdb.mapping;

import org.zucker.ezorm.core.Conditional;
import org.zucker.ezorm.core.param.QueryParam;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface DSLDelete<ME extends DSLDelete> extends Conditional<ME> {

    QueryParam toQueryParam();
}
