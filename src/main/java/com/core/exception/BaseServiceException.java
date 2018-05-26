package com.core.exception;

/**
 * 
 * <p>ClassName: BaseServiceException</p>
 * <p>Description: 业务层操作异常基类</p>
 */
public class BaseServiceException extends BaseException {

    /**
     * <p>Field serialVersionUID: TODO</p>
     */
    private static final long serialVersionUID = 6797192288283648830L;

    
    /**
     * 
     * <p>Description: 自定义异常</p>
     * @param message 异常类型枚举
     * @param cause 异常栈
     */
    public BaseServiceException(Object message, Throwable cause ){
        super(message.toString(), cause);
    }
    
    /**
     * 
     * <p>Description: 自定义异常类型</p>
     * @param message 异常类型枚举
     */
    public BaseServiceException(Object message){
        super(message.toString());
    }
}
