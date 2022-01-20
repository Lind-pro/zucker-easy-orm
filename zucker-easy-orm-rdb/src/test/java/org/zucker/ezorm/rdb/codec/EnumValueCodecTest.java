package org.zucker.ezorm.rdb.codec;

import org.junit.Assert;
import org.junit.Test;

/**
 * @auther: lind
 * @since: 1.0
 */
public class EnumValueCodecTest {

    @Test
    public void testSimple() {
        EnumValueCodec codec = new EnumValueCodec(TestEnum.class);

        Assert.assertEquals("A", codec.encode(TestEnum.A));
        Assert.assertEquals(TestEnum.A, codec.decode("A"));
    }

    @Test
    public void testArray() {
        EnumValueCodec codec = new EnumValueCodec(TestEnum[].class);
        Assert.assertEquals("A,C", codec.encode(new TestEnum[]{TestEnum.A, TestEnum.C}));

        Assert.assertArrayEquals(new TestEnum[]{TestEnum.A}, (Object[]) codec.decode("A"));
    }

    @Test
    public void testMask() {
        EnumValueCodec codec = new EnumValueCodec(TestEnum.class, true);
        Assert.assertEquals(1L, codec.encode(TestEnum.A));

        Assert.assertEquals(TestEnum.A, codec.decode(1));
    }

    @Test
    public void testMaskArray() {
        EnumValueCodec codec = new EnumValueCodec(TestEnum[].class, true);
        Assert.assertEquals(3L, codec.encode(new TestEnum[]{TestEnum.A, TestEnum.B}));
        Assert.assertArrayEquals(new TestEnum[]{TestEnum.A, TestEnum.B}, (Object[]) codec.decode(3));
    }

    public enum TestEnum {
        A, B, C
    }
}
