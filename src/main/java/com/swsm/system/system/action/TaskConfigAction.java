package com.swsm.system.system.action;

import com.alibaba.fastjson.JSON;
import com.core.action.EntityHandler;
import com.core.service.IEntityService;
import com.core.tools.*;
import com.swsm.system.system.model.TaskConfig;
import com.swsm.system.system.service.ITaskConfigService;
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
 * <p>
 * ClassName: TaskConfigAction
 * </p>
 * <p>
 * Description: 任务调度配置的Action交互层
 * </p>
 */
@Controller
@RequestMapping(value = "/main/taskConfigAction")
public class TaskConfigAction extends EntityHandler<TaskConfig> {

    /**
     * 引入service
     */
    @Autowired
    @Qualifier("taskConfigService")
    private ITaskConfigService taskConfigService;

    @Override
    @Autowired
    @Qualifier("taskConfigService")
    public void setEntityServiceImpl(IEntityService<TaskConfig> entityServiceImpl) {
        this.entityServiceImpl = entityServiceImpl;
        this.clazz = TaskConfig.class;

    }

    /**
     * 
     * <p>
     * Description: 查询任务调度配置信息
     * </p>
     * 
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    @ResponseBody
    @RequestMapping(value = "/getTaskConfig.mvc")
    public void getTaskConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Filter filter;
        filter = this.createFilter(request);
        List<Map<String, Object>> list;
        list = this.taskConfigService.getTaskConfig(filter);
        MesBaseUtil.writeJsonStr(response,
                this.toJsonStrForGrid(list, filter.getPageInfo(), getJsonExcludedProperties()));

    }

    /**
     * 
     * <p>
     * Description: 构建查询条件
     * </p>
     * 
     * @param request 请求
     * @return 由查询条件组装成的过滤器
     */
    private Filter createFilter(HttpServletRequest request) {
        Filter filter;
        filter = new Filter();
        String start;
        start = request.getParameter("start");
        String limit;
        limit = request.getParameter("limit");
        String params;
        params = request.getParameter("queryParams");
        Map<String, Object> paramsMap;
        paramsMap = new HashMap<String, Object>();
        paramsMap = (Map<String, Object>) JSON.parse(params);
        if (start != null) {
            filter.getPageInfo().start = Integer.parseInt(start);
        }
        if (limit != null) {
            filter.getPageInfo().limit = Integer.parseInt(limit);
        }
        Map<String, Query> map;
        map = new HashMap<String, Query>();
        if (StringUtils.isNotEmpty(paramsMap.get("taskName").toString())) {
            map.put("taskName", new Query(Condition.EQ, paramsMap.get("taskName").toString()));
        }
        if (StringUtils.isNotEmpty(paramsMap.get("taskCode").toString())) {
            map.put("taskCode", new Query(Condition.EQ, paramsMap.get("taskCode").toString()));
        }
        filter.setQueryMap(map);
        return filter;
    }

    /**
     * 
     * <p>
     * Description: 启用任务调度配置
     * </p>
     * 
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    @ResponseBody
    @RequestMapping(value = "/useTaskConfig.mvc")
    public void useTaskConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String taskCode;
        taskCode = request.getParameter("taskCode");
//        this.taskConfigService.useTaskConfig(taskCode);
        String success = this.taskConfigService.useTaskConfig(taskCode);
        WebUtil.response(response, this.toJsonStrActionMessage(true, success));
    }

    /**
     * 
     * <p>
     * Description: 禁用任务调度配置
     * </p>
     * 
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    @ResponseBody
    @RequestMapping(value = "/forbidTaskConfig.mvc")
    public void forbidTaskConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String taskCode = request.getParameter("taskCode");
        this.taskConfigService.forbidTaskConfig(taskCode);
        //WebUtil.response(response, this.toJsonStrActionMessage(true, success));
    }

    /**
     * 
     * <p>
     * Description: 修改任务调度配置
     * </p>
     * 
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    @ResponseBody
    @RequestMapping(value = "/updateCronExpression.mvc")
    public void updateCronExpression(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String taskCode = request.getParameter("taskCode");
        String cronExpression = request.getParameter("cronExpression");
        this.taskConfigService.updateCronExpression(taskCode, cronExpression);
        //WebUtil.response(response, this.toJsonStrActionMessage(true, success));
    }

    /**
     * 
     * <p>
     * Description: 修改任务调度配置中的用户名和备注
     * </p>
     * 
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/saveTaskConfig.mvc")
    @ResponseBody
    public void saveTaskConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String rows;
        rows = WebUtil.fetch(request, "storeDate");
        List<TaskConfig> list;
        list = JSON.parseArray(rows, TaskConfig.class);
        this.taskConfigService.saveTaskConfig(list);
    }

}
