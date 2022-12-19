package org.zucker.ezorm.rdb.executor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author lind
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmptySqlRequest implements SqlRequest {

    public static final EmptySqlRequest INSTANCE = new EmptySqlRequest();

    @Override
    public String getSql() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] getParameters() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public String toString() {
        return "empty sql";
    }
}
