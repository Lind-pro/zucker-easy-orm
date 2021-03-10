package org.zucker.ezorm.core.param;

import org.junit.Assert;
import org.junit.Test;

/**
 * @auther: lind
 * @since: 1.0
 */
public class SortTest {
    @Test
    public void test(){
        Sort sort = new Sort();
        sort.setOrder("-- delete");
        Assert.assertEquals(sort.getOrder(),"asc");
    }
}
