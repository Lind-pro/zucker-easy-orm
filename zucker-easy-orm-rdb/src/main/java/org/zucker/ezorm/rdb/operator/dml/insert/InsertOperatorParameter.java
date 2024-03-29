package org.zucker.ezorm.rdb.operator.dml.insert;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lind
 * @since 1.0
 */
@Getter
@Setter
public class InsertOperatorParameter {

    private Set<InsertColumn> columns = new LinkedHashSet<>();

    private List<List<Object>> values = new ArrayList<>();
}
