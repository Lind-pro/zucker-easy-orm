package org.zucker.ezorm.core.meta;

import org.zucker.ezorm.core.FeatureType;

/**
 * @author lind
 * @since 1.0
 */
public interface Feature {

    String getId();

    String getName();

    FeatureType getType();
}
