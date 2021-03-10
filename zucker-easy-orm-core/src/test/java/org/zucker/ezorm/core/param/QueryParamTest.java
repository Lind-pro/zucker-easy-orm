package org.zucker.ezorm.core.param;

import org.junit.Assert;
import org.junit.Test;

/**
 * @auther: lind
 * @since: 1.0
 */
public class QueryParamTest {

    @Test
    public void testPage(){
        QueryParam param = new QueryParam();

        param.doPaging(2,10);

        System.out.println(55/10);
        param.rePaging(15);

        Assert.assertEquals(param.getPageIndex(),1);
    }

    @Test
    public void testCustomFirstPage(){
        QueryParam param = new QueryParam();

        param.setFirstPageIndex(1);
        param.doPaging(1,10);

        Assert.assertEquals(param.getPageIndex(),0);
        Assert.assertEquals(param.getThinkPageIndex(),1);

        param.doPaging(2,10);

        Assert.assertEquals(param.getPageIndex(),1);

        Assert.assertEquals(param.getThinkPageIndex(),2);
    }
}
