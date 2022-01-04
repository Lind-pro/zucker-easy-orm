package org.zucker.ezorm.rdb.supports;

import lombok.*;
import org.zucker.ezorm.rdb.mapping.annotation.ColumnType;
import org.zucker.ezorm.rdb.mapping.annotation.DefaultValue;
import org.zucker.ezorm.rdb.mapping.annotation.EnumCodec;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.JDBCType;
import java.util.Date;
import java.util.List;

/**
 * @auther: lind
 * @since: 1.0
 */
@Table(name = "entity_test_table", indexes = @Index(name = "test_index", columnList = "name,state desc"))
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "createTime")
public class BasicTestEntity implements Serializable {

    @Column(length = 32)
    @Id
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "create_time", updatable = false)
    private Date createTime;

    @Column(nullable = false)
    private Byte state;

    @Column
    private Double doubleValue;

    @Column(scale = 1)
    private BigDecimal bigDecimal;

    @Column
    private Long balance;

    @Column(table = "entity_test_table_detail")
    private String detail;

    @Column
    @ColumnType(javaType = String.class)
    @DefaultValue("disabled")
    private List<String> tags;

    @Column
    @ColumnType(javaType = String.class)
    @DefaultValue("disabled")
    private StateEnum stateEnum;

    @Column
    @ColumnType(javaType = Long.class, jdbcType = JDBCType.BIGINT)
    @EnumCodec(toMask = true)
    @DefaultValue("0")
    private StateEnum[] stateEnums;

    @Column
    private String aTest;

    @Column(name = "address_id")
    private String addressId;

    @JoinColumn(name = "address_id")
    private Address address;

}
