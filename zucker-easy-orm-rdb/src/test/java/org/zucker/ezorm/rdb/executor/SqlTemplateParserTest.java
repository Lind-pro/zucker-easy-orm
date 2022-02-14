package org.zucker.ezorm.rdb.executor;

import org.junit.Assert;
import org.junit.Test;

/**
 * @auther: lind
 * @since: 1.0
 */
public class SqlTemplateParserTest {

    @Test
    public void testParse() {
        SqlRequest request = SqlTemplateParser.parse("select * from user where name = #{name} and status=1", (__) -> "1234");

        Assert.assertNotNull(request);
        Assert.assertEquals(request.getSql(), "select * from user where name = ? and status=1");
        Assert.assertArrayEquals(request.getParameters(), new Object[]{"1234"});
        Assert.assertTrue(request instanceof PrepareSqlRequest);

        Assert.assertEquals(((PrepareSqlRequest) request).toNativeSql(), "select * from user where name = '1234' and status=1");
    }
}
