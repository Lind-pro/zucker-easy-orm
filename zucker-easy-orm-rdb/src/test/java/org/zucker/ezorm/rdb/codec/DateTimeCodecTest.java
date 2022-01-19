package org.zucker.ezorm.rdb.codec;

import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @auther: lind
 * @since: 1.0
 */
public class DateTimeCodecTest {

    @Test
    public void testDecodeString() {
        DateTimeCodec codec = new DateTimeCodec("yyyy:MM:dd", String.class);

        Date date = new Date();
        Object val = codec.encode(date);
        Assert.assertEquals(date, val);
        Assert.assertEquals(codec.decode(codec.encode("2020:01:01")), "2020:01:01");
    }

    @Test
    public void testCodeList() {
        DateTimeCodec codec = new DateTimeCodec("yyyy-MM-dd", String.class);
        String str = "2022-01-19,2022-03-19";
        Object encode = codec.encode(str);
        Assert.assertTrue(encode instanceof List);

        Assert.assertEquals(str, codec.decode(encode));
    }

    @Test
    public void testsDecodeList() {
        DateTimeCodec codec = new DateTimeCodec("yyyy-MM-dd", Date.class);

        String str = "2022-01-19,2022-03-18";
        Object decode = codec.decode(str);
        Assert.assertTrue(decode instanceof List);
        Assert.assertArrayEquals(((List<?>) decode).toArray(), Arrays.stream(str.split("[,]")).map(codec::encode).toArray());
    }

    @Test
    public void testDecodeInstant() {
        DateTimeCodec codec = new DateTimeCodec("yyyy-MM-dd", Instant.class);
        Date date = new Date();
        Object value = codec.encode(date);
        Assert.assertEquals(date, value);
        Object data = codec.encode("2022-01-19");
        Assert.assertTrue(codec.decode(data) instanceof Instant);
    }

    @Test
    public void testDecodeDate() {
        DateTimeCodec codec = new DateTimeCodec("yyyy-MM-dd", Date.class);
        Date date = new Date();
        Object value = codec.encode(date);
        Assert.assertEquals(date, value);

        Object data = codec.encode("2022-02-18");
        Assert.assertEquals(codec.decode(data), data);
        Assert.assertEquals(codec.decode(((Date) data).getTime()), data);

    }
}
