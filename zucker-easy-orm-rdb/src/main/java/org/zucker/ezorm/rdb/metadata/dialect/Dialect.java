package org.zucker.ezorm.rdb.metadata.dialect;

import org.hswebframework.utils.StringUtils;
import org.zucker.ezorm.core.meta.Feature;
import org.zucker.ezorm.rdb.metadata.DataType;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.metadata.RDBFeatureType;
import org.zucker.ezorm.rdb.supports.h2.H2Dialect;
import org.zucker.ezorm.rdb.supports.mysql.MysqlDialect;

import java.sql.SQLType;
import java.util.Optional;

/**
 * 数据库方言
 * @see DefaultDialect
 *
 * @auther: lind
 * @since: 1.0
 */
public interface Dialect extends Feature {

    @Override
    default RDBFeatureType getType() {
        return RDBFeatureType.dialect;
    }

    void addDataTypeBuilder(String typeId, DataTypeBuilder mapper);

    String buildColumnDataType(RDBColumnMetadata columnMetadata);

    String getQuoteStart();

    String getQuoteEnd();

    String clearQuote(String string);

    boolean isColumnToUpperCase();

    Optional<SQLType> convertSqlType(Class<?> type);

    DataType convertDataType(String dataType);

    default String quote(String keyword, boolean changeCase) {
        if (keyword.startsWith(getQuoteStart()) && keyword.endsWith(getQuoteEnd())) {
            return keyword;
        }
        return getQuoteStart().concat(isColumnToUpperCase() && changeCase ? keyword.toUpperCase() : keyword).concat(getQuoteEnd());
    }

    default String quote(String keyword) {
        return quote(keyword, true);
    }

    default String buildColumnFullName(String tableName, String columnName) {
        if (columnName.contains(".")) {
            return columnName;
        }
        if (StringUtils.isNullOrEmpty(tableName)) {
            return StringUtils.concat(getQuoteStart(), isColumnToUpperCase() ? columnName.toUpperCase() : columnName, getQuoteEnd());
        }
        return StringUtils.concat(tableName, ".", getQuoteStart(), isColumnToUpperCase() ? columnName.toUpperCase() : columnName, getQuoteEnd());
    }
    Dialect MYSQL = new MysqlDialect();
    Dialect H2 = new H2Dialect();

}
