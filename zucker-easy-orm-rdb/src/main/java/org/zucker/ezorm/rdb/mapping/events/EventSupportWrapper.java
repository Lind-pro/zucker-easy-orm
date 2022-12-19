package org.zucker.ezorm.rdb.mapping.events;

import lombok.AllArgsConstructor;
import org.zucker.ezorm.rdb.events.ContextKeyValue;
import org.zucker.ezorm.rdb.events.ContextKeys;
import org.zucker.ezorm.rdb.executor.wrapper.ColumnWrapperContext;
import org.zucker.ezorm.rdb.executor.wrapper.ResultWrapper;
import org.zucker.ezorm.rdb.metadata.TableOrViewMetadata;

/**
 * @author lind
 * @since 1.0
 */
@AllArgsConstructor
public class EventSupportWrapper<E, R> implements ResultWrapper<E, R> {

    private TableOrViewMetadata metadata;

    private ResultWrapper<E, R> wrapper;

    private ContextKeyValue<?>[] defaultKeyValues;

    public static <E, R> EventSupportWrapper<E, R> eventWrapper(TableOrViewMetadata metadata,
                                                                ResultWrapper<E, R> wrapper,
                                                                ContextKeyValue<?>... contextKeyValues) {
        return new EventSupportWrapper<>(metadata, wrapper, contextKeyValues);
    }

    @Override
    public E newRowInstance() {
        return wrapper.newRowInstance();
    }

    @Override
    public void wrapColumn(ColumnWrapperContext<E> context) {
        wrapper.wrapColumn(context);
        metadata.fireEvent(MappingEventTypes.select_wrapper_column, ctx -> ctx.set(MappingContextKeys.columnWrapperContext(context), ContextKeys.tableMetadata(metadata)).set(defaultKeyValues));
    }

    @Override
    public boolean completedWrapRow(E result) {
        boolean val = wrapper.completedWrapRow(result);
        metadata.fireEvent(MappingEventTypes.select_wrapper_done, ctx -> ctx.set(MappingContextKeys.instance(result), ContextKeys.tableMetadata(metadata)).set(defaultKeyValues));
        return val;
    }

    @Override
    public R getResult() {
        return wrapper.getResult();
    }
}
