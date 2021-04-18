package org.zucker.ezorm.rdb.metadata;

import lombok.Getter;
import lombok.Setter;
import org.zucker.ezorm.rdb.operator.builder.fragments.delete.DefaultDeleteSqlBuilder;
import org.zucker.ezorm.rdb.operator.builder.fragments.insert.BatchInsertSqlBuilder;
import org.zucker.ezorm.rdb.operator.builder.fragments.update.DefaultUpdateSqlBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @auther: lind
 * @since: 1.0
 */
@Getter
@Setter
public class RDBTableMetadata extends AbstractTableOrViewMetadata implements Cloneable {

    private String comment;

    private List<RDBIndexMetadata> indexes = new ArrayList<>();

    public RDBTableMetadata(String name) {
        this();
        setName(name);
    }

    public Optional<RDBIndexMetadata> getIndex(String indexName) {
        return indexes.stream()
                .filter(index -> index.getName().equalsIgnoreCase(indexName))
                .findFirst();
    }

    public RDBTableMetadata() {
        super();
        addFeature(BatchInsertSqlBuilder.of(this));
        addFeature(DefaultUpdateSqlBuilder.of(this));
        addFeature(DefaultDeleteSqlBuilder.of(this));
        addFeature(Defau);
    }
}
