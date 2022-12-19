package org.zucker.ezorm.rdb.supports.h2;

import lombok.AllArgsConstructor;
import org.zucker.ezorm.rdb.exception.DuplicateKeyException;
import org.zucker.ezorm.rdb.metadata.RDBSchemaMetadata;
import org.zucker.ezorm.rdb.operator.ExceptionTranslation;

import java.sql.SQLException;
import java.util.Collections;

/**
 * @author lind
 * @since 1.0
 */
@AllArgsConstructor(staticName = "of")
public class H2JDBCExceptionTranslation implements ExceptionTranslation {

    private final RDBSchemaMetadata schema;

    @Override
    public Throwable translate(Throwable e) {
        if (e instanceof SQLException) {
            SQLException exception = (SQLException) e;
            if (exception.getErrorCode() == 23505) {
                throw new DuplicateKeyException(true, Collections.emptyList(), e);
            }
        }
        return e;
    }
}
