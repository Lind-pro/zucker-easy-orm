package org.zucker.ezorm.rdb.operator.dml;

import org.junit.Assert;
import org.junit.Test;
import org.zucker.ezorm.core.dsl.Query;
import org.zucker.ezorm.rdb.operator.dml.query.BuildParameterQueryOperator;
import org.zucker.ezorm.rdb.operator.dml.query.Joins;
import org.zucker.ezorm.rdb.operator.dml.query.QueryOperatorParameter;

import static org.zucker.ezorm.rdb.operator.dml.query.Selects.count;

public class BuildParameterQueryOperatorTest {

    @Test
    public void test() {
        BuildParameterQueryOperator query = new BuildParameterQueryOperator("u_user");

        query.select(count("id").as("total"))
                .join(Joins.left("detail").as("info"))
                .where(dsl -> dsl.like("name", "1234"))
                .paging(10, 0);

        QueryOperatorParameter parameter = query.getParameter();
        Assert.assertEquals(parameter.getFrom(), "u_user");
        Assert.assertFalse(parameter.getSelect().isEmpty());
        Assert.assertFalse(parameter.getJoins().isEmpty());
        Assert.assertFalse(parameter.getWhere().isEmpty());
        Assert.assertEquals(parameter.getPageIndex(), Integer.valueOf(10));
        Assert.assertEquals(parameter.getPageSize(), Integer.valueOf(0));
    }

    @Test
    public void testFromParameter() {
        BuildParameterQueryOperator query = new BuildParameterQueryOperator("u_user");
        query.setParam(Query.of()
                .select("id", "total")
                .where("name", "1234")
                .doPaging(10, 0)
                .getParam());

        QueryOperatorParameter parameter = query.getParameter();

        Assert.assertEquals(parameter.getFrom(), "u_user");
        Assert.assertFalse(parameter.getSelect().isEmpty());
        Assert.assertFalse(parameter.getWhere().isEmpty());
        Assert.assertEquals(parameter.getPageIndex(), Integer.valueOf(10));
        Assert.assertEquals(parameter.getPageSize(), Integer.valueOf(0));
    }
}
