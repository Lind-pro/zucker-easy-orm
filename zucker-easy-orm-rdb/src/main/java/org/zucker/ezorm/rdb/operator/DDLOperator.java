package org.zucker.ezorm.rdb.operator;

import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.operator.ddl.TableBuilder;

/**
 * 数据库DDL操作
 * <pre>
 *     {@code
 *            operator.ddl()
 *                 .createOrAlter("test_ddl_create")
 *                 .addColumn().name("id").varchar(32).primaryKey().comment("ID").commit()
 *                 .addColumn().name("name").varchar(64).notNull().comment("名称").commit()
 *                 .addColumn().name("comment").columnDef("varchar(32) not null default '1'").commit()
 *                 .commit()
 *                 .sync();
 *     }
 * </pre>
 *
 * @author lind
 * @since 1.0
 */
public interface DDLOperator {

    /**
     * 根据表名创建或修改表: 表不存在则创建，存在则修改
     *
     * @param name 表名
     * @return TableBuilder
     * @see org.zucker.ezorm.rdb.operator.builder.fragments.ddl.CreateTableSqlBuilder
     * @see org.zucker.ezorm.rdb.operator.builder.fragments.ddl.AlterTableSqlBuilder
     * @see TableBuilder
     */
    TableBuilder createOrAlter(String name);

    /**
     * 根据表结构创建或者修改表:表不存在则创建,存在则修改
     *
     * @param newTable 表结构
     * @return TableBuilder
     * @see org.zucker.ezorm.rdb.operator.builder.fragments.ddl.CreateTableSqlBuilder
     * @see org.zucker.ezorm.rdb.operator.builder.fragments.ddl.AlterTableSqlBuilder
     * @see TableBuilder
     */
    TableBuilder createOrAlter(RDBTableMetadata newTable);
}
