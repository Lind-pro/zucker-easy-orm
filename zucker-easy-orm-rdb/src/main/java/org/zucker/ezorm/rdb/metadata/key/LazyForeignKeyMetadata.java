package org.zucker.ezorm.rdb.metadata.key;

import org.zucker.ezorm.core.meta.ObjectMetadata;
import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.rdb.metadata.TableOrViewMetadata;
import org.zucker.ezorm.rdb.operator.dml.JoinType;

import java.util.List;
import java.util.Optional;

/**
 * @author lind
 * @since 1.0
 */
public class LazyForeignKeyMetadata implements ForeignKeyMetadata {

    private ForeignKeyBuilder builder;

    private TableOrViewMetadata mainTable;

    private List<ForeignKeyMetadata> preKey;

    private List<ForeignKeyColumn> columns;

    public static LazyForeignKeyMetadata of(ForeignKeyBuilder builder, TableOrViewMetadata mainTable) {
        LazyForeignKeyMetadata metadata = new LazyForeignKeyMetadata();
        metadata.builder = builder;
        metadata.mainTable = mainTable;
        return metadata;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public ObjectMetadata clone() {
        return null;
    }

    @Override
    public boolean isLogical() {
        return true;
    }

    @Override
    public boolean isToMany() {
        return getType().isToMany();
    }

    @Override
    public AssociationType getType() {
        return Optional.ofNullable(builder)
                .map(ForeignKeyBuilder::getAssociationType)
                .orElse(AssociationType.oneToOne);
    }

    @Override
    public TableOrViewMetadata getSource() {
        return mainTable;
    }

    @Override
    public TableOrViewMetadata getTarget() {
        return mainTable.getSchema()
                .getTableOrView(builder.getTarget())
                .orElseThrow(() -> new IllegalArgumentException("target [" + builder.getTarget() + "] doesn't exist"));
    }

    @Override
    public List<ForeignKeyColumn> getColumns() {
        return null;
    }

    @Override
    public boolean isAutoJoin() {
        return false;
    }

    @Override
    public JoinType getJoinType() {
        return null;
    }

    @Override
    public List<Term> getTerms() {
        return null;
    }

    @Override
    public Optional<ForeignKeyMetadata> getMiddleForeignKey(String name) {
        return Optional.empty();
    }

    @Override
    public List<ForeignKeyMetadata> getMiddleForeignKeys() {
        return null;
    }
}
