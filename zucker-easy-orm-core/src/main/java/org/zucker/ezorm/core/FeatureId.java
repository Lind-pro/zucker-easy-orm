package org.zucker.ezorm.core;

import org.zucker.ezorm.core.meta.Feature;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface FeatureId<T extends Feature> {

    String getId();

    static <T extends Feature> FeatureId<T> of(String id) {
        return () -> id;
    }
}
