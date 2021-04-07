package org.zucker.ezorm.rdb.operator.builder.fragments.term;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.zucker.ezorm.core.param.SQLTerm;
import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.operator.builder.FragmentBlock;
import org.zucker.ezorm.rdb.operator.builder.fragments.*;
import org.zucker.ezorm.rdb.utils.PropertiesUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther: lind
 * @since: 1.0
 */
@SuppressWarnings("all")
public abstract class AbstractTermFragmentBuilder<T> {

    @Getter
    @Setter
    private boolean useBlock = false;

    protected BlockSqlFragments createBlockFragments(T parameter, List<Term> terms) {
        BlockSqlFragments fragments = BlockSqlFragments.of();
        int index = 0;
        boolean termAvailable;
        boolean lastTermAvailable = false;
        for (Term term : terms) {
            index++;
            SqlFragments termFragments;
            if (term instanceof SQLTerm) {
                termFragments = PrepareSqlFragments.of()
                        .addSql(((SQLTerm) term).getSql())
                        .addParameter(PropertiesUtils.convertList(term.getValue()));
            } else {
                termFragments = term.getValue() == null ? EmptySqlFragments.INSTANCE : createTermFragments(parameter, term);
            }

            termAvailable = termFragments.isNotEmpty();
            if (termAvailable) {
                BlockSqlFragments termBlock = BlockSqlFragments.of();
                if (index != 1 && lastTermAvailable) {
                    termBlock.addBlock(FragmentBlock.before, term.getType().name());
                }
                termBlock.addBlock(FragmentBlock.term, termFragments);
                fragments.addBlock(FragmentBlock.term, termBlock);
                lastTermAvailable = termAvailable;
            }
            BlockSqlFragments nestBlock = BlockSqlFragments.of();
            List<Term> nest = term.getTerms();

            //嵌套条件
            if (nest != null && !nest.isEmpty()) {
                // 递归
                SqlFragments nestFragments = createTermFragments(parameter, nest);
                if (nestFragments.isNotEmpty()) {
                    // and or
                    if (termAvailable || lastTermAvailable) {
                        nestBlock.addBlock(FragmentBlock.before, term.getType().name());
                    }
                    nestBlock.addBlock(FragmentBlock.before, "(");
                    nestBlock.addBlock(FragmentBlock.term, nestFragments);
                    nestBlock.addBlock(FragmentBlock.after, ")");

                    fragments.addBlock(FragmentBlock.term, nestBlock);
                    lastTermAvailable = true;
                    continue;
                }
            }
        }
        return fragments;
    }


    private PrepareSqlFragments createPrepareFragments(T parameter, List<Term> terms) {
        PrepareSqlFragments fragments = PrepareSqlFragments.of();
        int index = 0;
        boolean termAvailable;
        boolean lastTermAvailable = false;
        for (Term term : terms) {
            index++;
            SqlFragments termFragments;
            if (term instanceof SQLTerm) {
                termFragments = PrepareSqlFragments.of()
                        .addSql(((SQLTerm) term).getSql())
                        .addParameter(PropertiesUtils.convertList(term.getValue()));
            } else {
                termFragments = term.getValue() == null ? EmptySqlFragments.INSTANCE : createTermFragments(parameter, term);
            }
            termAvailable = termFragments.isNotEmpty();
            if (termAvailable) {
                if (index != 1 && lastTermAvailable) {
                    // and or
                    fragments.addSql(term.getType().name());
                }
                fragments.addFragments(termFragments);
                lastTermAvailable = termAvailable;
            }
            List<Term> nest = term.getTerms();
            // 嵌套条件
            if (nest != null && !nest.isEmpty()) {
                // 递归...
                SqlFragments nestFragments = createTermFragments(parameter, nest);
                if (nestFragments.isNotEmpty()) {
                    // and or
                    if (termAvailable || lastTermAvailable) {
                        fragments.addSql(term.getType().name());
                    }
                    fragments.addSql("(");
                    fragments.addFragments(nestFragments);
                    fragments.addSql(")");
                    lastTermAvailable = true;
                    continue;
                }
            }
        }
        return fragments;
    }

    protected SqlFragments createTermFragments(T parameter, List<Term> terms) {
        return isUseBlock() ? createBlockFragments(parameter, terms) : createPrepareFragments(parameter, terms);
    }

    protected abstract SqlFragments createTermFragments(T parameter, Term term);
}
