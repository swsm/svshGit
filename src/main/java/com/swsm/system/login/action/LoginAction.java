package com.swsm.system.login.action;

import com.alibaba.fastjson.JSON;
import com.core.exception.BaseException;
import com.core.session.Jdf3SessionManage;
import com.core.tools.MD5Util;
import com.swsm.system.constant.ActionConstants;
import com.swsm.system.constant.CommonConstants;
import com.swsm.system.log.service.IAccessLogService;
import com.swsm.system.login.LoginUtil;
import com.swsm.system.login.service.ILoginService;
import com.swsm.system.login.session.LoginResult;
import com.swsm.system.login.session.LoginState;
import com.swsm.system.login.session.SessionManage;
import com.swsm.system.login.session.UserSession;
import com.swsm.system.main.service.IMainService;
import com.swsm.system.main.vo.ProductModel;
import com.swsm.system.product.ProductUtil;
import com.swsm.system.system.model.Organ;
import com.swsm.system.system.model.Resource;
import com.swsm.system.system.model.Role;
import com.swsm.system.system.model.User;
import com.swsm.system.system.service.ILoginInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.util.*;

/**
 * 
 * <p>
 * ClassName: LoginAction
 * </p>
 * <p>
 * Description: 用户登录
 * </p>
 */
@Controller
public class LoginAction {
    /**
     * 日志信息
     */
    private static Logger logger = LoggerFactory.getLogger(LoginAction.class);
    /**
     * 登录服务
     */
    @Autowired
    @Qualifier("loginService")
    private ILoginService loginService;
    /**
     * mainService服务
     */
    @Autowired
    @Qualifier("mainService")
    private IMainService mainService;

    /**
     * IAccessLogService服务
     */
    @Autowired
    @Qualifier("accessLogService")
    private IAccessLogService accessLogService;

    /**
     * ILoginInfoService服务
     */
    @Autowired
    @Qualifier("loginInfoService")
    private ILoginInfoService loginInfoService;

