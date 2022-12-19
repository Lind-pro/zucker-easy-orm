package org.zucker.ezorm.rdb.supports.h2;

import org.zucker.ezorm.rdb.operator.builder.FragmentBlock;
import org.zucker.ezorm.rdb.operator.builder.Paginator;
import org.zucker.ezorm.rdb.operator.builder.fragments.BlockSqlFragments;
import org.zucker.ezorm.rdb.operator.builder.fragments.PrepareSqlFragments;
import org.zucker.ezorm.rdb.operator.builder.fragments.SqlFragments;

/**
 * @author lind
 * @since 1.0
 */
public class H2Paginator implements Paginator {
    @Override
    public SqlFragments doPaging(SqlFragments fragments, int pageIndex, int pageSize) {
        if (fragments instanceof PrepareSqlFragments) {
            ((PrepareSqlFragments) fragments).addSql("limit ? offset ?")
                    .addParameter(pageSize, pageIndex * pageSize);
        } else if (fragments instanceof BlockSqlFragments) {
            ((BlockSqlFragments) fragments).addBlock(FragmentBlock.after, PrepareSqlFragments.of()
                    .addSql("limit ? offset ?")
                    .addParameter(pageSize, pageIndex * pageSize));
        }
        return fragments;
    }
}
