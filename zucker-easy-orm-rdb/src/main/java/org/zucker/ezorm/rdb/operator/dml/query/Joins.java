package org.zucker.ezorm.rdb.operator.dml.query;

import org.zucker.ezorm.rdb.operator.dml.Join;
import org.zucker.ezorm.rdb.operator.dml.JoinType;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface Joins {
    static JoinOperator left(String target) {
        return new JoinOperator(target, JoinType.left);
    }

    static JoinOperator inner(String target) {
        return new JoinOperator(target, JoinType.inner);
    }

    static JoinOperator right(String target) {
        return new JoinOperator(target, JoinType.right);
    }

    static JoinOperator full(String target) {
        return new JoinOperator(target, JoinType.full);
    }
}