    /**
     * 
     * <p>
     * Description: 校验用户是否已经被登录
     * </p>
     * 
     * @param request 请求对象
     * @param response 返回对象
     * @return 校验结果
     * @throws Exception 异常
     */
    @ResponseBody
    @RequestMapping(value = "/main/checkUserIsLogin.mvc")
    public boolean checkUserIsLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userName = request.getParameter("loginName");
        String localIp = this.getIpAddress(request);
        return this.loginInfoService.checkUserIsLogin(userName, localIp);
    }

    /**
     * 
     * <p>
     * Description: 加载用户Session信息
     * </p>
     * 
     * @param user 登录用户信息
     * @param userSession 用户ssession
     * @param request 请求信息
     */
    private void loadUserSession(User user, UserSession userSession, HttpServletRequest request) {
        HttpSession httpSession;
        httpSession = request.getSession(false);

        httpSession.setAttribute("sys.user", user.getId());
        httpSession.setAttribute("sys.userName", user.getUsername());
        httpSession.setAttribute("sys.dispName", user.getTruename());
        httpSession.setAttribute("sys.workNo", user.getWorkNo());

        userSession.setUserId(user.getId());
        userSession.setDispName(user.getTruename());
        userSession.setLoginName(user.getUsername());
        userSession.setLoginTime(new Date());
        userSession.setUserName(user.getUsername());
        userSession.setWorkNo(user.getWorkNo());
        Set<Organ> organSet;
        organSet = user.getOrganList();
        if (organSet.size() > 0) {
            Organ organ;
            organ = organSet.toArray(new Organ[0])[0];
            userSession.setOrganId(organ.getId());
            userSession.setOrganCode(organ.getOrganCode());
            userSession.setOrganName(organ.getOrganName());
            httpSession.setAttribute("sys.organId", organ.getId());
            httpSession.setAttribute("sys.organCode", organ.getOrganCode());
            httpSession.setAttribute("sys.organName", organ.getOrganName());
        }
        Set<Role> roles;
        roles = user.getRoleList();
        String[] roleIds;
        roleIds = new String[roles.size()];
        int i = 0;
        for (Role r : roles) {
            roleIds[i] = r.getId();
            i++;
        }
        userSession.setHavRoleIds(roleIds);
        try {
            Resource[] res;
            res = this.mainService.getHavResByLoginName(user.getUsername());
            String[] resCodes;
            resCodes = new String[res.length];
            for (i = 0; i < res.length; i++) {
                resCodes[i] = res[i].getResCode();
            }
            userSession.setHavResCodes(resCodes);
            userSession.setLoginState(LoginState.LOGIN);
        } catch (BaseException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    /**
     * kill掉session中存在userId的session
     * 
     * @param currentSession 用户session
     * @param userId 用户ID
     */
    private void killExistsSession(HttpSession currentSession, String userId) {
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
                this.loginInfoService.logOffUser(userId, CommonConstants.LOGOFF_TAG_DISPLACE);
                if (!currentSession.getId().equals(session.getId())) {
                    session.removeAttribute(SessionManage.USERSESSION);
                }
            }
        }
    }
    
    
   /**
    * 
    * <p>Description: 使用工卡登录时校验用户是否已登录</p>
    * @param request 请求对象 
    * @param response 返回对象
    * @return 校验结果  true:用户已登录
    * @throws Exception 异常
    */
    @ResponseBody
    @RequestMapping(value = "/main/checkUserLoginFlag.mvc")
    public  String checkUserLoginFlag(@RequestParam String workNo, HttpServletRequest request, HttpServletResponse response) throws Exception {     
        String userName = request.getSession().getAttribute("sys.userName").toString();
        List<Map<String, Object>> userInfo;
        userInfo = this.loginInfoService.getUserInfoByUserName(userName);
        if (userInfo.size() == 0)
            return "false";
        return "true";
    }
    /**
     * 用户登录，
     * 
     * @param request 请求信息
     * @param response 响应信息
     * @return 登录信息
     * @throws Exception 异常
     */
    @RequestMapping(value = "/main/login.mvc")
    @ResponseBody
    public Map<String, String> login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView view = new ModelAndView();
        String loginName = request.getParameter("loginName");
        String loginPassword = request.getParameter("loginPassword");
        User user = this.loginService.findUser(loginName);
        Map<String, String> map = new HashMap<>();
        if (user == null) {
            map.put("msg", LoginResult.LOGINNAMENOTFOUND.getContext());
            return map;
        }
        String password = user.getPassword();
        String pass = MD5Util.getDigest(loginPassword);
        if (!pass.equals(password)) {
            view.setViewName("login");
            view.addObject("msg", LoginResult.ERRORPASSWORD.getContext());
            if (!ActionConstants.ADMIN_USER.equals(loginName)) {
                map.put("msg", LoginResult.ERRORPASSWORD.getContext());
                return map;
            }
        }
        String[] resCodes = new String[0];
        LoginUtil.loadSession(resCodes, user, user.getOrganList(), user.getRoleList(), request);
        map.put("msg", LoginResult.LOGINOK.getContext());
        String localIp = this.getIpAddress(request);
        this.accessLogService.insertAccessLog(localIp, "登录", "登录系统", user.getTruename(), user.getId());
        this.loginInfoService.saveLoginInfo(loginName, localIp);
        return map;
    }

    /**
     * 
     * <p>
     * Description: 工卡登录
     * </p>
     * 
     * @param request 请求信息
     * @param workNo 响应信息
     * @return 登录信息
     * @throws Exception 异常
     */
    @RequestMapping(value = "/main/workNoLogin.mvc")
    public @ResponseBody
    Map<String, Object> workNoLogin(@RequestParam String workNo, HttpServletRequest request) throws Exception {
        Map<String, Object> jsonResponse;
        jsonResponse = new HashMap<String, Object>();
        User user = loginService.getUserByWorkNo(workNo);
        if (user == null) {
            jsonResponse.put("msg", LoginResult.WORKNONOTFOUND.getContext());
            return jsonResponse;
        }
        HttpSession currentSession;
        currentSession = request.getSession();
        UserSession userSession;
        userSession = SessionManage.getSessionManage().getUserSession(currentSession);
        if (userSession == null) {
            userSession = new UserSession(currentSession);
        }
        loadUserSession(user, userSession, request);
        currentSession.setAttribute(Jdf3SessionManage.USERSESSION, userSession);

        jsonResponse.put("msg", LoginResult.LOGINOK.getContext());

        return jsonResponse;
    }
    
    
    @RequestMapping("/enterLogin.mvc")
    public String enterLogin(HttpServletRequest request, HttpSession session) throws Exception {
        String localIp;
        localIp = this.getIpAddress(request);
        SessionManage.getSessionManage().getUserSessions().put(session.getId(), session);
        killExistsSession(session, session.getAttribute("sys.user").toString());
        this.accessLogService.insertAccessLog(localIp, "登录", "登录系统", "登录确认", session.getAttribute("sys.dispName")
                .toString());
        this.loginInfoService.saveLoginInfo(session.getAttribute("sys.userName").toString(), localIp);
        return "redirect:forwardPage.mvc?page=index";
    }
    /**
     * <p>
     * Description: 得到客户端IP
     * </p>
     * 
     * @param request 请求信息
     * @return ip
     * @throws Exception 异常
     */
    private String getIpAddress(HttpServletRequest request) throws Exception {
        String ipAddress;
        ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if ("127.0.0.1".equals(ipAddress)) {
                InetAddress inet;
                inet = InetAddress.getLocalHost();
                ipAddress = inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割   
        if (ipAddress != null && ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }
        return ipAddress;
    }

    /**
     * 
     * <p>
     * Description: 注销登录
     * </p>
     * 
     * @param request 请求信息
     * @param response 响应信息
     * @return view 登录页面
     * @throws Exception 异常
     */
    @RequestMapping(value = "/main/loginOut.mvc")
    @ResponseBody
    public Map<String, String> loginOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionManage sessionManage;
        sessionManage = SessionManage.getSessionManage();
        Map<String, HttpSession> sessions;
        sessions = sessionManage.getUserSessions();
        HttpSession currentSession;
        currentSession = request.getSession(false);
        UserSession userSession;
        userSession = sessionManage.getUserSession(currentSession);
        for (Map.Entry<String, HttpSession> entry : sessions.entrySet()) {
            HttpSession session;
            session = entry.getValue();
            if (currentSession.getId().equals(session.getId())) {
                session.removeAttribute(SessionManage.USERSESSION);
            }
        }
        Map<String, String> map;
        map = new HashMap<>();
        map.put("msg", LoginState.LOGOUT.getContext());
        String localIp;
        localIp = this.getIpAddress(request);
        this.accessLogService
                .insertAccessLog(localIp, "退出", "退出系统", userSession.getDispName(), userSession.getUserId());
        //2017-3-6:注销登录用户
        this.loginInfoService.logOffUser(userSession.getUserId(), CommonConstants.LOGOFF_TAG_NORMAL);
        return map;
    }

    /**
     * 
     * <p>
     * Description: 跳转到对应的页面
     * </p>
     * 
     * @param request 请求信息
     * @param response 响应信息
     * @return view 登录页面
     * @throws Exception 异常
     */
    @RequestMapping(value = "/forwardPage.mvc")
    public ModelAndView forwardPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView view;
        view = new ModelAndView();
        String page;
        page = request.getParameter("page");
        if (page == null) {
            page = "login";
        } else {
            if ("index".equals(page)) {
                UserSession userSession = SessionManage.getSessionManage().getUserSession(request.getSession(false));
                view.addObject("displayName", userSession.getDispName());
                view.addObject("organName", userSession.getOrganName());
                view.addObject("userName", userSession.getUserName());
                view.addObject("workNo", userSession.getWorkNo());
                ProductModel product = null;
                try {
                    product = ProductUtil.getProduct();
                    if (product == null) {
                        product = new ProductModel();
                    }
                    String productJson = JSON.toJSONString(product);
                    Map<String, String> varMap = product.getVarMap();
                    if (varMap != null) {
                        for (Map.Entry<String, String> entry : varMap.entrySet()) {
                            userSession.putVar(entry.getKey(), entry.getValue());
                        }
                    }
                    view.addObject("product", productJson);
                } catch (BaseException e) {
                    logger.warn(e.getMessage());
                }
            }
        }
        view.setViewName(page);
        return view;
    }
    /**
     * 
     * <p>
     * Description: 校验用户登录的状态
     * </p>
     * 
     * @param request 请求对象
     * @return 校验结果 0:ok;(1.正常注销；2.session过期；)3.账号二次登录踢出；4.管理界面踢出；
     * @throws Exception 异常
     */
    @ResponseBody
    @RequestMapping(value = "/main/checkLoginStatus.mvc")
    public String checkLoginStatus(HttpServletRequest request) throws Exception {
        String userName;
        userName = request.getParameter("userName");
        this.loginInfoService.checkAllSession();
        String localIp;
        localIp = this.getIpAddress(request);
        if (userName == null || userName.isEmpty()) {
            return "0";
        }
        User user = this.loginService.findUser(userName);
        if(user !=null && "0".equals(user.getEnabled())){
            return "5";
        }
        return this.loginInfoService.checkLoginStatus(userName, localIp);
    }
}
