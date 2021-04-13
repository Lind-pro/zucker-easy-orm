package org.zucker.ezorm.rdb.operator.builder.fragments.ddl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.zucker.ezorm.rdb.metadata.RDBIndexMetadata;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;

/**
 * @auther: lind
 * @since: 1.0
 */
@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class DropIndexParameter {
    private RDBTableMetadata table;

    private RDBIndexMetadata index;
}
