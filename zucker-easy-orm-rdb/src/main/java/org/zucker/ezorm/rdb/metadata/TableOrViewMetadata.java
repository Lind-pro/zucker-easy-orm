package org.zucker.ezorm.rdb.metadata;

import org.zucker.ezorm.core.FeatureId;
import org.zucker.ezorm.core.meta.Feature;
import org.zucker.ezorm.core.meta.FeatureSupportedMetadata;
import org.zucker.ezorm.core.meta.ObjectMetadata;
import org.zucker.ezorm.core.meta.ObjectType;
import org.zucker.ezorm.rdb.events.*;
import org.zucker.ezorm.rdb.metadata.dialect.Dialect;
import org.zucker.ezorm.rdb.metadata.key.ForeignKeyBuilder;
import org.zucker.ezorm.rdb.metadata.key.ForeignKeyMetadata;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * DQL 对象元数据：表或者视图
 *
 * @auther: lind
 * @since: 1.0
 */
public interface TableOrViewMetadata extends ObjectMetadata, FeatureSupportedMetadata {

    @Override
    ObjectType getObjectType();

    /**
     * 当前数据库方言
     */
    Dialect getDialect();

    /**
     * @return 元数据所在schema
     */
    RDBSchemaMetadata getSchema();

    /**
     * @return 获取当前表或者视图的所有列
     */
    List<RDBColumnMetadata> getColumns();

    /**
     * @return 获取所有列，并且包含关联表队列
     */
    List<RDBColumnMetadata> findColumns();

    /**
     * 获取当前表或者试图队列
     *
     * @param name 列名或者别名
     * @return
     */
    Optional<RDBColumnMetadata> getColumn(String name);

    /**
     * 查找列，可查找通过外键关联表队列或者其他表队列
     *
     * @param name
     * @return
     */
    Optional<RDBColumnMetadata> findColumn(String name);

    /**
     * 获取全部外键
     *
     * @return 全部外键集合
     */
    List<ForeignKeyMetadata> getForeignKeys();

    /**
     * 根据关联表获取外键
     *
     * @param targetName 关联表名
     * @return Optional
     */
    Optional<ForeignKeyMetadata> getForeignKey(String targetName);

    /**
     * 添加外键元数据
     *
     * @param metadata ForeignKeyMetadata
     */
    void addForeignKey(ForeignKeyMetadata metadata);

    /**
     * 使用builder 添加外键元数据
     *
     * @param builder Builder
     * @return 添加后的元数据
     */
    ForeignKeyMetadata addForeignKey(ForeignKeyBuilder builder);

    /**
     * 触发事件
     *
     * @param eventType 事件类型
     * @param keyValues 事件上下文键值内容
     */
    default void fireEvent(EventType eventType, ContextKeyValue<?>... keyValues) {
        fireEvent(eventType, ctx -> ctx.set(keyValues));
    }

    default void fireEvent(EventType eventType, Consumer<EventContext> contextConsumer) {
        this.findFeature(EventListener.ID)
                .ifPresent(eventListener -> {
                    EventContext context = EventContext.create();
                    context.set(ContextKeys.table, this);
                    contextConsumer.accept(context);
                    eventListener.onEvent(eventType, context);
                });
    }

    default List<Feature> findFeatures(Predicate<Feature> predicate) {
        return Stream.concat(getSchema().getFeatureList().stream(), getFeatureList().stream())
                .filter(predicate)
                .collect(Collectors.toList());
    }

    default List<Feature> findFeatures() {
        return findFeatures((feature -> true));
    }

    default String getFullName() {
        return getSchema().getName().concat(".").concat(getName());
    }

    default <T extends Feature> Optional<T> findFeature(FeatureId<T> id) {
        return findFeature(id.getId());
    }

    default <T extends Feature> Optional<T> findFeature(String id) {
        return Optional.of(this.<T>getFeature(id))
                .filter(Optional::isPresent)
                .orElseGet(() -> getSchema().findFeature(id));
    }

    void merge(TableOrViewMetadata metadata);

    void replace(TableOrViewMetadata metadata);
}
