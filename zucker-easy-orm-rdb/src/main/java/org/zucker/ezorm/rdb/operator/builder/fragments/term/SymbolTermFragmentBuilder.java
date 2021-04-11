package org.zucker.ezorm.rdb.operator.builder.fragments.term;

import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.PrepareSqlFragments;
import org.zucker.ezorm.rdb.operator.builder.fragments.SqlFragments;

/**
 * @auther: lind
 * @since: 1.0
 */
public class SymbolTermFragmentBuilder extends AbstractTermFragmentBuilder {

    private String symbol;

    public SymbolTermFragmentBuilder(String termType, String name, String symbol) {
        super(termType, name);
        this.symbol = symbol;
    }

    @Override
    public SqlFragments createFragments(String columnFullName, RDBColumnMetadata column, Term term) {
        return PrepareSqlFragments.of()
                .addSql(columnFullName, symbol, "?")
                .addParameter(convertValue(column, term));
    }
}
