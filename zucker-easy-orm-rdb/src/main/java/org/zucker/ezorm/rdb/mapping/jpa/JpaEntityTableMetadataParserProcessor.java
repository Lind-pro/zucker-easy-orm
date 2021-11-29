package org.zucker.ezorm.rdb.mapping.jpa;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.hswebframework.utils.ClassUtils;
import org.zucker.ezorm.rdb.mapping.DefaultEntityColumnMapping;
import org.zucker.ezorm.rdb.mapping.parser.DataTypeResolver;
import org.zucker.ezorm.rdb.metadata.*;
import org.zucker.ezorm.rdb.utils.AnnotationUtils;

import javax.persistence.*;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

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

    @Getter
    private DataTypeResolver dataTypeResolver;

    private JpaEntityTableMetadataParserProcessor(RDBTableMetadata tableMetadata, Class<?> entityType) {
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
                    .ifPresent(column -> {
                    });
        }
    }

    private <T extends Annotation> Optional<T> getAnnotation(Set<Annotation> annotations, Class<T> type) {
        return annotations.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .findFirst();
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

    private static String camelCase2UnderScorceCase(String str) {
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
            columnName = camelCase2UnderScorceCase(field.getName());
        }
        Class<?> javaType = descriptor.getPropertyType();

        if(javaType==Object.class){
            javaType=descriptor.getReadMethod().getReturnType();
        }
        if(javaType==Object.class){
            javaType=descriptor.getWriteMethod().getReturnType();
        }
        mapping.addMapping(columnName,field.getName());
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
        if(!column.columnDefinition.isEmpty()){
            metadata.setColumnDefinition(column.columnDefinition);
        }
        // TODO
//        getAnnotation(annotations,GeneratedValue.class)
//                .map(GeneratedValue::generator)
    }
}

