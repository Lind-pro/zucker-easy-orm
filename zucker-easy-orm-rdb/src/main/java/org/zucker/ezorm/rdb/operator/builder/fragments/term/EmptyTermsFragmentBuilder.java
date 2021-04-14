package org.zucker.ezorm.rdb.operator.builder.fragments.term;

import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.AbstractTermsFragmentBuilder;
import org.zucker.ezorm.rdb.operator.builder.fragments.PrepareSqlFragments;
import org.zucker.ezorm.rdb.operator.builder.fragments.SqlFragments;

/**
 * @auther: lind
 * @since: 1.0
 */
public class EmptyTermsFragmentBuilder extends AbstractTermsFragmentBuilder {

    private String symbol;

    public EmptyTermsFragmentBuilder(String termType, String name, boolean isNot) {
        super(termType, name);
        symbol = isNot ? "=" : "!=";
    }

    @Override
    public SqlFragments createFragments(String columnFullName, RDBColumnMetadata column, Term term) {
        return PrepareSqlFragments.of().addSql(columnFullName, symbol, "''");
    }
}
