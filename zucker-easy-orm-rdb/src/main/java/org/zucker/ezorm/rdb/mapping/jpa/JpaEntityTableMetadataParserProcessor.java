package org.zucker.ezorm.rdb.mapping.jpa;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.hswebframework.utils.ClassUtils;
import org.zucker.ezorm.core.DefaultValueGenerator;
import org.zucker.ezorm.core.RuntimeDefaultValue;
import org.zucker.ezorm.rdb.codec.EnumValueCodec;
import org.zucker.ezorm.rdb.mapping.DefaultEntityColumnMapping;
import org.zucker.ezorm.rdb.mapping.EntityPropertyDescriptor;
import org.zucker.ezorm.rdb.mapping.annotation.Comment;
import org.zucker.ezorm.rdb.mapping.annotation.DefaultValue;
import org.zucker.ezorm.rdb.mapping.annotation.Upsert;
import org.zucker.ezorm.rdb.mapping.parser.DataTypeResolver;
import org.zucker.ezorm.rdb.mapping.parser.ValueCodecResolver;
import org.zucker.ezorm.rdb.metadata.*;
import org.zucker.ezorm.rdb.metadata.key.AssociationType;
import org.zucker.ezorm.rdb.metadata.key.ForeignKeyBuilder;
import org.zucker.ezorm.rdb.operator.builder.fragments.term.EnumFragmentBuilder;
import org.zucker.ezorm.rdb.utils.AnnotationUtils;
import org.zucker.ezorm.rdb.utils.PropertiesUtils;

import javax.persistence.*;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Stream;

import static org.zucker.ezorm.rdb.utils.AnnotationUtils.getAnnotations;

/**
 * @auther: lind
 * @since: 1.0
 */
@Slf4j
public class JpaEntityTableMetadataParserProcessor {

    private final DefaultEntityColumnMapping mapping;

    private final Class<?> entityType;

    private final RDBTableMetadata tableMetadata;

    @Setter
    private DataTypeResolver dataTypeResolver;

    @Setter
    private ValueCodecResolver valueCodecResolver;


    public JpaEntityTableMetadataParserProcessor(RDBTableMetadata tableMetadata, Class<?> entityType) {
        this.tableMetadata = tableMetadata;
        this.entityType = entityType;
        this.mapping = new DefaultEntityColumnMapping(tableMetadata, entityType);
        tableMetadata.addFeature(this.mapping);
    }

    public void process() {
        PropertyDescriptor[] descriptors = BeanUtilsBean.getInstance()
                .getPropertyUtils()
                .getPropertyDescriptors(entityType);

        Table table = ClassUtils.getAnnotation(entityType, Table.class);
        int idx = 0;
        for (Index index : table.indexes()) {
            String name = index.name();
            if (name.isEmpty()) {
                name = tableMetadata.getName().concat("_idx_").concat(String.valueOf(idx++));
            }
            RDBIndexMetadata indexMetadata = new RDBIndexMetadata();
            indexMetadata.setUnique(index.unique());
            indexMetadata.setName(name);

            String[] columnList = index.columnList().split("[,]");
            for (String str : columnList) {
                String[] columnAndSort = str.split("[ ]+");
                RDBIndexMetadata.IndexColumn column = new RDBIndexMetadata.IndexColumn();
                column.setColumn(columnAndSort[0].trim());
                if (columnAndSort.length > 1) {
                    column.setSort(columnAndSort[1].equalsIgnoreCase("desc") ? RDBIndexMetadata.IndexSort.desc : RDBIndexMetadata.IndexSort.asc);
                }
                indexMetadata.getColumns().add(column);
            }
            tableMetadata.addIndex(indexMetadata);
        }
        idx = 0;
        for (UniqueConstraint constraint : table.uniqueConstraints()) {
            String name = constraint.name();
            if (name.isEmpty()) {
                name = tableMetadata.getName().concat("_const_").concat(String.valueOf(idx++));
            }
            ConstraintMetadata metadata = new ConstraintMetadata();
            metadata.setType(ConstraintType.Unique);
            metadata.setName(name);
            metadata.setColumns(new HashSet<>(Arrays.asList(constraint.columnNames())));
            tableMetadata.addConstraint(metadata);
        }

        List<Runnable> afterRun = new ArrayList<>();
        for (PropertyDescriptor descriptor : descriptors) {
            Set<Annotation> annotations = getAnnotations(entityType, descriptor);


            getAnnotation(annotations, Column.class)
                    .ifPresent(column -> handleColumnAnnotation(descriptor, annotations, ColumnInfo.of(column)));
            getAnnotation(annotations, JoinColumns.class)
                    .ifPresent(column -> afterRun.add(() -> handleJoinColumnAnnotation(descriptor, annotations, column.value())));

            getAnnotation(annotations, JoinColumn.class)
                    .ifPresent(column -> afterRun.add(() -> handleJoinColumnAnnotation(descriptor, annotations, column)));
        }
        afterRun.forEach(Runnable::run);
    }

