package org.zucker.ezorm.rdb.operator.builder.fragments.term;

import org.zucker.ezorm.core.FeatureId;
import org.zucker.ezorm.core.FeatureType;
import org.zucker.ezorm.core.meta.Feature;
import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.rdb.metadata.RDBFeatureType;
import org.zucker.ezorm.rdb.metadata.key.ForeignKeyMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.SqlFragments;

import java.util.List;

/**
 * @author lind
 * @since 1.0
 */
public interface ForeignKeyTermFragmentBuilder extends Feature {

    String idValue = "foreignKeyTermFragmentBuilder";

    FeatureId<ForeignKeyTermFragmentBuilder> ID = FeatureId.of(idValue);

    @Override
    default String getId() {
        return idValue;
    }

    @Override
    default String getName() {
        return getType().getName();
    }

    @Override
    default FeatureType getType() {
        return RDBFeatureType.foreignKeyTerm;
    }

    SqlFragments createFragments(String tableName, ForeignKeyMetadata key, List<Term> terms);
}
