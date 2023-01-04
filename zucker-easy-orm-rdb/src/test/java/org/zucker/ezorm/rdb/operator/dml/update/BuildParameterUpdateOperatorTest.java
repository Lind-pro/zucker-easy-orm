package org.zucker.ezorm.rdb.operator.dml.update;

import org.junit.Assert;
import org.junit.Test;

public class BuildParameterUpdateOperatorTest {

    @Test
    public void test() {
        BuildParameterUpdateOperator operator = new BuildParameterUpdateOperator();
        operator.set("name", "1234")
                .where(dsl -> dsl.and("id", "1"));

        UpdateOperatorParameter parameter = operator.getParameter();

        Assert.assertEquals(parameter.getColumns().get(0).getColumn(), "name");
        Assert.assertEquals(parameter.getColumns().get(0).getValue(), "1234");

        Assert.assertEquals(parameter.getWhere().get(0).getColumn(), "id");
        Assert.assertEquals(parameter.getWhere().get(0).getValue(), "1");
    }
}
