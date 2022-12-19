package org.zucker.ezorm.rdb.operator;

import org.zucker.ezorm.rdb.executor.SyncSqlExecutor;
import org.zucker.ezorm.rdb.executor.reactive.ReactiveSqlExecutor;

/**
 * SQL操作接口
 *
 * @author lind
 * @since 1.0
 */
public interface SQLOperator {

    /**
     * 同步SQL执行器,通常用于JDBC模式下使用
     *
     * @return 同步SQL操作
     */
    SyncSqlExecutor sync();

    /**
     * 响应式SQL执行器,通常用于R2DBC模式下使用
     *
     * @return 响应式SQL执行器
     */
    ReactiveSqlExecutor reactive();
}
