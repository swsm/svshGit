/**
 * SessionManage.java
 * Created at 2016-5-5
 * Created by Tangsanlin
 * Copyright (C) 2016 BROADTEXT SOFTWARE, All rights reserved.
 */
package com.core.session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




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
public class Jdf3SessionManage implements HttpSessionListener, ServletContextListener {
    /**
     * 日志信息
     */
    private static Logger logger = LoggerFactory.getLogger(Jdf3SessionManage.class);
    /**
     * session标志
     */
    public static final String USERSESSION = "USERSESSION";
    /**
     * 所有的session信息
     */
    protected Map<String, HttpSession> userSessions = new ConcurrentHashMap<String, HttpSession>();
    
    protected static Jdf3SessionManage jdf3SessionManage;
    

    /**
     * 获取所有的用户session集合
     * 
     * @return 所有的用户session信息
     */
    public Map<String, HttpSession> getUserSessions() {
        return this.userSessions;
    }
    
    protected Jdf3SessionManage(){
        logger.info("create Jdf3SessionManage ...");
        jdf3SessionManage=this;
    }
    /**
     * 获取session管理器
     * 
     * @return session管理器
     */
    public static Jdf3SessionManage getJdf3SessionManage() {
        if(jdf3SessionManage==null){
            jdf3SessionManage=new Jdf3SessionManage();
        }
        return jdf3SessionManage;
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
    public Jdf3UserSession getJdf3UserSession(HttpSession session) {
        HttpSession s;
        s = this.userSessions.get(session.getId());
        if (s != null) {
            Jdf3UserSession userSession;
            userSession = (Jdf3UserSession) s.getAttribute(USERSESSION);
            if (userSession == null) {
                session.invalidate();
            }
            return userSession;
        }
        return null;
    }


    @Override
    public void sessionCreated(HttpSessionEvent se) {
        logger.info("Session created:" + se.getSession().getId());
        Jdf3UserSession userSession;
        userSession = new Jdf3UserSession(se.getSession());
        se.getSession().setAttribute(USERSESSION, userSession);
        this.userSessions.put(se.getSession().getId(), se.getSession());
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        this.userSessions = new ConcurrentHashMap<String, HttpSession>();
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.info("Session Destroyed:" + se.getSession().getId());
        String sessionId;
        sessionId = se.getSession().getId();
        Jdf3UserSession userSession;
        userSession = (Jdf3UserSession) se.getSession().getAttribute(Jdf3SessionManage.USERSESSION);
        if (userSession != null) {
            se.getSession().removeAttribute(Jdf3SessionManage.USERSESSION);
        }
        this.userSessions.remove(sessionId);
    }


}
