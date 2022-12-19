package org.zucker.ezorm.rdb.metadata;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.zucker.ezorm.core.meta.ObjectMetadata;
import org.zucker.ezorm.core.meta.ObjectType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lind
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
public class RDBIndexMetadata implements ObjectMetadata {

    private String name;

    private String tableName;

    private String alias;

    private String comment;

    private List<IndexColumn> columns = new CopyOnWriteArrayList<>();

    private boolean unique;

    private boolean primaryKey;

    public RDBIndexMetadata(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(name)
                .append(" ")
                .append(unique ? "unique index" : "index")
                .append(" on ")
                .append(tableName);
        builder.append("(");
        int index = 0;
        for (IndexColumn column : columns) {
            if (index++ != 0) {
                builder.append(",");
            }
            builder.append(column.getColumn())
                    .append(" ")
                    .append(column.getSort().name());
        }
        builder.append(")");
        return builder.toString();
    }

    public boolean contains(String column) {
        return columns.stream().anyMatch(indexColumn -> indexColumn.getColumn().equals(column));
    }

    @Override
    public ObjectType getObjectType() {
        return RDBObjectType.index;
    }

    @Override
    @SneakyThrows
    public RDBIndexMetadata clone() {
        RDBIndexMetadata metadata = (RDBIndexMetadata) super.clone();

        metadata.columns.clear();
        columns.stream()
                .map(IndexColumn::clone)
                .forEach(metadata.columns::add);
        return metadata;
    }

    public boolean isChanged(RDBIndexMetadata old) {
        if (old.getColumns().size() != getColumns().size()) {
            return true;
        }
        Map<String, IndexColumn> nameMapping = getColumns()
                .stream()
                .collect(Collectors.toMap(IndexColumn::getColumn, Function.identity()));

        for (IndexColumn oldColumn : old.getColumns()) {
            IndexColumn column = nameMapping.get(oldColumn.column);
            if (column == null) {
                return true;
            }
        }
        return false;
    }

    public enum IndexSort {
        asc, desc
    }

    @Getter
    @Setter
    public static class IndexColumn implements Cloneable, Comparable<IndexColumn> {
        private String column;

        private IndexSort sort = IndexSort.asc;

        private int sortIndex;

        public static IndexColumn of(String column, IndexSort sort) {
            IndexColumn indexColumn = new IndexColumn();
            indexColumn.setColumn(column);
            indexColumn.setSort(sort);
            return indexColumn;
        }

        @Override
        @SneakyThrows
        protected IndexColumn clone() {
            return (IndexColumn) super.clone();
        }

        @Override
        public int compareTo(IndexColumn o) {
            return Integer.compare(sortIndex, o.sortIndex);
        }
    }
}
