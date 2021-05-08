package org.zucker.ezorm.rdb.metadata.parser;

import org.zucker.ezorm.core.meta.ObjectMetadata;
import org.zucker.ezorm.core.meta.ObjectMetadataParser;
import org.zucker.ezorm.core.meta.ObjectType;
import org.zucker.ezorm.rdb.metadata.RDBIndexMetadata;
import org.zucker.ezorm.rdb.metadata.RDBObjectType;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface IndexMetadataParser extends ObjectMetadataParser {

    String id = "indexMetadataParser";

    @Override
   default String getId(){
        return id;
    }

    @Override
   default String getName(){
        return "索引解析器";
    }

    @Override
   default ObjectType getObjectType(){
        return RDBObjectType.index;
    }

    List<RDBIndexMetadata> parseTableIndex(String tableName);

    @Override
    List<RDBIndexMetadata> parseAll();

    Flux<RDBIndexMetadata> parseTableIndexReactive(String tableName);

    @Override
    Flux<RDBIndexMetadata> parseAllReactive();
}
