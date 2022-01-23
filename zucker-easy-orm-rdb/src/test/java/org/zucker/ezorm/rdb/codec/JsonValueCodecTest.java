package org.zucker.ezorm.rdb.codec;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @auther: lind
 * @since: 1.0
 */
public class JsonValueCodecTest {

    @Test
    public void testList() {
        JsonValueCodec codec = JsonValueCodec.ofCollection(List.class, String.class);
        Object arr = codec.decode("[\"1\",\"2\",\"3\"]");

        Assert.assertTrue(arr instanceof List);

        Assert.assertEquals(((List<?>) arr).size(), 3);
    }

    @Test
    public void testByteBuffer() {
        JsonValueCodec codec = JsonValueCodec.ofCollection(Set.class, String.class);

        Object arr = codec.decode(ByteBuffer.wrap("[\"1\",\"2\"]".getBytes()));

        Assert.assertTrue(arr instanceof Set);

        Assert.assertEquals(((Set<?>) arr).size(), 2);
    }

    @Test
    public void testSet() {
        JsonValueCodec codec = JsonValueCodec.ofCollection(Set.class, String.class);
        Object arr = codec.decode("[\"1\",\"2\"]");

        Assert.assertTrue(arr instanceof Set);
        Assert.assertEquals(((Set<?>) arr).size(), 2);
    }

    @Test
    public void testMap() {
        JsonValueCodec codec = JsonValueCodec.ofMap(Map.class, String.class, Integer.class);

        Object arr = codec.decode("{\"a\":1,\"b\":2}");

        Assert.assertTrue(arr instanceof Map);
        Map<String, Integer> val = (Map<String, Integer>) arr;
        Assert.assertEquals(val.size(), 2);
        Assert.assertEquals(val.get("a"), Integer.valueOf(1));
        Assert.assertEquals(val.get("b"), Integer.valueOf(2));
    }

    //TODO
}
