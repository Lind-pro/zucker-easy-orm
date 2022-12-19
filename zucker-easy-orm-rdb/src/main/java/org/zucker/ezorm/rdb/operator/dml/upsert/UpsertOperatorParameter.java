package org.zucker.ezorm.rdb.operator.dml.upsert;

import lombok.Getter;
import lombok.Setter;
import org.zucker.ezorm.rdb.operator.dml.insert.InsertColumn;

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
public class UpsertOperatorParameter {

    private Set<UpsertColumn> columns = new LinkedHashSet<>();

    private List<List<Object>> values = new ArrayList<>();

    private boolean doNothingOnConflict = false;

    @SuppressWarnings("all")
    public Set<InsertColumn> toInsertColumns() {
        return (Set) columns;
    }
}
