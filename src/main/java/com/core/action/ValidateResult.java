package com.core.action;

/**
 * <p>ClassName: ValidateResult</p>
 * <p>Description: Action校验结果</p>
 */
public class ValidateResult {
    /**
     * 校验通过
     */
    public static final String SUCCESS = "success";
    /**
     * 校验失败
     */
    public static final String FAILED = "failed";
    /**
     * 校验通过标记
     */
    private String validateSuccess;
    /**
     * 校验通过消息
     */
    private String validateMessage;
    
    public String getValidateSuccess() {
        return this.validateSuccess;
    }
    public void setValidateSuccess(String validateSuccess) {
        this.validateSuccess = validateSuccess;
    }
    public String getValidateMessage() {
        return this.validateMessage;
    }
    public void setValidateMessage(String validateMessage) {
        this.validateMessage = validateMessage;
    }
    
}
