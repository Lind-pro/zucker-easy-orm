package org.zucker.ezorm.rdb.codec;

import org.zucker.ezorm.core.Decoder;

import java.sql.Blob;
import java.sql.Clob;

/**
 * @auther: lind
 * @since: 1.0
 */
public class JdbcResultDecoder implements Decoder<Object> {

    public static final JdbcResultDecoder INSTANCE = new JdbcResultDecoder();

    @Override
    public Object decode(Object data) {
        if(data instanceof Clob){
            return BlobValueCodec.INSTANCE.decode(data);
        }
        if (data instanceof Blob){
            return ClobValueCodec.INSTANCE.decode(data);
        }
        return data;
    }
}
