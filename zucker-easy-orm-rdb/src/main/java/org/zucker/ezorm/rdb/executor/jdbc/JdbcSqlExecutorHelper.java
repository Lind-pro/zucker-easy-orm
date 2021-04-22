package org.zucker.ezorm.rdb.executor.jdbc;

import lombok.SneakyThrows;
import org.zucker.ezorm.rdb.executor.NullValue;

import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @auther: lind
 * @since: 1.0
 */
public class JdbcSqlExecutorHelper {

    @SneakyThrows
    public static List<String> getResultColumns(ResultSetMetaData metaData) {
        int count = metaData.getColumnCount();
        // 获取到执行SQL 后返回的列信息
        List<String> columns = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            columns.add(metaData.getColumnLabel(i));
        }
        return columns;
    }

    protected static void preparedStatementParameter(PreparedStatement statement,Object[] parameter) throws SQLException {
        if(parameter==null||parameter.length==0){
            return;
        }
        int index=1;
        for (Object object:parameter){
            if(object==null){
                statement.setNull(index++, Types.NULL);
            }else if(object instanceof NullValue){
                statement.setNull(index++,((NullValue) object).getDataType().getSqlType().getVendorTypeNumber());
            }else if(object instanceof Date){
                statement.setTimestamp(index++,new java.sql.Timestamp(((Date) object).getTime()));
            }else if(object instanceof byte[]){
                statement.setBlob(index++,new ByteArrayInputStream((byte[]) object));
            }else {
                statement.setObject(index++,object);
            }
        }
    }
}
