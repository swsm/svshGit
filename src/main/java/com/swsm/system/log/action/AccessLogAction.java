/**
 * AccessLogAction.java
 * Created at 2016-5-30
 * Created by Codegen
 * Copyright (C) 2015 BROADTEXT SOFTWARE, All rights reserved.
 */
package com.swsm.system.log.action;

import com.core.action.EntityHandler;
import com.core.action.HandlerResult;
import com.core.service.IEntityService;
import com.core.tools.Condition;
import com.core.tools.Filter;
import com.core.tools.MesBaseUtil;
import com.core.tools.Query;
import com.core.tools.format.DateUtil;
import com.swsm.system.log.model.AccessLog;
import com.swsm.system.log.service.IAccessLogService;
import com.swsm.system.login.session.SessionManage;
import com.swsm.system.login.session.UserSession;
import com.swsm.system.system.service.ILoginInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * ClassName: AccessLogAction
 * </p>
 * <p>
 * Description: 访问日志表的页面交互处理Action，继承自EntityHandler
 * </p>
 */
@Controller
@RequestMapping("/main/log")
public class AccessLogAction extends EntityHandler<AccessLog> {
    
    /**
     * 访问日志AccessLogService
     */
    @Autowired
    @Qualifier("accessLogService")
    private IAccessLogService accessLogService;
    
    /**
     * ILoginInfoService
     */
    @Autowired
    @Qualifier("loginInfoService")
    private ILoginInfoService loginInfoService;
    
    @Override
    @Autowired
    @Qualifier("accessLogService")
    public void setEntityServiceImpl(IEntityService<AccessLog> entityServiceImpl) {
        this.entityServiceImpl = entityServiceImpl;
        this.clazz = AccessLog.class;
    }
    
    /**
     * <p>Description: 查询访问日志信息</p>
     * @param request 请求
     * @param response 响应
     */
    @RequestMapping(value = "/getAccessLog.mvc")
    @ResponseBody
    public void getAccessLog(HttpServletRequest request, HttpServletResponse response) {
        //构建过滤器
        Filter filter;
        filter = createFilter(request);
        List<AccessLog> list;
        list = this.accessLogService.getAccessLog(filter);
        MesBaseUtil.writeJsonStr(response, this.toJsonStrForGrid(list, filter.getPageInfo(),
                getJsonExcludedProperties()));
    }
    
    /**
     * <p>Description: 建立filter </p>
     * @param request 请求
     * @return 过滤器
     */
    private Filter createFilter(HttpServletRequest request) {
        Filter filter;
        filter = new Filter();
        String start;
        start = request.getParameter("start");
        String limit;
        limit = request.getParameter("limit");
        if (start != null) {
            filter.getPageInfo().start = Integer.parseInt(start);
        }
        if (limit != null) {
            filter.getPageInfo().limit = Integer.parseInt(limit);
        }
        //构建查询条件
        Map<String, Query> queryMap;
        queryMap = new HashMap<String, Query>();
        if (StringUtils.isNotEmpty(request.getParameter("queryStartDate"))) {
            queryMap.put(
                    "startDate",
                    new Query(Condition.GE, DateUtil.getDefaultDateTime(request.getParameter("queryStartDate")
                            .replace("T", " "))));
        }
        if (StringUtils.isNotEmpty(request.getParameter("queryEndDate"))) {
            queryMap.put(
                    "endDate",
                    new Query(Condition.LE, DateUtil.getDefaultDateTime(request.getParameter("queryEndDate")
                            .replace("T", " "))));
        }
        if (StringUtils.isNotEmpty(request.getParameter("modual"))) {
            queryMap.put("modual", new Query(Condition.LIKE, request.getParameter("modual")));
        }
        if (StringUtils.isNotEmpty(request.getParameter("logConten"))) {
            queryMap.put("logConten", new Query(Condition.LIKE, request.getParameter("logConten")));
        }
        filter.setQueryMap(queryMap);
        return filter;
    }
    
    /**
     * <p>Description: 新增访问日志</p>
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/insertAccessLog.mvc")
    @ResponseBody
    public void insertAccessLog(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionManage sessionManage;
        sessionManage = SessionManage.getSessionManage();
        UserSession userSession;
        userSession = sessionManage.getUserSession(request.getSession());
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
        String modual;
        modual = request.getParameter("modual");
        String logConten;
        logConten = request.getParameter("logConten");
        this.accessLogService.insertAccessLog(ipAddress, modual, logConten, userSession.getDispName(),
                userSession.getUserId());
        //2017-3-6:更新登录用户实时访问的模块
        this.loginInfoService.updateLoginInfo(userSession.getUserName(), modual);
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, HandlerResult.SUCCESS));
    }
}
