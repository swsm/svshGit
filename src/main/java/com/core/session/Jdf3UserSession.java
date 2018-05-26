/**
 * UserSession.java
 * Created at 2016-5-5
 * Created by Tangsanlin
 * Copyright (C) 2016 BROADTEXT SOFTWARE, All rights reserved.
 */
package com.core.session;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Date;


/**
 * 
 * <p>
 * ClassName: UserSession
 * </p>
 * <p>
 * Description: 定义用户session类
 * </p>
 */
public class Jdf3UserSession implements Serializable {
    /**
     * 版本号
     */
    private static final long serialVersionUID = 184146177423505956L;
    /**
     * sessionId
     */
    private String sessionId;
    /**
     * 登录用户名
     */
    private String loginName;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 显示用户名称
     */
    private String dispName;
    /**
     * 工号
     */
    private String workNo;
    /**
     * 用户所属机构ID，直接结构，暂时认为一个用户只有一个机构
     */
    private String organId;
    /**
     * 用户所属机构名称
     */
    private String organName;
    /**
     * 用户所属机构编码
     */
    private String organCode;
    /**
     * 用户拥有的权限对饮的res_code集合
     */
    private String[] havResCodes;
    /**
     * 用户对饮的角色ID集合
     */
    private String[] havRoleIds;
    /**
     * 用户记录ID，表示用户在系统的系统标志，相当于pk_id
     */
    private String userId;
    /**
     * 用户登录的时间
     */
    private Date loginTime;

    /**
     * 
     * <p>
     * Description: 根据session实例化用户UserSession
     * </p>
     * 
     * @param session httpsession
     */
    public Jdf3UserSession(HttpSession session) {
        this.sessionId = session.getId();
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getLoginName() {
        return this.loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDispName() {
        return this.dispName;
    }

    public void setDispName(String dispName) {
        this.dispName = dispName;
    }

    public String getWorkNo() {
        return this.workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }

    public String getOrganId() {
        return this.organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }

    public String getOrganName() {
        return this.organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getOrganCode() {
        return this.organCode;
    }

    public void setOrganCode(String organCode) {
        this.organCode = organCode;
    }

    public String[] getHavResCodes() {
        return this.havResCodes;
    }

    public void setHavResCodes(String[] havResCodes) {
        this.havResCodes = havResCodes;
    }

    public String[] getHavRoleIds() {
        return this.havRoleIds;
    }

    public void setHavRoleIds(String[] havRoleIds) {
        this.havRoleIds = havRoleIds;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getLoginTime() {
        return this.loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

}
