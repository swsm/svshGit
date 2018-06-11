/**
 * InterfaceLogAction.java
 * Created at 2017-10-16
 * Created by chenshu
 * Copyright (C) 2017 BROADTEXT SOFTWARE, All rights reserved.
 */
package com.swsm.log.action;

import com.alibaba.fastjson.JSON;
import com.core.action.EntityHandler;
import com.core.service.IEntityService;
import com.core.tools.Condition;
import com.core.tools.Filter;
import com.core.tools.MesBaseUtil;
import com.core.tools.Query;
import com.core.tools.format.DateUtil;
import com.swsm.log.model.InterfaceLog;
import com.swsm.log.service.IInterfaceLogService;
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
 * <p>ClassName: InterfaceLogAction</p>
 * <p>Description: 接口日志页面交互的Action</p>
 * <p>Author: chenshu</p>
 * <p>Date: 2017-10-16</p>
 */
@Controller
@RequestMapping("/main/interfaceLog")
public class InterfaceLogAction extends EntityHandler<InterfaceLog> {

    @Autowired
    @Qualifier("interfaceLogService")
    @Override
    public void setEntityServiceImpl(IEntityService<InterfaceLog> entityServiceImpl) {
        this.entityServiceImpl = entityServiceImpl;
    }

    @Autowired
    @Qualifier("interfaceLogService")
    private IInterfaceLogService interfaceLogService;
    
    @RequestMapping(value = "/getInterfaceLog.mvc")
    @ResponseBody
    public void getInterfaceLog(HttpServletRequest request,HttpServletResponse response){
        Filter filter;
        filter = createFilter(request);
        List<InterfaceLog> list;
        list = this.interfaceLogService.getInterfaceLog(filter);
        MesBaseUtil.writeJsonStr(response, this.toJsonStrForGrid(list, filter.getPageInfo(),
                getJsonExcludedProperties()));
    }
    
    @SuppressWarnings("unchecked")
    private Filter createFilter(HttpServletRequest request){
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
        if (StringUtils.isNotEmpty(String.valueOf(paramsMap.get("interfaceName")))) {
            queryMap.put("interfaceName", new Query(Condition.LIKE, String.valueOf(paramsMap.get("interfaceName"))));
        }
        if (StringUtils.isNotEmpty(String.valueOf(paramsMap.get("interfaceIp")))) {
            queryMap.put("interfaceIp", new Query(Condition.LIKE, String.valueOf(paramsMap.get("interfaceIp"))));
        }
        if (StringUtils.isNotEmpty(String.valueOf(paramsMap.get("dataItems")))) {
            queryMap.put("dataItems", new Query(Condition.LIKE, String.valueOf(paramsMap.get("dataItems"))));
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
}
