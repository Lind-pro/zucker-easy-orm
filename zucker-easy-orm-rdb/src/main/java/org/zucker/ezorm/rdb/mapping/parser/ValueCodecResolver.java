package org.zucker.ezorm.rdb.mapping.parser;

import org.zucker.ezorm.core.ValueCodec;
import org.zucker.ezorm.rdb.mapping.EntityPropertyDescriptor;

import java.util.Optional;

/**
 * @auther: lind
 * @since: 1.0
 */
public interface ValueCodecResolver {

    Optional<ValueCodec> resolve(EntityPropertyDescriptor descriptor);
}
