package org.zucker.ezorm.rdb.codec;

import org.zucker.ezorm.core.Decoder;
import org.zucker.ezorm.core.Encoder;
import org.zucker.ezorm.core.ValueCodec;

import javax.lang.model.type.ErrorType;
import java.util.LinkedList;

/**
 * @auther: lind
 * @since: 1.0
 */
public class CompositeValueCodec implements ValueCodec<Object, Object> {

    private LinkedList<Encoder> encoders = new LinkedList<>();
    private LinkedList<Decoder> decoders = new LinkedList<>();

    public CompositeValueCodec addEncode(Encoder encoder) {
        encoders.add(encoder);
        return this;
    }

    public CompositeValueCodec addDecoder(Decoder decoder) {
        decoders.add(decoder);
        return this;
    }

    public CompositeValueCodec addEncoderFirst(Encoder encoder) {
        encoders.addFirst(encoder);
        return this;
    }

    public CompositeValueCodec addDecoderFirst(Decoder decoder) {
        decoders.addFirst(decoder);
        return this;
    }

    @Override
    public Object encode(Object value) {
        for (Encoder codec : encoders) {
            value = codec.encode(value);
        }
        return value;
    }

    @Override
    public Object decode(Object data) {
        for (Decoder code : decoders) {
            data = code.decode(data);
        }
        return data;
    }
}
