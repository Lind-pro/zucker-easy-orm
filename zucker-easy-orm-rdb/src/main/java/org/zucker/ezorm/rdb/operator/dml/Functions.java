package org.zucker.ezorm.rdb.operator.dml;

import org.zucker.ezorm.rdb.metadata.RDBFeatureType;
import org.zucker.ezorm.rdb.metadata.RDBFeatures;

/**
 * @auther: lind
 * @since: 1.0
 */
public class Functions {

    static FunctionColumnOperator count(String column){
        return new FunctionColumnOperator(RDBFeatures.count.getFunction(), column);
    }

    static FunctionColumnOperator sum(String column){
        return new FunctionColumnOperator(RDBFeatures.sum.getFunction(), column);
    }
}
