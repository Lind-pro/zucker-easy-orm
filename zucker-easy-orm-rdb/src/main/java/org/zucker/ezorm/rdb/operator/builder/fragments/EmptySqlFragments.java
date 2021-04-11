package org.zucker.ezorm.rdb.operator.builder.fragments;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zucker.ezorm.core.param.SQLTerm;
import org.zucker.ezorm.core.param.Term;
import org.zucker.ezorm.rdb.operator.builder.FragmentBlock;
import org.zucker.ezorm.rdb.utils.PropertiesUtils;

import java.util.Collections;
import java.util.List;

/**
 * @auther: lind
 * @since: 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmptySqlFragments implements SqlFragments {

    public static final EmptySqlFragments INSTANCE = new EmptySqlFragments();

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public List<String> getSql() {
        return Collections.emptyList();
    }

    @Override
    public List<Object> getParameters() {
        return Collections.emptyList();
    }

}
