package org.zucker.ezorm.rdb.metadata;

import org.zucker.ezorm.core.FeatureId;
import org.zucker.ezorm.core.FeatureType;
import org.zucker.ezorm.core.ValueCodec;
import org.zucker.ezorm.core.meta.Feature;

import java.util.Optional;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface ValueCodecFactory extends Feature {

    String ID_VALUE = "valueCodecFactory";

    FeatureId<ValueCodecFactory> ID = FeatureId.of(ID_VALUE);

    @Override
    default FeatureType getType() {
        return RDBFeatureType.codec;
    }

    @Override
    default String getName() {
        return "值编解码器";
    }

    @Override
    default String getId() {
        return ID_VALUE;
    }

    Optional<ValueCodec> createValueCodec(RDBColumnMetadata column);
}
