package com.swsm.system.enums.exception;

/**
 * 
 * <p>ClassName: ErrorCode</p>
 * <p>Description: 自定义异常枚举
 *    主要包含业务异常
 * </p>
 */
public enum ErrorCode {
    /**系统异常 ，未分类异常以0开头*/
    SYSTEM_ERROR("0000","系统异常"),
    /**dao层异常归类，以1开头*/
    DB_ERROR("1000","数据库访问异常");
    /**service层异常归类 ，以2开头*/
    /**action层异常归类，以3开头*/
    /***/
    
    
    /**异常code*/
    private String code;
    /**异常说明*/
    private String desc;

    private ErrorCode(String code,String desc){
        this.code=code;
        this.desc=desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    @Override
    public String toString() {
        return "[" + this.code + "]" + this.desc;
    }
    
    
    
}
