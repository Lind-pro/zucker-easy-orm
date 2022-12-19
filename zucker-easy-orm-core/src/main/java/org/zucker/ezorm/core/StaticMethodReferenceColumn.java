package org.zucker.ezorm.core;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author lind
 * @since 1.0
 *
 * 使用静态方法引用来定义列名，如
 * <pre>
 *     createQuery().where(UserEntity:getName,"name").single();
 * </pre>
 */
public interface StaticMethodReferenceColumn<T> extends Function<T,Object>, Serializable {

    default String getColumn(){
        return MethodReferenceConverter.convertToColumn(this);
    }
}
