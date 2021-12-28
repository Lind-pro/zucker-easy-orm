package org.zucker.ezorm.rdb.operator.builder.fragments.term;

import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.PrepareSqlFragments;
import org.zucker.ezorm.rdb.operator.builder.fragments.SqlFragments;

/**
 * @auther: lind
 * @since: 1.0
 */
public class EmptyTermFragmentBuilder extends AbstractTermFragmentBuilder {

    private final String symbol;

    public EmptyTermFragmentBuilder(String termType, String name, boolean not) {
        super(termType, name);
        symbol = not ? "!=" : "=";
    }

    @Override
    public PrepareSqlFragments createFragments(String columnFullName, RDBColumnMetadata column, Term term) {
        return PrepareSqlFragments.of().addSql(columnFullName, symbol, "''");
    }

}
