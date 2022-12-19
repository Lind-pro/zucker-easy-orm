package org.zucker.ezorm.rdb.metadata.key;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lind
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum AssociationType {
    oneToOne(false),
    manyToOne(false),
    manyToMay(true),
    oneToMay(true);

    boolean toMany;
}
