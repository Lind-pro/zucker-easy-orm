package org.zucker.ezorm.rdb.operator.builder.fragments.ddl;

import lombok.*;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;

/**
 * @auther: lind
 * @since: 1.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlterRequest {

    private RDBTableMetadata newTable;

    private RDBTableMetadata oldTable;

    private boolean allowDrop;

    private boolean allowAlter = true;

    private boolean allowIndexAlter = true;
}
