package org.zucker.ezorm.rdb.metadata;

import lombok.Getter;
import lombok.Setter;
import org.zucker.ezorm.core.meta.Feature;
import org.zucker.ezorm.rdb.metadata.key.ForeignKeyMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.query.QueryTermsFragmentBuilder;
import org.zucker.ezorm.rdb.operator.builder.fragments.query.SelectColumnFragmentBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @auther: lind
 * @since: 1.0
 */
@Getter
@Setter
public abstract class AbstractTableOrViewMetadata implements TableOrViewMetadata{

    private String name;

    private String alias;

    private RDBSchemaMetadata schema;

    protected Map<String,RDBColumnMetadata> allColumns=new ConcurrentHashMap<String,RDBColumnMetadata>(){
        @Override
        public RDBColumnMetadata get(Object key) {
            String k = String.valueOf(key);
            RDBColumnMetadata metadata = super.get(key);
            if(metadata==null){
                metadata=super.get(k.toUpperCase());
            }
            if(metadata ==null){
                metadata=super.get(k.toLowerCase());
            }
            return metadata;
        }
    };

    protected List<ForeignKeyMetadata> foreignKey = new CopyOnWriteArrayList<>();

    protected Map<String, Feature> features = new ConcurrentHashMap<>();

    public AbstractTableOrViewMetadata(){
        // 注册默认的where条件构造器
        addFeature(QueryTermsFragmentBuilder.of(this));
        // 注册默认的查询列构造器
        addFeature(SelectColumnFragmentBuilder.of(this));
        //JOIN
        addFeature();
    }
}
