package org.zucker.ezorm.core.param;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lind
 * @since 1.0
 */
@Getter
@Setter
@EqualsAndHashCode
public class Column {
    @Schema(description = "字段名")
    private String name;

    @Hidden
    private String type;
}
