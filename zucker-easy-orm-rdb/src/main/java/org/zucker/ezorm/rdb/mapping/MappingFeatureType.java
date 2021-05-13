package org.zucker.ezorm.rdb.mapping;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.zucker.ezorm.core.FeatureType;

/**
 * @auther: lind
 * @since: 1.0
 */
@Getter
@AllArgsConstructor
public enum MappingFeatureType implements FeatureType {

    columnPropertyMapping("列与属性映射关系"),

    entityManager("实体类管理器"),

    propertyDescriptor("属性描述器");

    private String name;

    @Override
    public String getId() {
        return name();
    }

    public String createFeatureId(Class type) {
        return getId().concat(":").concat(type.getName());
    }
}
