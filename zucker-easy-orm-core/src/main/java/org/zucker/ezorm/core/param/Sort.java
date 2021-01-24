package org.zucker.ezorm.core.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class Sort extends Column{

    @Schema(description = "排序方式",allowableValues = {"asc","desc"},minLength = 3,maxLength = 4)
    private String order = "asc";

    public Sort(){
    }

    public Sort(String column){
        this.setName(column);
    }

    public String getOrder(){
        if("desc".equalsIgnoreCase(order)){
            return order;
        }else{
            return order="asc";
        }
    }

    public void asc(){
        this.order="asc";
    }

    public void desc(){
        this.order="desc";
    }
}
