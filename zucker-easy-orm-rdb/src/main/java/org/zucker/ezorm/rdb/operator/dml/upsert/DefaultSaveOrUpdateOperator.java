package org.zucker.ezorm.rdb.operator.dml.upsert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.zucker.ezorm.rdb.executor.SqlRequest;
import org.zucker.ezorm.rdb.mapping.defaults.SaveResult;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;

import java.util.List;
import java.util.function.Supplier;

/**
 * @auther: lind
 * @since: 1.0
 */
public class DefaultSaveOrUpdateOperator implements SaveResultOperator {

    private RDBTableMetadata table;

    private RDBColumnMetadata idColumn;

    private volatile boolean idColumnParsed;

    public static DefaultSaveOrUpdateOperator of(RDBTableMetadata table) {
        return new DefaultSaveOrUpdateOperator(table);
    }

    public DefaultSaveOrUpdateOperator(RDBTableMetadata table) {
        this.table = table;
    }

    protected RDBColumnMetadata getIdColumn() {
        if (idColumnParsed) {
            return idColumn;
        }
        if (idColumn == null) {
            this.idColumn = table.getColumns()
                    .stream()
                    .filter(RDBColumnMetadata::isPrimaryKey)
                    .findFirst()
                    .orElse(null);
            idColumnParsed = true;
        }
        return idColumn;
    }

    @Getter
    @AllArgsConstructor
    protected static class UpdateOrInsert {
        SqlRequest updateSql;
        Supplier<SqlRequest> insertSql;
    }

    public SaveResultOperator execute(UpsertOperatorParameter parameter){
        return new DefaultSaveOrUpdateOperator(()->createUp)
    }

    @AllArgsConstructor
    static class Upsert{
        protected List<SqlRequest>  insert;
        protected List<UpdateOrInsert> upserts;
    }

    @AllArgsConstructor
    protected class DefaultSaveResultOperator implements SaveResultOperator{
        Supplier<Upsert> supplier;

        @Override
        public SaveResult sync() {
            Sy
            return null;
        }
    }
}
