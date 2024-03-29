package org.zucker.ezorm.rdb.operator.builder.fragments.query;

import lombok.AllArgsConstructor;
import org.zucker.ezorm.core.param.SQLTerm;
import org.zucker.ezorm.rdb.metadata.RDBColumnMetadata;
import org.zucker.ezorm.rdb.metadata.TableOrViewMetadata;
import org.zucker.ezorm.rdb.metadata.key.ForeignKeyColumn;
import org.zucker.ezorm.rdb.metadata.key.ForeignKeyMetadata;
import org.zucker.ezorm.rdb.operator.builder.fragments.NativeSql;
import org.zucker.ezorm.rdb.operator.builder.fragments.PrepareSqlFragments;
import org.zucker.ezorm.rdb.operator.builder.fragments.SqlFragments;
import org.zucker.ezorm.rdb.operator.builder.fragments.function.FunctionFragmentBuilder;
import org.zucker.ezorm.rdb.operator.dml.Join;
import org.zucker.ezorm.rdb.operator.dml.query.QueryOperatorParameter;
import org.zucker.ezorm.rdb.operator.dml.query.SelectColumn;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lind
 * @since 1.0
 */
@AllArgsConstructor(staticName = "of")
public class SelectColumnFragmentBuilder implements QuerySqlFragmentBuilder {

    private TableOrViewMetadata metadata;

    @Override
    public String getId() {
        return selectColumns;
    }

    @Override
    public String getName() {
        return "查询列";
    }

    private Set<SelectColumn> getAllSelectColumn(String ownerAlias, Set<String> excludes, TableOrViewMetadata metadata) {
        return metadata
                .getColumns()
                .stream()
                .map(columnMetadata ->
                        Optional.ofNullable(ownerAlias)
                                .map(alias -> SelectColumn.of(alias.concat(".").concat(columnMetadata.getName()), alias.concat(".").concat(columnMetadata.getAlias())))
                                .orElseGet(() -> SelectColumn.of(columnMetadata.getName(), columnMetadata.getAlias())))
                .collect(Collectors.toSet());
    }

    private Set<SelectColumn> createSelectColumns(QueryOperatorParameter parameter) {
        Set<SelectColumn> columns = new LinkedHashSet<>(parameter.getSelect());
        Set<String> excludes = parameter.getSelectExcludes();
        if (columns.isEmpty()) {
            return getAllSelectColumn(null, excludes, metadata);
        } else {
            Set<SelectColumn> realColumns = new LinkedHashSet<>();
            for (SelectColumn column : parameter.getSelect()) {
                String columnName = column.getColumn();
                if (column.getFunction() != null || columnName == null || excludes.contains(columnName)) {
                    realColumns.add(column);
                    continue;
                }
                if (columnName.contains("*")) {
                    String[] arr = columnName.split("[.]");
                    if (arr.length == 1 || metadata.equalsNameOrAlias(arr[0])) {
                        // 当前表全部字段
                        realColumns.addAll(getAllSelectColumn(null, excludes, metadata));
                    } else if (arr.length == 2) {
                        // join的表
                        parameter.findJoin(arr[0])
                                .flatMap(join -> metadata.getSchema().findTableOrView(join.getTarget()))
                                .map(tar -> getAllSelectColumn(arr[0], excludes, tar))
                                .map(Optional::of)
                                .orElseGet(() -> {
                                    // 逻辑外键表全部字段
                                    return metadata.getForeignKey(arr[0])
                                            .filter(ForeignKeyMetadata::isAutoJoin)
                                            .map(ForeignKeyMetadata::getTarget)
                                            .map(tar -> getAllSelectColumn(arr[0], excludes, tar));
                                }).ifPresent(realColumns::addAll);
                    }
                    continue;
                }
                realColumns.add(column);
            }
            return realColumns;
        }
    }

    @Override
    public SqlFragments createFragments(QueryOperatorParameter parameter) {
        Set<SelectColumn> columns = createSelectColumns(parameter);

        PrepareSqlFragments fragments = columns
                .stream()
                .map(column -> this.createFragments(parameter, column))
                .filter(Objects::nonNull)
                .reduce(PrepareSqlFragments.of(), (main, source) -> main.addFragments(source).addSql(","));

        fragments.removeLastSql();
        return fragments;
    }

    private String getAlias(String owner, RDBColumnMetadata metadata, SelectColumn column) {
        if (column.getAlias() != null) {
            return this.metadata.getDialect().quote(column.getAlias(), false);
        }
        if (metadata == null) {
            return null;
        }
        String alias = metadata.getAlias();
        if (alias.contains(".")) {
            return alias;
        }
        if (owner != null) {
            alias = owner.concat(".").concat(alias);
        }
        return metadata.getDialect().quote(alias, false);
    }

