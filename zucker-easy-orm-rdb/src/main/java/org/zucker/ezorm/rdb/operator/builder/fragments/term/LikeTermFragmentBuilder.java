package org.zucker.ezorm.rdb.operator.builder.fragments.term;

import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.core.param.TermType;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.PrepareSqlFragments;
import org.zucker.ezorm.rdb.operator.builder.fragments.SqlFragments;

/**
 * @auther: lind
 * @since: 1.0
 */
public class LikeTermFragmentBuilder extends AbstractTermFragmentBuilder {

    private final boolean not;

    public LikeTermFragmentBuilder(boolean not) {
        super(not ? TermType.nLike : TermType.like, not ? "Not Like" : "Like");
        this.not = not;
    }

    @Override
    public SqlFragments createFragments(String columnFullName, RDBColumnMetadata column, Term term) {
        boolean reversal = term.getOptions().contains("reversal");
        boolean startWith = term.getOptions().contains("startWith");
        boolean endWith = term.getOptions().contains("endWith");

        PrepareSqlFragments fragments = PrepareSqlFragments.of();
        if (reversal) {
            fragments.addSql("?").addParameter(term.getValue());
        } else {
            fragments.addSql(columnFullName);
        }
        if (not) {
            fragments.addSql("not");
        }
        fragments.addSql("like");

        if (reversal) {
            if (startWith) {
                fragments.addSql("contact( '%',");
            } else {
                fragments.addSql("contact(");
            }
            fragments.addSql(columnFullName);
            if (endWith) {
                fragments.addSql(",'%' )");
            } else {
                fragments.addSql(")");
            }
        } else {
            fragments.addSql("?").addParameter(term.getValue());
        }
        return fragments;
    }
}
