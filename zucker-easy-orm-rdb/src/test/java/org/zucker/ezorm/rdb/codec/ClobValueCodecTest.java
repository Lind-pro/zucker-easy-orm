package org.zucker.ezorm.rdb.codec;

import io.r2dbc.spi.Clob;
import org.junit.Assert;
import org.junit.Test;
import reactor.core.publisher.Mono;

/**
 * @author lind
 * @since 1.0
 */
public class ClobValueCodecTest {

    @Test
    public void test() {
        ClobValueCodec codec = new ClobValueCodec();

        Assert.assertEquals(codec.decode(codec.encode("test")), "test");
        Assert.assertEquals(codec.decode(1), 1);
    }

    @Test
    public void testR2dbc() {
        ClobValueCodec codec = new ClobValueCodec();
        Assert.assertEquals(codec.decode(Clob.from(Mono.just("test"))), "test");
    }
}
