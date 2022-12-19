package org.zucker.ezorm.rdb.mapping.wrapper;

import lombok.Getter;
import lombok.Setter;
import org.zucker.ezorm.core.GlobalConfig;
import org.zucker.ezorm.core.ObjectPropertyOperator;
import org.zucker.ezorm.rdb.executor.wrapper.ColumnWrapperContext;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrapper;
import org.zucker.ezorm.rdb.mapping.EntityColumnMapping;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author lind
 * @since 1.0
 */
public class EntityResultWrapper<E> implements ResultWrapper<E, E> {

    private final Supplier<E> entityInstanceSupplier;

    @Getter
    @Setter
    private ObjectPropertyOperator propertyOperator = GlobalConfig.getPropertyOperator();

    @Getter
    @Setter
    private EntityColumnMapping mapping;

    public EntityResultWrapper(Supplier<E> supplier) {
        this.entityInstanceSupplier = supplier;
    }

    public EntityResultWrapper(Supplier<E> supplier, EntityColumnMapping mapping) {
        this.entityInstanceSupplier = supplier;
        this.mapping = mapping;
    }

    @Override
    public E newRowInstance() {
        return entityInstanceSupplier.get();
    }

    @Override
    public void wrapColumn(ColumnWrapperContext<E> context) {
        String property = Optional.ofNullable(mapping)
                .flatMap(mapping -> mapping.getPropertyByColumnName(context.getColumnLabel()))
                .orElse(context.getColumnLabel());

        Object value = Optional.ofNullable(mapping)
                .flatMap(mapping -> mapping.getColumnByProperty(property))
                .map(columnMetadata -> columnMetadata.decode(context.getResult()))
                .orElseGet(context::getResult);
        if (value != null) {
            propertyOperator.setProperty(context.getRowInstance(), property, value);
        }
    }

    @Override
    public boolean completedWrapRow(E result) {
        return true;
    }

    @Override
    public E getResult() {
        throw new UnsupportedOperationException();
    }
}
