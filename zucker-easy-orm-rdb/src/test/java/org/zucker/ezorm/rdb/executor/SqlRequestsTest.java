package org.zucker.ezorm.rdb.executor;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

public class SqlRequestsTest {

    @Test
    public void templateTest() {
        SqlRequest request = SqlRequests.of("select * from order where id = #{id}", Collections.singletonMap("id", "o_id"));
        Assert.assertEquals(request.getSql(), "select * from order where id = ?");
        Assert.assertArrayEquals(request.getParameters(), new Object[]{"o_id"});
    }
}
