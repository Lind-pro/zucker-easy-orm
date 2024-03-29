package org.zucker.ezorm.rdb.metadata;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.zucker.ezorm.core.CastUtil;
import org.zucker.ezorm.core.meta.ObjectType;
import org.zucker.ezorm.rdb.metadata.key.ForeignKeyMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.delete.DefaultDeleteSqlBuilder;
import org.zucker.ezorm.rdb.operator.builder.fragments.insert.BatchInsertSqlBuilder;
import org.zucker.ezorm.rdb.operator.builder.fragments.update.DefaultUpdateSqlBuilder;
import org.zucker.ezorm.rdb.operator.dml.upsert.DefaultSaveOrUpdateOperator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author lind
 * @since 1.0
 */
@Getter
@Setter
public class RDBTableMetadata extends AbstractTableOrViewMetadata implements Cloneable {

    private String comment;

    private List<RDBIndexMetadata> indexes = new ArrayList<>();

    private List<ConstraintMetadata> constraints = new ArrayList<>();

    public RDBTableMetadata(String name) {
        this();
        setName(name);
    }

    public RDBTableMetadata() {
        super();
        addFeature(BatchInsertSqlBuilder.of(this));
        addFeature(DefaultUpdateSqlBuilder.of(this));
        addFeature(DefaultDeleteSqlBuilder.of(this));
        addFeature(DefaultSaveOrUpdateOperator.of(this));
    }

    public Optional<ConstraintMetadata> getConstraint(String name) {
        return constraints
                .stream()
                .filter(metadata -> metadata.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public Optional<RDBIndexMetadata> getIndex(String indexName) {
        return indexes.stream()
                .filter(index -> index.getName().equalsIgnoreCase(indexName))
                .findFirst();
    }

    public void addConstraint(ConstraintMetadata metadata) {
        Objects.requireNonNull(metadata.getName(), "Constraint name can not be null");
        metadata.setTableName(this.getName());
        constraints.add(metadata);
    }


    public void addIndex(RDBIndexMetadata index) {
        Objects.requireNonNull(index.getName(), "index name can not be null");
        index.setTableName(getName());
        indexes.add(index);

        for (RDBIndexMetadata.IndexColumn column : index.getColumns()) {
            getColumn(column.getColumn())
                    .ifPresent(columnMetadata -> {
                        if (index.isPrimaryKey()) {
                            columnMetadata.setPrimaryKey(true);
                        }
                    });
        }
    }

    @Override
    public ObjectType getObjectType() {
        return RDBObjectType.table;
    }

    @Override
    @SneakyThrows
    public RDBTableMetadata clone() {
        RDBTableMetadata clone = (RDBTableMetadata) super.clone();
        clone.setAllColumns(new ConcurrentHashMap<>());

        this.getColumns()
                .stream()
                .map(RDBColumnMetadata::clone)
                .forEach(clone::addColumn);

        clone.setFeatures(new HashMap<>(getFeatures()));
        clone.setIndexes(getIndexes()
                .stream()
                .map(RDBIndexMetadata::clone)
                .collect(Collectors.toList()));

        this.getForeignKey()
                .stream()
                .map(ForeignKeyMetadata::clone)
                .map(CastUtil::<ForeignKeyMetadata>cast)
                .forEach(clone::addForeignKey);

        return clone;
    }

    @Override
    public void merge(TableOrViewMetadata metadata) {
        super.merge(metadata);
    }
}
