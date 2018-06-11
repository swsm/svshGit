package com.swsm.log.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.core.action.EntityHandler;
import com.core.action.HandlerResult;
import com.core.service.IEntityService;
import com.core.tools.*;
import com.core.tools.file.ExcelUtil;
import com.core.tools.format.DateUtil;
import com.swsm.log.service.IOperateLogService;
import com.swsm.login.session.UserSession;
import com.swsm.log.model.OperateLog;
import com.swsm.login.session.SessionManage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * ClassName: OperateLogAction
 * </p>
 * <p>
 * Description: 操作日志表的页面交互处理Action，继承自EntityHandler
 * </p>
 */
@Controller
@RequestMapping("/main/log")
public class OperateLogAction extends EntityHandler<OperateLog> {
    @Override
    @Autowired
    @Qualifier("operateLogService")
    public void setEntityServiceImpl(IEntityService<OperateLog> entityServiceImpl) {
        this.entityServiceImpl = entityServiceImpl;
        this.clazz = OperateLog.class;
    }
    
    /**
     * 操作日志OperateLogService
     */
    @Autowired
    @Qualifier("operateLogService")
    private IOperateLogService operateLogService;
    
    /**
     * <p>Description: 查询操作日志信息</p>
     * @param request 请求
     * @param response 响应
     */
    @RequestMapping(value = "/getOperateLog.mvc")
    @ResponseBody
    public void getOperateLog(HttpServletRequest request, HttpServletResponse response) {
        //构建过滤器
        Filter filter;
        filter = createFilter(request);
        List<OperateLog> list;
        list = this.operateLogService.getOperateLog(filter);
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
        if (StringUtils.isNotEmpty(request.getParameter("logContent"))) {
            queryMap.put("logContent", new Query(Condition.LIKE, request.getParameter("logContent")));
        }
        if (StringUtils.isNotEmpty(request.getParameter("operationUser"))) {
            queryMap.put("operationUser", new Query(Condition.LIKE, request.getParameter("operationUser")));
        }
        if (StringUtils.isNotEmpty(request.getParameter("operateIp"))) {
            queryMap.put("operateIp", new Query(Condition.LIKE, request.getParameter("operateIp")));
        }
        
        filter.setQueryMap(queryMap);
        return filter;
    }
    
    /**
     * <p>Description: 新增操作日志</p>
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/insertOperateLog.mvc")
    @ResponseBody
    public void insertOperateLog(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String logContents;
        logContents = WebUtil.fetch(request, "logContents");
        //转化成List<Map>形式
        List<Map> list;
        list = this.getListByString(logContents);
        SessionManage sessionManage;
        sessionManage = SessionManage.getSessionManage();
        UserSession userSession;
        userSession = sessionManage.getUserSession(request.getSession());
        InetAddress inetAddress;
        inetAddress = InetAddress.getLocalHost();
        String localIp;
        localIp = inetAddress.getHostAddress();
        String modual;
        modual = request.getParameter("modual");
//        this.operateLogService.insertOperateLog(localIp, modual, userSession.getDispName(),
//                userSession.getUserId(), list);
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, HandlerResult.SUCCESS));
    }
    
    /**
     * <p>Description: 有StringJSON获取List</p>
     * @param rows String
     * @return List
     */
    @SuppressWarnings("rawtypes")
    private List<Map> getListByString(String rows) {
        JSONArray array;
        array = JSON.parseArray(rows);
        //转化成List<Map>形式
        List<Map> list;
        list = new LinkedList<Map>();
        if (array != null) {
            for (int i = 0; i < array.size(); i++) {
                Map rltMap;
                rltMap = (Map) JSON.parse(array.get(i).toString());
                list.add(rltMap);
            }
        }
        return list;
    }
    
    /**
     * <p>Description: 导出日志信息</p>
     * @param request 请求对象
     * @param response 返回对象
     * @throws Exception 异常
     */
    @SuppressWarnings("static-access")
    @ResponseBody
    @RequestMapping(value = "/exportData.mvc")
    public void exportData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String[] columnNames = null;
        String[] fileNames = null;
        Filter filter;
        filter = this.doFilter(request, this.getQueryMap(request));
        filter.getPageInfo().start = 0;
        filter.getPageInfo().limit = 65535;
        List<OperateLog> list;
        list = this.operateLogService.getOperateLog(filter);
        columnNames = new String[] { "操作时间", "操作用户", "用户IP", "模块名称", "日志内容" };
        fileNames = new String[] { "createDate", "username", "remoteIp", "modual", "logContent" };
        ExcelUtil.exportExcel(response, list, OperateLog.class, columnNames, fileNames,
                "操作日志" + dateFormat.format(new Date()));
    }
    
    /**
     * 
     * <p>Description: 组装queryMap</p>
     * @param request 请求对象
     * @return queryMap
     */
    private Map<String, Query> getQueryMap(HttpServletRequest request) {
        //用户名
        Map<String, Query> queryMap;
        queryMap = new HashMap<String, Query>();
        try {
            String queryStartDate;
            queryStartDate = URLDecoder.decode(request.getParameter("queryStartDate").replace("T", " "), "utf-8");
            if (StringUtils.isNotEmpty(queryStartDate)) {
                queryMap.put("startDate", new Query(Condition.GE, DateUtil.getDefaultDateTime(queryStartDate)));
            }
            String queryEndDate;
            queryEndDate = URLDecoder.decode(request.getParameter("queryEndDate").replace("T", " "), "utf-8");
            if (StringUtils.isNotEmpty(queryEndDate)) {
                queryMap.put("endDate", new Query(Condition.LE, DateUtil.getDefaultDateTime(queryEndDate)));
            }
            String modual;
            modual = URLDecoder.decode(request.getParameter("modual"), "utf-8");
            if (StringUtils.isNotEmpty(modual)) {
                queryMap.put("modual", new Query(Condition.LIKE, modual));
            }
            String logContent;
            logContent = URLDecoder.decode(request.getParameter("logContent"), "utf-8");
            if (StringUtils.isNotEmpty(logContent)) {
                queryMap.put("logContent", new Query(Condition.LIKE, logContent));
            }
            String operationUser;
            operationUser = URLDecoder.decode(request.getParameter("operationUser"), "utf-8");
            if (StringUtils.isNotEmpty(operationUser)) {
                queryMap.put("operationUser", new Query(Condition.LIKE, operationUser));
            }
            String operateIp;
            operateIp = URLDecoder.decode(request.getParameter("operateIp"), "utf-8");
            if (StringUtils.isNotEmpty(operateIp)) {
                queryMap.put("operateIp", new Query(Condition.LIKE, operateIp));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return queryMap;
    }
}
