package org.zucker.ezorm.rdb.supports.mysql;

import lombok.AllArgsConstructor;
import org.zucker.ezorm.rdb.exception.DuplicateKeyException;
import org.zucker.ezorm.rdb.operator.ExceptionTranslation;

import java.sql.SQLException;
import java.util.Collections;

/**
 * @author lind
 * @since 1.0
 */
@AllArgsConstructor(staticName = "of")
public class MysqlJDBCExceptionTranslation implements ExceptionTranslation {
    @Override
    public Throwable translate(Throwable e) {
        if (e instanceof SQLException) {
            SQLException exception = (SQLException) e;
            if (exception.getErrorCode() == 1062 || exception.getErrorCode() == 1022) {
                throw new DuplicateKeyException(true, Collections.emptyList(), e);
            }
        }
        return e;
    }
}
