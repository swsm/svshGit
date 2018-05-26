/**
 * LoginState.java
 * Created at 2016-5-5
 * Created by TangSanlin
 * Copyright (C) 2016 BROADTEXT SOFTWARE, All rights reserved.
 */
package com.swsm.system.login.session;
/**
 * 
 * <p>ClassName: LoginState</p>
 * <p>Description: 登录状态类型</p>
 */
public enum LoginState {
    /**
     * 退出
     */
    LOGOUT("LOGOUT"), 
    /**
     * 登录中
     */
    LOGING("LOGING"), 
    /**
     * 登录
     */
    LOGIN("LOGIN");
    /**
     * 状态标志
     */
    private String context;
    /**
     * 
     * <p>Description: 构建状态类型</p>
     * @param context 状态标识
     */
    private LoginState(String context) {
        this.context = context;
    }
    /**
     * 
     * <p>Description: 获取状态标识</p>
     * @return 返回状态标识
     */
    public String getContext() {
        return this.context;
    }

}
