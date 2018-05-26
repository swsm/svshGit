package com.core.action;

/**
 * <p>ClassName: HandlerResult</p>
 * <p>Description: Action操作结果</p>
 */
public class HandlerResult {
    /**
     * 操作成功
     */
    public static final String SUCCESS = "success";
    
    /**
     * 操作失败
     */
    public static final String FAILED = "failed";

    /**
     * 操作成功
     */
    private String success;
    /**
     * 操作消息
     */
    private String message;
    /**
     * 返回对象
     */
    private Object resultObj;
    
    public String getSuccess() {
        return this.success;
    }
    public void setSuccess(String success) {
        this.success = success;
    }
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Object getResultObj() {
        return this.resultObj;
    }
    public void setResultObj(Object resultObj) {
        this.resultObj = resultObj;
    }
    
}

