package org.zucker.ezorm.core.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.zucker.ezorm.core.FeatureType;

/**
 * @author lind
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum DefaultFeatureType implements FeatureType {
    metadataParser("元数据解析器"),
    eventListener("事件监听器"),
    defaultValueGenerator("默认值生成器");

    private final String name;

    @Override
    public String getId() {
        return name();
    }

}
