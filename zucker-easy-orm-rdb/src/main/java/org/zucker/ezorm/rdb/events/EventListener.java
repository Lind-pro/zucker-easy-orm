package org.zucker.ezorm.rdb.events;

import org.zucker.ezorm.core.FeatureId;
import org.zucker.ezorm.core.FeatureType;
import org.zucker.ezorm.core.meta.DefaultFeatureType;
import org.zucker.ezorm.core.meta.Feature;


/**
 * @auther: lind
 * @since: 1.0
 */
public interface EventListener extends Feature {

    String ID_VALUE = "EventListener";

    @Override
    default String getId() {
        return ID_VALUE;
    }

    @Override
    default String getName() {
        return "事件监听器";
    }

    @Override
    default FeatureType getType() {
        return DefaultFeatureType.eventListener;
    }

    FeatureId<EventListener> ID = FeatureId.of(ID_VALUE);

    void onEvent(EventType type, EventContext context);
}
