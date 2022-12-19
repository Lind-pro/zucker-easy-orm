package org.zucker.ezorm.core.meta;

import org.zucker.ezorm.core.FeatureType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * @author lind
 * @since 1.0
 */
public interface ObjectMetadataParser extends Feature{

    @Override
    default FeatureType getType(){
        return DefaultFeatureType.metadataParser;
    }

    ObjectType getObjectType();

    Optional<? extends ObjectMetadata> parseByName(String name);

    List<? extends ObjectMetadata> parseAll();

    Mono<? extends ObjectMetadata> parseByNameReactive(String name);

    Flux<? extends ObjectMetadata> parseAllReactive();
}
