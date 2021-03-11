package org.zucker.ezorm.rdb.codec;

import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;
import reactor.core.publisher.Mono;

import javax.sql.rowset.serial.SerialBlob;
import java.io.NotSerializableException;
import java.nio.ByteBuffer;

/**
 * @auther: lind
 * @since: 1.0
 */
public class BlobValueCodecTest {

    @Test
    @SneakyThrows
    public void testSimple() {
        BlobValueCodec codec = new BlobValueCodec();

        Assert.assertNull(codec.decode(null));
        Assert.assertNull(codec.encode(null));

        Object[] values = {"1", 1, true, 1L, 0.2D};
        for (Object value:values){

            Object encode =codec.encode(value);
            Assert.assertNotNull(encode);
            Assert.assertTrue(encode instanceof byte[]);

            Object decode = codec.decode(new SerialBlob((byte[]) encode));
            Assert.assertEquals(decode,value);
        }

        for (Object value:values){
            Object decode = codec.decode(io.r2dbc.spi.Blob.from(Mono.just(ByteBuffer.wrap((byte[]) codec.encode(value)))));
            Assert.assertEquals(decode,value);
        }
    }

    @Test
    public void testByteArr(){
        byte[] arr = {1,2,3};
        BlobValueCodec codec = new BlobValueCodec();
        Assert.assertArrayEquals(arr, (byte[]) codec.decode(codec.encode(arr)));
    }

    @Test
    public void testError(){
        BlobValueCodec codec = new BlobValueCodec();
        try {
            codec.encode(new Object());
            Assert.fail();
        }catch (Exception e){
            Assert.assertTrue(e instanceof NotSerializableException);
        }
    }
}
