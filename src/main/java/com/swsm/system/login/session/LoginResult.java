package com.swsm.system.login.session;

/**
 * 
 * <p>ClassName: LoginResult</p>
 * <p>Description: 用户登录系统的结果类型</p>
 */
public enum LoginResult {
    /**
     * 表示用户登录系统OK
     */
    LOGINOK("登录成功"), 
    /**
     * 输入的用户名或者密码不正确
     */
    LOGINNAMENOTFOUND("输入的用户名或者密码不正确！"), 
    /**
     * 此用户已被禁用，请与管理员联系！
     */
    LOGINNAMEFORBIDDEN("此用户已被禁用，请与管理员联系！"),
    /**
     * 此用户尚未开通登录权限，请与管理员联系
     */
    USERNOTAVIABLE("此用户尚未开通登录权限，请与管理员联系！"), 
    /**
     * 输入的用户名或者密码不正确
     */
    ERRORPASSWORD("输入的用户名或者密码不正确！"), 
    /**
     * 验证码错误或已过期，请重新刷新页面
     */
    ERRORIDENTIFYINGCODE("验证码错误或已过期，请重新刷新页面！"),
    /**
     * session time out
     */
    SESSIONTIMEOUT("session time out！"), 
    /**
     * 工号不存在！
     */
    WORKNONOTFOUND("此工号不存在"),
    /**
     * 登录的用户数已经超过了系统设置的最大值  
     */
    LOGINNUMBERGTMAXUSER("登录的用户数已经超过了系统设置的最大值");
    /**
     * 枚举值
     */
    
  
    
    private String context;
    /**
     * 
     * <p>Description: LoginResult</p>
     * @param context context
     */
    private LoginResult(String context) {
        this.context = context;
    }
    /**
     * 
     * <p>Description: getContext</p>
     * @return String
     */
    public String getContext() {
        return this.context;
    }

}
