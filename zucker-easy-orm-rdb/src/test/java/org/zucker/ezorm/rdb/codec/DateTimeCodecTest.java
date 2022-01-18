package org.zucker.ezorm.rdb.codec;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

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


    //TODO
}