    private <T extends Annotation> Optional<T> getAnnotation(Set<Annotation> annotations, Class<T> type) {
        return annotations.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .findFirst();
    }

    @SneakyThrows
    private void handleJoinColumnAnnotation(PropertyDescriptor descriptor, Set<Annotation> annotations, JoinColumn... column) {

        Field field = PropertiesUtils.getPropertyField(entityType, descriptor.getName())
                .orElseThrow(() -> new NoSuchFieldException("no such field" + descriptor.getName() + " in " + entityType));

        Table join;
        ForeignKeyBuilder builder = ForeignKeyBuilder.builder()
                .source(tableMetadata.getFullName())
                .name(descriptor.getName())
                .alias(descriptor.getName())
                .build();

        Type fieldGenericType = field.getGenericType();
        if (fieldGenericType instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) fieldGenericType).getActualTypeArguments();
            join = Stream.of(types)
                    .map(Class.class::cast)
                    .map(t -> AnnotationUtils.getAnnotation(t, Table.class))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        } else {
            builder.setAutoJoin(true);
            join = AnnotationUtils.getAnnotation(field.getType(), Table.class);
        }
        String joinTableName;
        if (join != null) {
            joinTableName = join.schema().isEmpty() ? join.name() : join.schema().concat(".").concat(join.name());
        } else {
            log.warn("can not resolve join table for :{}", field);
            return;
        }
        builder.setTarget(joinTableName);

        getAnnotation(annotations, OneToOne.class)
                .ifPresent(oneToOne -> builder.setAssociationType(AssociationType.oneToOne));
        getAnnotation(annotations, OneToMany.class)
                .ifPresent(oneToMany -> builder.setAssociationType(AssociationType.oneToMay));

        getAnnotation(annotations, ManyToMany.class)
                .ifPresent(manyToMany -> builder.setAssociationType(AssociationType.manyToMay));
        getAnnotation(annotations, ManyToOne.class)
                .ifPresent(manyToOne -> builder.setAssociationType(AssociationType.manyToOne));

        for (JoinColumn joinColumn : column) {
            String columnName = joinColumn.name();
            builder.addColumn(columnName, joinColumn.referencedColumnName());
        }
        tableMetadata.addForeignKey(builder);
    }

    @Getter
    @SuppressWarnings("all")
    private static class ColumnInfo {
        private String name = "";
        private String table = "";

        private boolean nullable;
        private boolean updatable;
        private boolean insertable;
        private boolean saveable;

        private int length;
        private int precision;
        private int scale;

        private String columnDefinition = "";

        public static ColumnInfo of(JoinColumn column) {
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.insertable = column.insertable();
            columnInfo.updatable = column.updatable();
            columnInfo.nullable = column.nullable();
            columnInfo.name = column.name();
            columnInfo.table = column.table();
            return columnInfo;
        }

        public static ColumnInfo of(Column column) {
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.insertable = column.insertable();
            columnInfo.updatable = column.updatable();
            columnInfo.nullable = column.nullable();
            columnInfo.name = column.name();
            columnInfo.table = column.table();
            columnInfo.length = column.length();
            columnInfo.scale = column.scale();
            columnInfo.precision = column.precision();
            return columnInfo;
        }
    }

    private static String camelCase2UnderScoreCase(String str) {
        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    sb.append("_");
                }
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private void handleColumnAnnotation(PropertyDescriptor descriptor, Set<Annotation> annotations, ColumnInfo column) {
        // 另外一个表
        if (!column.table.isEmpty() && !column.table.equals(tableMetadata.getName())) {
            mapping.addMapping(column.table.concat(".").concat(column.name), descriptor.getName());
            return;
        }
        String columnName;
        Field field = AnnotationUtils
                .getFieldByDescriptor(entityType, descriptor)
                .orElse(null);

        if (null == field) {
            return;
        }
        if (!column.name.isEmpty()) {
            columnName = column.name;
        } else {
            // 驼峰命名
            columnName = camelCase2UnderScoreCase(field.getName());
        }
        Class<?> javaType = descriptor.getPropertyType();

        if (javaType == Object.class) {
            javaType = descriptor.getReadMethod().getReturnType();
        }
        if (javaType == Object.class) {
            javaType = descriptor.getWriteMethod().getReturnType();
        }
        mapping.addMapping(columnName, field.getName());
        RDBColumnMetadata metadata = tableMetadata.getColumn(columnName).orElseGet(tableMetadata::newColumn);
        metadata.setName(columnName);
        metadata.setAlias(field.getName());
        metadata.setJavaType(javaType);
        metadata.setLength(column.length);
        metadata.setPrecision(column.precision);
        metadata.setScale(column.scale);
        metadata.setNotNull(!column.nullable);
        metadata.setUpdatable(column.updatable);
        metadata.setInsertable(column.insertable);
        if (!column.columnDefinition.isEmpty()) {
            metadata.setColumnDefinition(column.columnDefinition);
        }
        getAnnotation(annotations, GeneratedValue.class)
                .map(GeneratedValue::generator)
                .map(gen -> lazyDefaultValueGenerator.of(() ->
                        tableMetadata.findFeatureNow(DefaultValueGenerator.<RDBColumnMetadata>createId(gen))))
                .map(gen -> gen.generate(metadata))
                .ifPresent(metadata::setDefaultValue);

        getAnnotation(annotations, DefaultValue.class)
                .map(gen -> {
                    if (gen.value().isEmpty()) {
                        return lazyDefaultValueGenerator.of(() ->
                                        tableMetadata.findFeatureNow(DefaultValueGenerator.createId(gen.generator())))
                                .generate(metadata);
                    }
                    return (RuntimeDefaultValue) gen::value;
                })
                .ifPresent(metadata::setDefaultValue);

        getAnnotation(annotations, Comment.class)
                .map(Comment::value)
                .ifPresent(metadata::setComment);

        getAnnotation(annotations, Upsert.class)
                .map(Upsert::insertOnly)
                .ifPresent(insertOnly -> metadata.setSaveable(!insertOnly));

        getAnnotation(annotations, Id.class).ifPresent(id -> metadata.setPrimaryKey(true));

        EntityPropertyDescriptor propertyDescriptor = SimpleEntityPropertyDescriptor.of(entityType, descriptor.getName(), javaType, metadata, descriptor);

        metadata.addFeature(propertyDescriptor);

        Optional.ofNullable(dataTypeResolver)
                .map(resolver -> resolver.resolve(propertyDescriptor))
                .ifPresent(metadata::setType);

        if (metadata.getType() == null) {
            tableMetadata.getDialect()
                    .convertSqlType(metadata.getJavaType())
                    .ifPresent(jdbcType -> metadata.setJdbcType(jdbcType, metadata.getJavaType()));
        }

        Optional.ofNullable(valueCodecResolver)
                .map(resolver -> resolver.resolve(propertyDescriptor)
                        .orElseGet(() -> metadata
                                .findFeature(ValueCodecFactory.ID)
                                .flatMap(factory -> factory.createValueCodec(metadata))
                                .orElse(null)))
                .ifPresent(metadata::setValueCodec);

        if (metadata.getValueCodec() instanceof EnumValueCodec) {
            EnumValueCodec codec = (EnumValueCodec) metadata.getValueCodec();
            if (codec.isToMask()) {
                metadata.addFeature(EnumFragmentBuilder.eq);
                metadata.addFeature(EnumFragmentBuilder.not);
            }
        }
        tableMetadata.addColumn(metadata);
    }
}

