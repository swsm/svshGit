/**
 * BaseDaoException.java
 * Created at 2014-7-20
 * Created by Lee
 * Copyright (C) 2014 SHANGHAI BROADTEXT, All rights reserved.
 */
package com.core.exception;

/**
 * 
 * <p>ClassName: BaseDaoException</p>
 * <p>Description: 数据库操作异常</p>
 */
public class BaseDaoException extends BaseException {

    /**
     * <p>Field serialVersionUID: 版本</p>
     */
    private static final long serialVersionUID = 1L;
    
    public BaseDaoException(String message) {
        super(message);
    }
    
    /**
     * 
     * <p>Description: 自定义异常</p>
     * @param message 异常类型枚举
     * @param cause 异常栈
     */
    public BaseDaoException(Object message, Throwable cause ){
        super(message.toString(), cause);
    }
    
    /**
     * 
     * <p>Description: 自定义异常类型</p>
     * @param message 异常类型枚举
     */
    public BaseDaoException(Object message){
        super(message.toString());
    }

}