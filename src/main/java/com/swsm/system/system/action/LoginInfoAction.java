package com.swsm.system.system.action;

import com.alibaba.fastjson.JSON;
import com.core.action.BaseHandler;
import com.core.tools.*;
import com.swsm.constant.CommonConstants;
import com.swsm.login.session.SessionManage;
import com.swsm.login.session.UserSession;
import com.swsm.system.system.model.LoginInfo;
import com.swsm.system.system.service.ILoginInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>ClassName: LoginInfoAction</p>
 * <p>Description: 用户登录的action交互层</p>
 */
@Controller
@RequestMapping("/main/loginInfo")
public class LoginInfoAction extends BaseHandler {

    /**
     * ILoginInfoService
     */
    @Autowired
    @Qualifier("loginInfoService")
    private ILoginInfoService loginInfoService;

    /**
     * 
     * <p>Description: 根据查询条件查询全部登录信息（状态为1，即登录中）</p>
     * @param request 请求对象
     * @param response 返回对象
     * @throws Exception 异常
     */
    @ResponseBody
    @SuppressWarnings({ "unchecked", "static-access" })
    @RequestMapping(value = "/queryLoginInfo.mvc")
    public void queryLoginInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String params;
        params = request.getParameter("queryParams");
        Map<String, Object> paramsMap;
        paramsMap = new HashMap<String, Object>();
        paramsMap = JSON.parseObject(params, Map.class);
        Map<String, Query> queryMap;
        queryMap = this.doQueryList(paramsMap);
        Filter filter;
        filter = this.doFilter(request, queryMap);
        List<LoginInfo> liList;
        liList = this.loginInfoService.queryLoginInfo(filter);
        Map<String, Object> rltMap;
        rltMap = new HashMap<String, Object>();
        rltMap.put("rows", liList);
        rltMap.put("total", filter.getPageInfo().count);
        MesBaseUtil.writeJsonStr(response, JsonUtil.getJsonStringFromMap(rltMap));
    }

    /**
     * 
     * <p>Description: 查询条件组装</p>
     * @param paramsMap 参数Map
     * @return queryMap
     */
    private Map<String, Query> doQueryList(Map<String, Object> paramsMap) {
        Map<String, Query> queryMap;
        queryMap = new HashMap<String, Query>();
        if (StringUtils.isNotEmpty(paramsMap.get("userName").toString())) {
            queryMap.put("userName", new Query(Condition.LIKE, paramsMap.get("userName").toString()));
        }
        if (StringUtils.isNotEmpty(paramsMap.get("trueName").toString())) {
            queryMap.put("trueName", new Query(Condition.LIKE, paramsMap.get("trueName").toString()));
        }
        if (StringUtils.isNotEmpty(paramsMap.get("ipAddress").toString())) {
            queryMap.put("ipAddress", new Query(Condition.LIKE, paramsMap.get("ipAddress").toString()));
        }
        return queryMap;
    }
    
    
    /**
     * 
     * <p>Description: 强制踢出登录用户(注：遇到服务器问题，会发生session中已不存在该用户，但库表中未及时更新的状况)</p>
     * @param request 请求对象
     * @param response 返回对象
     * @return 操作结果
     */
    @ResponseBody
    @RequestMapping(value = "/logOffUser.mvc")
    public boolean logOffUser(HttpServletRequest request, HttpServletResponse response) {
        String userName;
        userName = request.getParameter("userName");
        SessionManage sessionManage;
        sessionManage = SessionManage.getSessionManage();
        Map<String, HttpSession> sessions;
        sessions = sessionManage.getUserSessions();
        UserSession curSession = null;
        for (Map.Entry<String, HttpSession> entry : sessions.entrySet()) {
            HttpSession session;
            session = entry.getValue();
            UserSession userSession;
            userSession = sessionManage.getUserSession(session);
            if (userName.equals(userSession.getUserName())) {
                curSession = userSession;
                session.removeAttribute(SessionManage.USERSESSION);
            }
        }
        if (curSession != null) {
            this.loginInfoService.logOffUser(curSession.getUserId(), CommonConstants.LOGOFF_TAG_REJECT);
        } else {
            this.loginInfoService.logOffUserByUserName(userName, CommonConstants.LOGOFF_TAG_REJECT);
        }
        return true;
    }

}