    @SuppressWarnings("all")
    protected List<Join> createJoin(String owner, String target, ForeignKeyMetadata key) {
        List<Join> joins = new ArrayList<>();

        //中间表
        for (ForeignKeyMetadata middleForeignKey : key.getMiddleForeignKeys()) {
            Join join = new Join();
            for (ForeignKeyColumn column : middleForeignKey.getColumns()) {
                PrepareSqlFragments condition = PrepareSqlFragments.of();
                condition.addSql(column.getSourceColumn().getFullName(key.getAlias()));
                condition.addSql("=").addSql(column.getTargetColumn().getFullName(middleForeignKey.getAlias()));
                join.getTerms().add(SQLTerm.of(condition.toRequest().getSql()));
            }
            if (middleForeignKey.getTerms() != null) {
                join.getTerms().addAll(middleForeignKey.getTerms());
            }
            join.setTarget(middleForeignKey.getTarget().getFullName());
            join.setType(middleForeignKey.getJoinType());
            join.addAlias(middleForeignKey.getName(), middleForeignKey.getAlias(), middleForeignKey.getTarget().getAlias(), middleForeignKey.getName());
            joins.add(join);
        }
        {
            Join join = new Join();
            join.setType(key.getJoinType());
            join.setTarget(key.getTarget().getFullName());
            join.setAlias(owner);
            join.addAlias(key.getTarget().getAlias());
            // 关联条件
            for (ForeignKeyColumn column : key.getColumns()) {
                PrepareSqlFragments condition = PrepareSqlFragments.of();
                condition.addSql(column.getSourceColumn().getFullName(target));
                condition.addSql("=").addSql(column.getTargetColumn().getFullName(key.getAlias()));
                join.getTerms().add(SQLTerm.of(condition.toRequest().getSql()));
            }
            joins.add(join);
        }
        return joins;
    }

    public PrepareSqlFragments createFragments(QueryOperatorParameter parameter, SelectColumn selectColumn) {
        if (selectColumn instanceof NativeSql) {
            return PrepareSqlFragments.of()
                    .addSql(((NativeSql) selectColumn).getSql())
                    .addParameter(((NativeSql) selectColumn).getParameters());
        }
        String columnStr = selectColumn.getColumn();
        RDBColumnMetadata columnMetadata;

        if (columnStr != null && columnStr.contains(".")) {//关联表 table.column
            String[] arr = columnStr.split("[.]");
            return parameter
                    .findJoin(arr[0])
                    .flatMap(join -> metadata
                            .getSchema()
                            .getTableOrView(join.getTarget())
                            .flatMap(table -> table.getColumn(arr[1]))
                            .flatMap(joinColumn -> this
                                    .createFragments(getColumnFullName(joinColumn, join.getAlias()), joinColumn, selectColumn)
                                    .map(fragments -> PrepareSqlFragments
                                            .of()
                                            .addParameter(fragments)
                                            .addSql("as", getAlias(join.getAlias(), joinColumn, selectColumn)))))
                    .orElseGet(() -> metadata
                            .getForeignKey(arr[0])
                            .filter(ForeignKeyMetadata::isAutoJoin)
                            .filter(key -> key.getTarget().findColumn(arr[1]).isPresent())
                            .map(foreignKey -> {
                                // 自动join
                                if (!parameter.findJoin(arr[0]).isPresent()) {
                                    parameter
                                            .getJoins()
                                            .addAll(createJoin(arr[0], parameter.getFormAlias(), foreignKey));
                                }
                                PrepareSqlFragments sqlFragments = PrepareSqlFragments.of();
                                RDBColumnMetadata targetColumn = foreignKey
                                        .getTarget()
                                        .getColumn(arr[1])
                                        .orElse(null);
                                if (targetColumn == null) {
                                    return null;
                                }
                                sqlFragments.addSql(targetColumn.getFullName(arr[0]), "as", getAlias(arr[0], targetColumn, selectColumn));
                                return sqlFragments;
                            }).orElse(null));
        } else {
            columnMetadata = metadata.findColumn(columnStr).orElse(null);
        }
        RDBColumnMetadata findColumnMetadata = columnMetadata;
        String columnFullName = Optional.ofNullable(findColumnMetadata).map(this::getColumnFullName).orElse(null);
        return this
                .createFragments(columnFullName, columnMetadata, selectColumn)
                .map(fragments -> {
                    PrepareSqlFragments sqlFragments = PrepareSqlFragments.of().addFragments(fragments);
                    sqlFragments.addSql("as").addSql(getAlias(null, findColumnMetadata, selectColumn));
                    return sqlFragments;
                }).orElse(null);
    }

    public Optional<SqlFragments> createFragments(String columnFullName, RDBColumnMetadata columnMetadata, SelectColumn column) {
        String function = column.getFunction();
        if (function != null) {
            return metadata.findFeature(FunctionFragmentBuilder.createFeatureId(function))
                    .map(fragment -> fragment.create(columnFullName, columnMetadata, column.getOpts()))
                    .map(fragments -> {
                        if (fragments.isEmpty()) {
                            throw new UnsupportedOperationException("unsupported function" + column);
                        }
                        return fragments;
                    });
        } else {
            return Optional.ofNullable(columnFullName)
                    .map(PrepareSqlFragments::of);
        }
    }

    protected String getColumnFullName(RDBColumnMetadata column, String alias) {
        return column.getFullName(alias);
    }

    protected String getColumnFullName(RDBColumnMetadata column) {
        return column.getFullName();
    }
}
