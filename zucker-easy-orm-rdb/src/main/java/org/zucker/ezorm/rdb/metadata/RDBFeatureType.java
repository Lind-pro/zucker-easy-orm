package org.zucker.ezorm.rdb.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.zucker.ezorm.core.FeatureType;

/**
 * @author lind
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum RDBFeatureType implements FeatureType {

    dialect("数据库方言"),
    termType("SQL条件"),
    termsType("SQL条件组合"),
    query("查询"),
    paginator("分页器"),
    sqlBuilder("SQL构造器"),
    sqlExecutor("SQL执行器"),
    metadataParser("元数据解析器"),
    function("函数"),
    fragment("SQL片段"),
    foreignKeyTerm("外键关联条件"),
    codec("编解码器"),
    exceptionTranslation("异常转换"),
    saveOrUpdateOperator("新增或者保存操作器");

    @Override
    public String getId() {
        return name();
    }

    private String name;

    public String getFeatureId(String suffix) {
        return getId().concat(":").concat(suffix);
    }
}
