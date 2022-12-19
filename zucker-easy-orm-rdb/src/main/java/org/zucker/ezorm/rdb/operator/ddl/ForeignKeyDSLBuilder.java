package org.zucker.ezorm.rdb.operator.ddl;

import org.zucker.ezorm.core.Conditional;
import org.zucker.ezorm.core.dsl.Query;
import org.zucker.ezorm.core.param.QueryParam;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.metadata.key.ForeignKeyBuilder;

import java.util.function.Consumer;

/**
 * @author lind
 * @since 1.0
 */
public class ForeignKeyDSLBuilder {

    private final TableBuilder tableBuilder;
    private final RDBTableMetadata table;
    protected final ForeignKeyBuilder builder = ForeignKeyBuilder.builder().build();

    public ForeignKeyDSLBuilder(TableBuilder tableBuilder, RDBTableMetadata table) {
        this.table = table;
        this.tableBuilder = tableBuilder;
        this.builder.setAutoJoin(true);
    }

    public ForeignKeyDSLBuilder name(String name) {
        builder.setName(name);
        return this;
    }

    public ForeignKeyDSLBuilder alias(String alias) {
        builder.setAlias(alias);
        return this;
    }

    public ForeignKeyDSLBuilder target(String source) {
        builder.setTarget(source);
        return this;
    }

    public ForeignKeyDSLBuilder column(String sourceColumn, String targetColumn) {
        builder.addColumn(sourceColumn, targetColumn);
        return this;
    }

    public ForeignKeyDSLBuilder autoJoin(boolean autoJoin) {
        builder.setAutoJoin(autoJoin);
        return this;
    }

    public ForeignKeyDSLBuilder toMany() {
        builder.setAutoJoin(false);
        builder.setToMany(true);
        return this;
    }

    public ForeignKeyDSLBuilder condition(Consumer<Conditional<?>> consumer) {
        Query<?, QueryParam> query = Query.of();
        consumer.accept(query);
        builder.setTerms(query.getParam().getTerms());
        return this;
    }

    public TableBuilder commit() {
        table.addForeignKey(builder);
        return tableBuilder;
    }
}
