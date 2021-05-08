package org.zucker.ezorm.rdb.metadata.parser;

import org.zucker.ezorm.core.meta.ObjectMetadataParser;
import org.zucker.ezorm.core.meta.ObjectType;
import org.zucker.ezorm.rdb.metadata.RDBObjectType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface TableMetadataParser extends ObjectMetadataParser {

    String id = "tableMetadataParser";

    @Override
    default String getId() {
        return id;
    }

    @Override
    default String getName() {
        return "表结构解析器";
    }

    @Override
    default ObjectType getObjectType() {
        return RDBObjectType.table;
    }

    List<String> parseAllTableName();

    Flux<String> parseAllTableNameReactive();

    boolean tableExists(String name);

    Mono<Boolean> tableExistsReactive(String name);
}
