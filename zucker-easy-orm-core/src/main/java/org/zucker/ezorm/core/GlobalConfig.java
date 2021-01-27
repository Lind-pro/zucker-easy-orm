package org.zucker.ezorm.core;

/**
 * @auther: lind
 * @since: 1.0
 */
public class GlobalConfig {
    private static ObjectPropertyOperator propertyOperator;

    static {
    }

    private static ObjectConverter objectConverter;

    public static ObjectConverter getObjectConverter() {
        return objectConverter;
    }

    public static ObjectPropertyOperator getPropertyOperator() {
        return propertyOperator;
    }

    public static void setPropertyOperator(ObjectPropertyOperator propertyOperator) {
        GlobalConfig.propertyOperator = propertyOperator;
    }

    public static void setObjectConverter(ObjectConverter objectConverter) {
        GlobalConfig.objectConverter = objectConverter;
    }
}