/**
 * DaoException.java
 * Created at 2014-7-20
 * Created by lift
 * Copyright (C) 2014 SHANGHAI BROADTEXT, All rights reserved.
 */
package com.core.exception;

/**
 * 
 * <p>ClassName: BaseException</p>
 * <p>Description: 业务异常基类</p>
 */
public class BaseException extends RuntimeException {
    
    /**
     * <p>Field serialVersionUID: 序列号</p>
     */
    private static final long serialVersionUID = 9154911670481769135L;
    
    /**
     * 
     * <p>Description: 自定义异常</p>
     * @param message 异常类型枚举
     * @param cause 异常栈
     */
    public BaseException(Object message, Throwable cause ){
        super(message.toString(), cause);
    }
    
    /**
     * 
     * <p>Description: 自定义异常类型</p>
     * @param message 异常类型枚举
     */
    public BaseException(Object message){
        super(message.toString());
    }
    
    
    /**
     * Constructs a new exception with <code>null</code> as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public BaseException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message. The cause
     * is not initialized, and may subsequently be initialized by a call to
     * {@link #initCause}.
     * 
     * @param message the detail message. The detail message is saved for later
     *            retrieval by the {@link #getMessage()}method.
     */
    public BaseException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * <p>
     * Note that the detail message associated with <code>cause</code> is <i>not
     * </i> automatically incorporated in this exception's detail message.
     * 
     * @param message the detail message (which is saved for later retrieval by
     *            the {@link #getMessage()}method).
     * @param cause the cause (which is saved for later retrieval by the
     *            {@link #getCause()}method). (A<tt>null</tt> value is
     *            permitted, and indicates that the cause is nonexistent or
     *            unknown.)
     */
    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message
     * of <tt>(cause==null ? null : cause.toString())</tt> (which typically
     * contains the class and detail message of <tt>cause</tt>). This
     * constructor is useful for exceptions that are little more than wrappers
     * for other throwables (for example,
     * {@link java.security.PrivilegedActionException}).
     * 
     * @param cause the cause (which is saved for later retrieval by the
     *            {@link #getCause()}method). (A<tt>null</tt> value is
     *            permitted, and indicates that the cause is nonexistent or
     *            unknown.)
     */
    public BaseException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * parameter keys.
     * <p>
     * keys形式为new String[]{"我","你"}
     * 
     * @param message the detail message (which is saved for later retrieval by
     *            the {@link #getMessage()}method).
     * @param keys 消息中的参数 为String 数组
     */
    public BaseException(String message, String[] keys) {
        super(message);
    }
}