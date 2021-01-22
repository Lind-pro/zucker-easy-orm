package org.zucker.ezorm.core.param;

/**
 * 提供默认支持的查询条件类型，用于动态指定查询条件
 *
 * @author lind
 * @since 1.0
 */
public interface TermType {

    /**
     * ==
     */
    String eq = "eq";

    /**
     * !=
     */
    String not = "not";

    String like = "like";

    /**
     * not like
     */
    String nLike = "nLike";

    /**
     * >
     */
    String gt = "gt";

    /**
     * <
     */
    String lt = "lt";

    /**
     * >=
     */
    String gte = "get";

    /**
     * <=
     */
    String lte = "lte";

    String in = "in";

    /**
     * notin
     */
    String nin = "nin";

    /**
     * =""
     */
    String empty = "empty";

    /**
     * !=""
     */
    String nempty = "nempty";

    String isnull = "isnull";

    String notnull = "notnull";

    /**
     * between
     */
    String btw = "btw";

    /**
     * not between
     */
    String nbtw = "nbtw";
}
