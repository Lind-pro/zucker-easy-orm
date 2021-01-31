package org.zucker.ezorm.core.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther: lind
 * @since: 1.0
 */

@Getter
@AllArgsConstructor
public enum DefaultObjectType implements ObjectType {
    database("数据库"),
    schema("schema");

    private String name;

    @Override
    public String getId() {
        return name();
    }
}
