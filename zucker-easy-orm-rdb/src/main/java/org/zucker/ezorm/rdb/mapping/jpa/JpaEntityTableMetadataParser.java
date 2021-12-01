package org.zucker.ezorm.rdb.mapping.jpa;

import lombok.Setter;
import org.hswebframework.utils.ClassUtils;
import org.zucker.ezorm.rdb.mapping.annotation.Comment;
import org.zucker.ezorm.rdb.mapping.parser.*;
import org.zucker.ezorm.rdb.metadata.RDBDatabaseMetadata;
import org.zucker.ezorm.rdb.metadata.RDBSchemaMetadata;
import org.zucker.ezorm.rdb.metadata.RDBTableMetadata;
import org.zucker.ezorm.rdb.utils.AnnotationUtils;

import javax.persistence.Table;
import java.util.Optional;

/**
 * @auther: lind
 * @since: 1.0
 * @see javax.persistence.Column
 * @see javax.persistence.JoinTable
 * @see javax.persistence.JoinColumn
 */
public class JpaEntityTableMetadataParser implements EntityTableMetadataParser {

    @Setter
    private RDBDatabaseMetadata databaseMetadata;

    @Setter
    private DataTypeResolver dataTypeResolver = DefaultDataTypeResolver.INSTANCE;

    private ValueCodecResolver valueCodecResolver = DefaultValueCodecResolver.COMMONS;

    @Override
    public Optional<RDBTableMetadata> parseTableMetadata(Class<?> entityType) {
        Table table = AnnotationUtils.getAnnotation(entityType, Table.class);
        if (table == null) {
            return Optional.empty();
        }
        RDBSchemaMetadata schema = databaseMetadata.getSchema(table.schema())
                .orElseGet(databaseMetadata::getCurrentSchema);

        RDBTableMetadata tableMetadata = schema.newTable(table.name());

        Optional.ofNullable(ClassUtils.getAnnotation(entityType, Comment.class))
                .map(Comment::value)
                .ifPresent(tableMetadata::setComment);

        JpaEntityTableMetadataParserProcessor parserProcessor = new JpaEntityTableMetadataParserProcessor(tableMetadata, entityType);
        parserProcessor.setDataTypeResolver(dataTypeResolver);
        parserProcessor.setValueCodecResolver(valueCodecResolver);
        parserProcessor.process();
        return Optional.of(tableMetadata);
    }
}
