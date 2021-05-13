package org.zucker.ezorm.rdb.mapping.defaults;

import lombok.Getter;
import lombok.Setter;
import org.zucker.ezorm.core.GlobalConfig;
import org.zucker.ezorm.core.ObjectPropertyOperator;
import org.zucker.ezorm.rdb.events.ContextKeyValue;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrapper;
import org.zucker.ezorm.rdb.mapping.EntityColumnMapping;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.DatabaseOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


/**
 * @auther: lind
 * @since: 1.0
 */
public abstract class DefaultRepository<E> {

    protected DatabaseOperator operator;

    protected ResultWrapper<E,?> wrapper;

    private volatile String idColumn;

    protected EntityColumnMapping mapping;

    @Getter
    protected volatile String[] properties;

    protected Supplier<RDBTableMetadata> tableSupplier;

    protected final List<ContextKeyValue> defaultContextKeyValue = new ArrayList<>();

    @Getter
    @Setter
    private ObjectPropertyOperator propertyOperator = GlobalConfig.getPropertyOperator();

    public DefaultRepository(DatabaseOperator operator,Supplier<RDBTableMetadata> supplier,ResultWrapper<E,?> wrapper){
        this.operator =operator;
        this.wrapper =wrapper;
        this.tableSupplier= supplier;
//        defaultContextKeyValue.add(repo)
    }
}
