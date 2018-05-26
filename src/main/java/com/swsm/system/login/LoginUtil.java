package com.swsm.system.login;

import com.core.tools.SpringUtil;
import com.swsm.system.constant.CommonConstants;
import com.swsm.system.login.session.LoginState;
import com.swsm.system.login.session.SessionManage;
import com.swsm.system.login.session.UserSession;
import com.swsm.system.system.model.Organ;
import com.swsm.system.system.model.Role;
import com.swsm.system.system.model.User;
import com.swsm.system.system.service.ILoginInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;
import java.util.Set;


/**
 * <p>
 * ClassName: LoginUtil
 * </p>
 * <p>
 * Description: 登录相关的方法
 * </p>
 */
public class LoginUtil {

    /**
     * 日志信息
     */
    private static Logger logger = LoggerFactory.getLogger(LoginUtil.class);

    /**
     * 
     * <p>
     * Description: 加载session信息
     * </p>
     * 
     * @param resCodes 页面资源数组
     * @param user 用户
     * @param organSet 机构
     * @param roles 角色
     * @param request 请求对象
     * @param response 返回对象
     */
    public static void loadSession(String[] resCodes, User user, Set<Organ> organSet, Set<Role> roles,
                                   HttpServletRequest request, HttpServletResponse response) {
        HttpSession currentSession;
        currentSession = request.getSession(true);
        UserSession userSession;
        userSession = SessionManage.getSessionManage().getUserSession(currentSession);
        if (userSession == null) {
            userSession = new UserSession(currentSession);
            currentSession.setAttribute(SessionManage.USERSESSION, userSession);
            SessionManage.getSessionManage().getUserSessions().put(currentSession.getId(), currentSession);
        }
        loadUserSession(resCodes, user, organSet, roles, userSession, request);
        logger.info("loadUserSession success!");
        killExistsSession(currentSession, user.getId(), request);
    }

    /**
     * 
     * <p>Description: 包含该cookie</p>
     * @param request 请求对象
     * @param cookie cookie
     * @return true：包含
     */
    public static boolean containCookie(HttpServletRequest request, Cookie cookie) {
        Cookie[] cookies;
        cookies = request.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals(cookie.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * <p>Description: 获取token</p>
     * @param request 请求对象
     * @return 返回token
     */
    public static String getToken(HttpServletRequest request) {
        return request.getParameter("token");
    }

    /**
     * 
     * <p>
     * Description: 加载用户Session信息
     * </p>
     * 
     * @param resCodes 登录用户所有页面资源
     * @param user 登录用户信息
     * @param organSet 登录用户所属机构
     * @param roles 登录用户角色
     * @param userSession 用户ssession
     * @param request 请求信息
     */
    public static void loadUserSession(String[] resCodes, User user, Set<Organ> organSet, Set<Role> roles,
            UserSession userSession, HttpServletRequest request) {
        userSession.setUserId(user.getId());
        userSession.setDispName(user.getTruename());
        userSession.setLoginName(user.getUsername());
        userSession.setLoginTime(new Date());
        userSession.setUserName(user.getUsername());
        userSession.setWorkNo(user.getWorkNo());
        if (organSet.size() > 0) {
            Organ organ;
            organ = organSet.toArray(new Organ[0])[0];
            userSession.setOrganId(organ.getId());
            userSession.setOrganCode(organ.getOrganCode());
            userSession.setOrganName(organ.getOrganName());
        }
        String[] roleIds;
        roleIds = new String[roles.size()];
        int i = 0;
        for (Role r : roles) {
            roleIds[i] = r.getId();
            i++;
        }
        userSession.setHavRoleIds(roleIds);
        userSession.setHavResCodes(resCodes);
        userSession.setLoginState(LoginState.LOGIN);
    }

    /**
     * kill掉session中存在userId的session
     * 
     * @param currentSession 用户session
     * @param userId 用户ID
     * @param request 请求对象
     */
    public static void killExistsSession(HttpSession currentSession, String userId, HttpServletRequest request) {
        SessionManage sessionManage;
        sessionManage = SessionManage.getSessionManage();
        Map<String, HttpSession> sessions;
        sessions = sessionManage.getUserSessions();
        for (Map.Entry<String, HttpSession> entry : sessions.entrySet()) {
            HttpSession session;
            session = entry.getValue();
            UserSession userSesssion;
            userSesssion = sessionManage.getUserSession(session);
            if (userSesssion != null && userId.equals(userSesssion.getUserId())) {
                //2017-3-4:注销登录用户
                ILoginInfoService loginInfoService;
                loginInfoService = (ILoginInfoService) SpringUtil.getBean(request, "loginInfoService");
                loginInfoService.logOffUser(userId, CommonConstants.LOGOFF_TAG_DISPLACE);
                if (!currentSession.getId().equals(session.getId())) {
                    session.removeAttribute(SessionManage.USERSESSION);
                }
            }
        }
    }
}