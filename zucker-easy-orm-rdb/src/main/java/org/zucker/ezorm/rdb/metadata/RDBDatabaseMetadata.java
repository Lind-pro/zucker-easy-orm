package org.zucker.ezorm.rdb.metadata;

import org.zucker.ezorm.core.meta.AbstractDatabaseMetadata;
import org.zucker.ezorm.rdb.metadata.dialect.Dialect;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @auther: lind
 * @since: 1.0
 */
public class RDBDatabaseMetadata extends AbstractDatabaseMetadata<RDBSchemaMetadata> {

    protected Dialect dialect;

    public RDBDatabaseMetadata(Dialect dialect) {
        this.dialect = dialect;
    }

    public Dialect getDialect() {
        return dialect;
    }

    public Optional<TableOrViewMetadata> getTableOrView(String name) {
        return this.getObject(name, RDBSchemaMetadata::getTableOrView);
    }

    public Optional<RDBTableMetadata> getTable(String name) {
        return this.getObject(name, RDBSchemaMetadata::getTable);
    }

    public Mono<TableOrViewMetadata> getTableOrViewReactive(String name) {
        return this.getObjectReactive(name, RDBSchemaMetadata::getTableOrViewReactive);
    }
}