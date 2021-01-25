package org.zucker.ezorm.core;

import java.util.Optional;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface ObjectPropertyOperator {

    Optional<Object> getProperty(Object object, String name);

    void setProperty(Object object, String name, Object value);
}
