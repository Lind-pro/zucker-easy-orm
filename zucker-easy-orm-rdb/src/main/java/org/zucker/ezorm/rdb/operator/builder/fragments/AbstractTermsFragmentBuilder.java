package org.zucker.ezorm.rdb.operator.builder.fragments;

import lombok.Getter;
import lombok.Setter;
import org.zucker.ezorm.core.param.SQLTerm;
import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.rdb.operator.builder.FragmentBlock;
import org.zucker.ezorm.rdb.utils.PropertiesUtils;

import java.util.List;

/**
 * 抽象查询条件构造器,用于将{@link Term}构造为SQL的where条件，支持嵌套条件.
 *
 * <pre>{@code
 * {column:"id","value":"data-id"} => where id = ?
 *
 * {column:"id","value":"data-id",terms:[{column:"name",value:"test"}]} => where id = ? and (name = ?)
 * }</pre>
 *
 * @param <T> 参数类型
 * @author lind
 * @since 1.0
 */
@SuppressWarnings("all")
public abstract class AbstractTermsFragmentBuilder<T> {

    @Getter
    @Setter
    private boolean useBlock = false;

    /**
     * 构造{@link BlockSqlFragments},通常用于分页的场景,可以获取到SQL的每一个组成部分.
     *
     * @param parameter parameter
     * @param terms     terms
     * @return BlockSqlFragments
     * @see BlockSqlFragments
     */
    private BlockSqlFragments createBlockFragments(T parameter, List<Term> terms) {
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


    /**
     * 构造{@link PrepareSqlFragments}
     *
     * @param parameter parameter
     * @param terms     terms
     * @return PrepareSqlFraments
     * @see PrepareSqlFragments
     */
    private PrepareSqlFragments createPrepareFragments(T parameter, List<Term> terms) {
        PrepareSqlFragments fragments = PrepareSqlFragments.of();

        int index = 0;
        boolean termAvailable;
        boolean lastTermAvailable = false;
        for (Term term : terms) {
            index++;
            SqlFragments termFragments;
            // 原生SQL
            if (term instanceof SQLTerm) {
                termFragments = PrepareSqlFragments
                        .of()
                        .addSql(((SQLTerm) term).getSql())
                        .addParameter(PropertiesUtils.convertList(term.getValue()));
            } else {
                // 值为null 时忽略条件
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

    /**
     * 构造单个条件的SQL 片段,方法无需处理{@link Term#getTerms()}
     * <p>
     * 如果{@link Term#getValue()}为{@code null}，此方法不会被调用。
     * </p>
     *
     * @param parameter 参数
     * @param term      条件
     * @return SqlFragments
     */
    protected abstract SqlFragments createTermFragments(T parameter, Term term);
}
