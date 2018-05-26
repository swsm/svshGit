package com.swsm.system.login.session;

import com.core.session.Jdf3UserSession;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 * <p>
 * ClassName: UserSession
 * </p>
 * <p>
 * Description: 定义用户session类
 * </p>
 */
public class UserSession extends Jdf3UserSession {
    /**
     * 版本号
     */
    private static final long serialVersionUID = 184146177423505956L;
    /**
     * 用户状态，创建用户session信息初始时为，注销状态
     */
    private LoginState loginState = LoginState.LOGOUT;
    /**
     * 变量配置
     */
    private Map<String, String> vars = new HashMap<String, String>();
    
    /**用户token*/
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 
     * <p>
     * Description: 加入变量配置
     * </p>
     * 
     * @param varName 变量名称
     * @param varValue 变量值
     */
    public void putVar(String varName, String varValue) {
        this.vars.put(varName, varValue);
    }

    /**
     * 
     * <p>
     * Description: 根据变量名称获取变量值
     * </p>
     * 
     * @param varName 变量名称
     * @return 变量值
     */
    public String getVar(String varName) {
        return this.vars.get(varName);
    }

    /**
     * 
     * <p>
     * Description: 根据session实例化用户UserSession
     * </p>
     * 
     * @param session httpsession
     */
    public UserSession(HttpSession session) {
        super(session);
    }

    public LoginState getLoginState() {
        return this.loginState;
    }

    public void setLoginState(LoginState loginState) {
        this.loginState = loginState;
    }

}
