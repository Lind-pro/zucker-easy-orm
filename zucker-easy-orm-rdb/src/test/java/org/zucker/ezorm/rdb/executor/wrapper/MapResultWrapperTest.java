package org.zucker.ezorm.rdb.executor.wrapper;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @author lind
 * @since 1.0
 */
public class MapResultWrapperTest {

    @Test
    public void testWrapper() {

        MapResultWrapper wrapper = MapResultWrapper.defaultInstance();
        Map<String, Object> instance = wrapper.newRowInstance();

        wrapper.doWrap(instance, "id", 1);
        wrapper.doWrap(instance, "info.name", "张三");
        Assert.assertEquals(instance.get("id"), 1);
        Assert.assertTrue(instance.get("info") instanceof Map);
        Assert.assertEquals(((Map<?, ?>) instance.get("info")).get("name"), "张三");

    }
}
