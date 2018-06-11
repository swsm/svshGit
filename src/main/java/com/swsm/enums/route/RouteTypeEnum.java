package com.swsm.enums.route;
/**
 * 
 * <p>ClassName: RouteType</p>
 * <p>Description: 产品工艺流程中流程类型定义</p>
 */
public enum RouteTypeEnum {
    
    
    STANDARD(0,"标准流程"),
    TEMPORARY(1,"临时流程");
    
    /**流程类型id*/
    private Integer typeId;
    /**描述*/
    private String desc;
    
    private RouteTypeEnum(Integer typeId,String desc){
        this.typeId=typeId;
        this.desc=desc;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    
   
    
}
