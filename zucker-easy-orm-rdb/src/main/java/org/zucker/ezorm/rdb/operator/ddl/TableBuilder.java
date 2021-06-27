package org.zucker.ezorm.rdb.operator.ddl;

import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;

import java.util.function.Consumer;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface TableBuilder {

    TableBuilder addColumn(RDBColumnMetadata column);

    TableBuilder custom(Consumer<RDBTableMetadata> consumer);

    ColumnBuilder addColumn();

    ColumnBuilder addColumn(String name);

    TableBuilder removeColumn(String name);

    TableBuilder dropColumn(String name);

    TableBuilder comment(String comment);

    TableBuilder alias(String name);

    TableBuilder allowAlter(boolean allow);

    TableBuilder autoLoad(boolean autoLoad);

    TableBuilder merge(boolean merge);

    IndexBuilder index();

    ForeignKeyDSLBuilder foreignKey();

    TableDDLResultOperator commit();
}
