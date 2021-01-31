package org.zucker.ezorm.core.meta;

/**
 * 元数据对象类型，如：表，视图。
 * 请尽量使用枚举实现此接口.
 *
 * @auther: lind
 * @since: 1.0
 * @see DefaultObjectType
 */
public interface ObjectType {

    String getId();

    String getName();
}
