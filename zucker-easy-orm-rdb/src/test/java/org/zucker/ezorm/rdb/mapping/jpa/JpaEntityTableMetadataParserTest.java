package org.zucker.ezorm.rdb.mapping.jpa;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.Assert;
import org.junit.Test;
import org.zucker.ezorm.rdb.mapping.annotation.ColumnType;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.metadata.RDBDatabaseMetadata;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.metadata.dialect.Dialect;
import org.zucker.ezorm.rdb.supports.h2.H2SchemaMetadata;

import javax.persistence.*;
import java.sql.JDBCType;
import java.util.Date;
import java.util.List;

/**
 * @author lind
 * @since 1.0
 */
public class JpaEntityTableMetadataParserTest {

    @Test
    public void test() {
        RDBDatabaseMetadata database = new RDBDatabaseMetadata(Dialect.H2);
        H2SchemaMetadata schema = new H2SchemaMetadata("PUBLIC");
        database.setCurrentSchema(schema);
        database.addSchema(schema);

        JpaEntityTableMetadataParser parser = new JpaEntityTableMetadataParser();
        parser.setDatabaseMetadata(database);

        RDBTableMetadata table = parser.parseTableMetadata(User.class).orElseThrow(IllegalAccessError::new);

        Assert.assertEquals(table.getName(), "entity_test");

        RDBColumnMetadata id = table.getColumn("id").orElseThrow(NullPointerException::new);

        Assert.assertTrue(id.isPrimaryKey());
        Assert.assertEquals(id.getJavaType(), String.class);
        Assert.assertEquals(id.getLength(), 32);

        Assert.assertEquals(table.getColumn("create_time").orElseThrow(NullPointerException::new).getAlias(), "createTime");

        Assert.assertEquals(table.getColumn("aTest").orElseThrow(NullPointerException::new).getAlias(), "aTest");

    }

    public interface InterfaceEntity<ID> {
        ID getId();
    }

    @Getter
    @Setter
    public static class GenericEntity<ID> implements InterfaceEntity<ID> {
        @Id
        @Column(length = 32)
        private ID id;

        @ColumnType(typeId = "jsonb", jdbcType = JDBCType.VARCHAR)
        private List<String> jsonArray;

    }

    @Table(name = "address")
    @Getter
    @Setter
    public class Address {
        @Column
        @Id
        private String id;

        @Column
        private String name;
    }

    @Getter
    @Setter
    @Table(name = "entity_test",
            indexes = @Index(
                    name = "test_index",
                    columnList = "name asc,state desc"
            ))
    @ToString
    @EqualsAndHashCode(callSuper = true)
    public class User extends GenericEntity<String> {

        @Column
        private String name;

        @Column
        private Byte state;

        @Column(name = "create_time")
        private Date createTime;

        @Column(table = "entity_detail", name = "name")
        private String infoName;

        @Column
        private String addressId;

        @Column
        private String aTest;

        @JoinColumn(name = "address_id")
        private Address address;
    }

}
