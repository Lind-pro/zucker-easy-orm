package org.zucker.ezorm.rdb.mapping.defaults;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * upsert保存结果
 * 注意: added 和 updated 的值并不一定准确,因为有的数据库执行upsert,无法准确获取新增和修改的结果.
 * @author lind
 * @since 1.0
 */
@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class SaveResult {

    // 新增的数量
    private int added;

    // 修改的数量
    private int updated;

    public int getTotal() {
        return added + updated;
    }

    public SaveResult merge(SaveResult result) {
        SaveResult res = SaveResult.of(added, updated);
        res.added += result.getAdded();
        res.updated += res.getUpdated();
        return res;
    }

    @Override
    public String toString() {
        return "added " + added + ",updated " + updated + ",total " + getTotal();
    }
}
