package org.zucker.ezorm.rdb.metadata;

import lombok.AllArgsConstructor;
import org.zucker.ezorm.core.OriginalValueCodec;
import org.zucker.ezorm.core.ValueCodec;
import org.zucker.ezorm.rdb.codec.BlobValueCodec;
import org.zucker.ezorm.rdb.codec.ClobValueCodec;
import org.zucker.ezorm.rdb.codec.DateTimeCodec;
import org.zucker.ezorm.rdb.codec.NumberValueCodec;
import org.zucker.ezorm.rdb.utils.DataTypeUtils;

import java.sql.JDBCType;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @auther: lind
 * @since: 1.0
 */
public class DefaultValueCodecFactory implements ValueCodecFactory {

    private List<Strategy> strategies = new CopyOnWriteArrayList<>();

    private ValueCodec defaultCodec = OriginalValueCodec.INSTANCE;

    public static DefaultValueCodecFactory COMMONS = new DefaultValueCodecFactory();

    static {
        COMMONS.register(column -> DataTypeUtils.typeIsNumber(column.getType()),
                column -> new NumberValueCodec(column.getJavaType()));

        COMMONS.register(column -> DataTypeUtils.typeIsDate(column.getType()),
                column -> new DateTimeCodec("yyyy-MM-dd HH:mm:ss", column.getJavaType()));

        COMMONS.register(column -> column.getSqlType() == JDBCType.CLOB, column -> ClobValueCodec.INSTANCE);
        COMMONS.register(column -> column.getSqlType() == JDBCType.BLOB, column -> BlobValueCodec.INSTANCE);
    }

    public void register(Predicate<RDBColumnMetadata> predicate, Function<RDBColumnMetadata, ValueCodec> function) {
        strategies.add(new Strategy(predicate, function));
    }

    @Override
    public Optional<ValueCodec> createValueCodec(RDBColumnMetadata column) {
        return strategies.stream()
                .filter(strategy -> strategy.predicate.test(column))
                .map(strategy -> strategy.function.apply(column))
                .findFirst();
    }

    @AllArgsConstructor
    class Strategy {
        Predicate<RDBColumnMetadata> predicate;

        Function<RDBColumnMetadata, ValueCodec> function;
    }
}
