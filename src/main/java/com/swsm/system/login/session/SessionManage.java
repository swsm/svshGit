/**
 * SessionManage.java
 * Created at 2016-5-5
 * Created by Tangsanlin
 * Copyright (C) 2016 BROADTEXT SOFTWARE, All rights reserved.
 */
package com.swsm.system.login.session;

import com.core.session.Jdf3SessionManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * <p>
 * ClassName: SessionManage
 * </p>
 * <p>
 * Description: Session管理器
 * </p>
 * <p>
 * Author: Administrator
 * </p>
 * <p>
 * Date: 2016-5-17
 * </p>
 */
public class SessionManage extends Jdf3SessionManage {
    /**
     * 日志信息
     */
    private static Logger logger = LoggerFactory.getLogger(SessionManage.class);
    /**
     * session管理器
     */
    private static SessionManage sessionManage;

    /**
     * 
     * <p>
     * Description: 私有构造器
     * </p>
     */
    public SessionManage() {
        super();
        logger.info("create SessionManage...");
        sessionManage = this;
    }

    /**
     * 获取所有的用户session集合
     * 
     * @return 所有的用户session信息
     */
    public Map<String, HttpSession> getUserSessions() {
        return super.userSessions;
    }

    /**
     * 
     * <p>
     * Description: 根据当前session获取用户session
     * </p>
     * 
     * @param session 当前session
     * @return 用户session信息
     */
    public UserSession getUserSession(HttpSession session) {
        HttpSession s;
        s = this.userSessions.get(session.getId());
        if (s != null) {
            UserSession userSession;
            userSession = (UserSession) s.getAttribute(Jdf3SessionManage.USERSESSION);
            if (userSession == null) {
                session.invalidate();
            }
            return userSession;
        }
        return null;
    }

    /**
     * 获取session管理器
     * 
     * @return session管理器
     */
    public static SessionManage getSessionManage() {
        if (sessionManage == null) {
            sessionManage = new SessionManage();
        }
        return sessionManage;
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        logger.info("Session created:" + se.getSession().getId());
        UserSession userSession;
        userSession = new UserSession(se.getSession());
        se.getSession().setAttribute(Jdf3SessionManage.USERSESSION, userSession);
        this.userSessions.put(se.getSession().getId(), se.getSession());
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        this.userSessions = new ConcurrentHashMap<>();
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.info("Session Destroyed:" + se.getSession().getId());
        String sessionId;
        sessionId = se.getSession().getId();
        UserSession userSession;
        userSession = (UserSession) se.getSession().getAttribute(Jdf3SessionManage.USERSESSION);
        if (userSession != null && userSession.getLoginState() == LoginState.LOGIN) {
            userSession.setLoginState(LoginState.LOGOUT);
            se.getSession().removeAttribute(Jdf3SessionManage.USERSESSION);
        }
        this.userSessions.remove(sessionId);
    }

    /**
     * 
     * <p>
     * Description: 获取挡墙用户数量
     * </p>
     */
    public void getLoginedUser() {
        int loginUserNumber;
        loginUserNumber = this.userSessions.size();
        logger.debug("当前登录人数:" + loginUserNumber);
    }

}
