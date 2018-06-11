package com.swsm.log.action;

import com.alibaba.fastjson.JSON;
import com.core.action.EntityHandler;
import com.core.service.IEntityService;
import com.core.tools.*;
import com.core.tools.format.DateUtil;
import com.swsm.log.model.ExceptionLog;
import com.swsm.log.service.IExceptionLogService;
import com.swsm.system.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>ClassName: ExceptionLogAction</p>
 * <p>Description: 异常日志表的页面交互处理Action，继承自EntityHandler</p>
 */
@Controller
@RequestMapping("/main/exceptionLog")
public class ExceptionLogAction extends EntityHandler<ExceptionLog> {
    
    /**
     * 异常日志exceptionLogService
     */
    @Autowired
    @Qualifier("exceptionLogService")
    private IExceptionLogService exceptionLogService;

    @Override
    @Autowired
    @Qualifier("exceptionLogService")
    public void setEntityServiceImpl(IEntityService<ExceptionLog> entityServiceImpl) {
        this.entityServiceImpl = entityServiceImpl;
        this.clazz = ExceptionLog.class;
    }

    
    /**
     * <p>Description: 查询异常日志信息</p>
     * @param request 请求
     * @param response 响应
     */
    @RequestMapping(value = "/getExceptionLog.mvc")
    @ResponseBody
    public void getExceptionLog(HttpServletRequest request, HttpServletResponse response) {
        //构建过滤器
        Filter filter;
        filter = createFilter(request);
        List<ExceptionLog> list;
        list = this.exceptionLogService.getExceptionLog(filter);
        MesBaseUtil.writeJsonStr(response, this.toJsonStrForGrid(list, filter.getPageInfo(),
                getJsonExcludedProperties()));
    }
    
    /**
     * <p>Description: 根据id查询异常日志内容</p>
     * @param request 请求
     * @param response 响应
     */
    @RequestMapping(value = "/getExceptionLogConten.mvc")
    @ResponseBody
    public void getExceptionLogConten(HttpServletRequest request, HttpServletResponse response) {
        //构建过滤器
        Filter filter;
        filter = createFilter(request);
        String id;
        id = request.getParameter("id");
        List<ExceptionLog> list;
        list = this.exceptionLogService.getExceptionLogConten(id);
        MesBaseUtil.writeJsonStr(response, this.toJsonStrForGrid(list, filter.getPageInfo(), 
                getJsonExcludedProperties()));
    }
    
    /**
     * <p>Description: 建立filter </p>
     * @param request 请求
     * @return 过滤器
     */
    @SuppressWarnings("unchecked")
    private Filter createFilter(HttpServletRequest request) {
        Filter filter;
        filter = new Filter();
        String start;
        start = request.getParameter("start");
        String limit;
        limit = request.getParameter("limit");
        String params;
        params = request.getParameter("queryParams");
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap = (Map<String, Object>) JSON.parse(params);
        if (start != null) {
            filter.getPageInfo().start = Integer.parseInt(start);
        }
        if (limit != null) {
            filter.getPageInfo().limit = Integer.parseInt(limit);
        }
        //构建查询条件
        Map<String, Query> queryMap;
        queryMap = new HashMap<String, Query>();
        if (StringUtils.isNotEmpty(String.valueOf(paramsMap.get("exceptionType")))) {
            queryMap.put("exceptionType", new Query(Condition.EQ, String.valueOf(paramsMap.get("exceptionType"))));
        }
        if (StringUtils.isNotEmpty(String.valueOf(paramsMap.get("exceptionReason")))) {
            queryMap.put("exceptionReason", new Query(Condition.LIKE, String.valueOf(paramsMap.get("exceptionReason"))));
        }
        if (StringUtils.isNotEmpty(String.valueOf(paramsMap.get("remoteIp")))) {
            queryMap.put("remoteIp", new Query(Condition.LIKE, String.valueOf(paramsMap.get("remoteIp"))));
        }
        if (StringUtils.isNotEmpty(String.valueOf(paramsMap.get("startOccurDate")))) {
            queryMap.put("startOccurDate", new Query(Condition.GE, 
                    DateUtil.getDefaultDateTime(String.valueOf(paramsMap.get("startOccurDate")))));
        }
        if (StringUtils.isNotEmpty(String.valueOf(paramsMap.get("endOccurDate")))) {
            queryMap.put("endOccurDate", new Query(Condition.LE, 
                    DateUtil.getDefaultDateTime(String.valueOf(paramsMap.get("endOccurDate")))));
        }
        filter.setQueryMap(queryMap);
        return filter;
    }
    
    @RequestMapping(value = "/getUserName.mvc")
    @ResponseBody
    public void getUserName(HttpServletRequest request, HttpServletResponse response){
        String userId = WebUtil.fetch(request, "userId");
        User user = this.exceptionLogService.getUserById(userId);
        MesBaseUtil.writeJsonStr(response,user.getUsername());
    }
}
