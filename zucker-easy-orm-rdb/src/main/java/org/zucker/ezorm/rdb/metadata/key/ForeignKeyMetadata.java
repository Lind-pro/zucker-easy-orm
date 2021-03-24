package org.zucker.ezorm.rdb.metadata.key;

import org.zucker.ezorm.core.meta.ObjectMetadata;
import org.zucker.ezorm.core.meta.ObjectType;
import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.rdb.metadata.RDBObjectType;
import org.zucker.ezorm.rdb.metadata.TableOrViewMetadata;
import org.zucker.ezorm.rdb.operator.dml.JoinType;

import java.util.List;
import java.util.Optional;

/**
 * 外键
 *
 * @auther: lind
 * @since: 1.0
 */
public interface ForeignKeyMetadata extends ObjectMetadata {

    @Override
    default ObjectType getObjectType() {
        return RDBObjectType.foreignKey;
    }

    /**
     * @return 是否为逻辑外键
     */
    boolean isLogical();

    /**
     * @return 是否N对多
     */
    boolean isToMany();

    AssociationType getType();

    TableOrViewMetadata getSource();

    TableOrViewMetadata getTarget();

    List<ForeignKeyColumn> getColumns();

    boolean isAutoJoin();

    /**
     * @return 自动关联类型
     */
    JoinType getJoinType();

    List<Term> getTerms();

    Optional<ForeignKeyMetadata> getMiddleForeignKey(String name);

    List<ForeignKeyMetadata> getMiddleForeignKeys();
}
