package org.zucker.ezorm.core;

import org.zucker.ezorm.core.param.Term;

/**
 * @author lind
 * @since 1.0
 */
public interface TermTypeConditionalSuppport {

    /**
     * 条件接收器，用于处理接收到的条件并进行对应的操作如{@link Term.Type#and}
     *
     * @param <T>
     * @param <O>
     */
    interface Accepter<T, O> {
        T accept(String column, String termType, O value);
    }

    interface SimpleAccepter<T, O> {
        T accept(String column, O value);
    }
}
