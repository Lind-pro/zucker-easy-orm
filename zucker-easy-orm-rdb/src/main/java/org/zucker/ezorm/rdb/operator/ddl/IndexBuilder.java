package org.zucker.ezorm.rdb.operator.ddl;

import org.zucker.ezorm.rdb.metadata.RDBIndexMetadata;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;

/**
 * @auther: lind
 * @since: 1.0
 */
public class IndexBuilder {

    private TableBuilder tableBuilder;

    private RDBTableMetadata table;

    protected final RDBIndexMetadata index = new RDBIndexMetadata();

    public IndexBuilder(TableBuilder tableBuilder, RDBTableMetadata table) {
        this.tableBuilder = tableBuilder;
        this.table = table;
    }

    public IndexBuilder name(String indexName) {
        index.setName(indexName);
        return this;
    }

    public IndexBuilder unique() {
        index.setUnique(true);
        return this;
    }

    public IndexBuilder column(String column) {
        return column(column, RDBIndexMetadata.IndexSort.asc);
    }

    public IndexBuilder column(String column, String sort) {
        return column(column, RDBIndexMetadata.IndexSort.valueOf(sort));
    }

    public IndexBuilder column(String column, RDBIndexMetadata.IndexSort sort) {
        index.getColumns().add(RDBIndexMetadata.IndexColumn.of(column, sort));
        return this;
    }

    public TableBuilder commit() {
        table.addIndex(index);
        return tableBuilder;
    }
}
