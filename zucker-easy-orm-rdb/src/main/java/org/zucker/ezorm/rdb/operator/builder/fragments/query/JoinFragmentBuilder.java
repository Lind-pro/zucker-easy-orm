package org.zucker.ezorm.rdb.operator.builder.fragments.query;

import lombok.AllArgsConstructor;
import org.zucker.ezorm.rdb.metadata.TableOrViewMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.SqlFragments;
import org.zucker.ezorm.rdb.operator.dml.query.QueryOperatorParameter;

/**
 * @auther: lind
 * @since: 1.0
 */
@AllArgsConstructor(staticName = "of")
public class JoinFragmentBuilder implements QuerySqlFragmentBuilder{

    private TableOrViewMetadata metadata;

    @Override
    public SqlFragments createFragments(QueryOperatorParameter parameter) {
        return null;
    }
}
