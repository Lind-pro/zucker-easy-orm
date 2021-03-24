package org.zucker.ezorm.rdb.operator.builder;

import lombok.AllArgsConstructor;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.metadata.RDBSchemaMetadata;
import org.zucker.ezorm.rdb.metadata.TableOrViewMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.SqlFragments;
import org.zucker.ezorm.rdb.operator.dml.query.QueryOperatorParameter;
import org.zucker.ezorm.rdb.operator.dml.query.QuerySqlBuilder;

import java.util.Optional;

/**
 * @auther: lind
 * @since: 1.0
 */
@AllArgsConstructor(staticName = "of")
public class DefaultQuerySqlBuilder implements QuerySqlBuilder {

    protected RDBSchemaMetadata schema;

    protected Optional<SqlFragments> select(QueryOperatorParameter parameter, TableOrViewMetadata metadata){
        return metadata.getFeature(select)
    }
    @Override
    public SqlRequest build(QueryOperatorParameter parameter) {
        return null;
    }
}
