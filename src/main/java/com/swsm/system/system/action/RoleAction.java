package com.swsm.system.system.action;

import com.alibaba.fastjson.JSON;
import com.core.action.EntityHandler;
import com.core.action.HandlerResult;
import com.core.service.IEntityService;
import com.core.tools.*;
import com.swsm.constant.CommonConstants;
import com.swsm.log.model.OperateContent;
import com.swsm.log.service.IOperateLogService;
import com.swsm.login.session.SessionManage;
import com.swsm.login.session.UserSession;
import com.swsm.system.system.model.Role;
import com.swsm.system.system.service.IRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>ClassName: RoleAction</p>
 * <p>Description: 角色Action</p>
 */
@Controller
@RequestMapping("/main/system")
public class RoleAction extends EntityHandler<Role> {
    
    @Override
    @Autowired
    @Qualifier("roleService")
    public void setEntityServiceImpl(IEntityService<Role> entityServiceImpl) {
        this.entityServiceImpl = entityServiceImpl;
        this.clazz = Role.class;
    }
    
    /**
     * 角色RoleService
     */
    @Autowired
    @Qualifier("roleService")
    private IRoleService roleService;
    
    /**
     * 操作日志OperateLogService
     */
    @Autowired
    @Qualifier("operateLogService")
    private IOperateLogService operateLogService;
    
    /**
     * <p>Description: 查询角色信息</p>
     * @param request 请求
     * @param response 响应
     */
    @RequestMapping(value = "/getRole.mvc")
    @ResponseBody
    public void getRole(HttpServletRequest request, HttpServletResponse response) {
        //构建过滤器
        Filter filter;
        filter = createFilter(request);
        List<Role> list;
        list = this.roleService.getRole(filter);
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
        if (StringUtils.isNotEmpty(request.getParameter("roleName"))) {
            queryMap.put("roleName", new Query(Condition.LIKE, request.getParameter("roleName")));
        }
        if (StringUtils.isNotEmpty(request.getParameter("roleType"))) {
            queryMap.put("roleType", new Query(Condition.EQ, request.getParameter("roleType")));
        }
        filter.setQueryMap(queryMap);
        return filter;
    }
    
    /**
     * <p>Description: 设置构造json字符串的属性排除数组</p>
     * @return 排除数组
     */
    protected Map<Class<?>, String[]> getJsonExcludedProperties() {
        Map<Class<?>, String[]> excludeMap;
        excludeMap = new HashMap<Class<?>, String[]>();
        excludeMap.put(Role.class, new String[]{"userList", "resList"});
        return excludeMap;
    }
    
    /**
     * <p>Description: 新增或修改角色</p>
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/saveRole.mvc")
    @ResponseBody
    public void saveOrUpdateRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String rows;
        rows = WebUtil.fetch(request, "storeDate");
        List<Role> objList;
        objList = JSON.parseArray(rows, Role.class);
        SessionManage sessionManage;
        sessionManage = SessionManage.getSessionManage();
        UserSession userSession;
        userSession = sessionManage.getUserSession(request.getSession());
        this.roleService.saveOrUpdateRole(objList, userSession.getUserName());
        //增加操作日志
        List<OperateContent> list;
        list = new ArrayList<OperateContent>();
        for (Role role : objList) {
            OperateContent operateContent;
            operateContent = new OperateContent();
            operateContent.setOperateName(role.getRoleName());
            if (StringUtils.isEmpty(role.getUpdateUser())) {
                operateContent.setOperateType(CommonConstants.LOG_CONTENT_INSERT);
            } else {
                operateContent.setOperateType(CommonConstants.LOG_CONTENT_UPDATE);
            }
            list.add(operateContent);
        }
        this.operateLogService.insertOperateLog(request, userSession.getDispName(),
                userSession.getUserId(), list);
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, HandlerResult.SUCCESS));
    }
    
    /**
     * <p>Description: 删除角色</p>
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/deleteRole.mvc")
    @ResponseBody
    public void deleteRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ids;
        ids = request.getParameter("ids");
        this.roleService.deleteRole(ids.split(","));
        //增加操作日志
        String roleNames;
        roleNames = request.getParameter("roleNames");
        SessionManage sessionManage;
        sessionManage = SessionManage.getSessionManage();
        UserSession userSession;
        userSession = sessionManage.getUserSession(request.getSession());
        List<OperateContent> list;
        list = new ArrayList<OperateContent>();
        for (String roleName : roleNames.split(",")) {
            OperateContent operateContent;
            operateContent = new OperateContent();
            operateContent.setOperateName(roleName);
            operateContent.setOperateType(CommonConstants.LOG_CONTENT_DELETE);
            list.add(operateContent);
        }
        this.operateLogService.insertOperateLog(request, userSession.getDispName(),
                userSession.getUserId(), list);
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, HandlerResult.SUCCESS));
    }
    
    /**
     * <p>Description: 启用或禁用角色</p>
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/updateRoleEnabled.mvc")
    @ResponseBody
    public void updateRoleEnabled(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ids;
        ids = request.getParameter("ids");
        this.roleService.updateRoleEnabled(request.getParameter("enabled"), ids.split(","));
        //增加操作日志
        String roleNames;
        roleNames = request.getParameter("roleNames");
        SessionManage sessionManage;
        sessionManage = SessionManage.getSessionManage();
        UserSession userSession;
        userSession = sessionManage.getUserSession(request.getSession());
        List<OperateContent> list;
        list = new ArrayList<OperateContent>();
        for (String roleName : roleNames.split(",")) {
            OperateContent operateContent;
            operateContent = new OperateContent();
            operateContent.setOperateName(roleName);
            //0-禁用  1-启用
            if ("0".equals(request.getParameter("enabled"))) {
                operateContent.setOperateType(CommonConstants.LOG_CONTENT_DISABLE);
            } else {
                operateContent.setOperateType(CommonConstants.LOG_CONTENT_ENABLE);
            }
            list.add(operateContent);
        }
        this.operateLogService.insertOperateLog(request, userSession.getDispName(),
                userSession.getUserId(), list);
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, HandlerResult.SUCCESS));
    }
    
    /**
     * <p>Description: 将选中的资源id保存到角色与资源关系表中</p>
     * @param request HTTP请求
     * @param response HTTP响应
     * @throws Exception 异常信息
     */
    @RequestMapping(value = "/updateRoleResource.mvc")
    @ResponseBody
    public void updateRoleResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取从前台传递过来的对象@分割的id字符串,并组装为数组
        String resIdStr;
        resIdStr = request.getParameter("resIdStr");
        String roleId;
        roleId = request.getParameter("roleId");
        //更改资源与角色的对应关系
        this.roleService.updateRoleResource(roleId, resIdStr);
        //增加操作日志
        String roleName;
        roleName = request.getParameter("roleName");
        SessionManage sessionManage;
        sessionManage = SessionManage.getSessionManage();
        UserSession userSession;
        userSession = sessionManage.getUserSession(request.getSession());
        OperateContent operateContent;
        operateContent = new OperateContent();
        operateContent.setOperateName(roleName);
        operateContent.setOperateType(CommonConstants.LOG_CONTENT_UPDATE);
        this.operateLogService.insertOperateLog(request, userSession.getDispName(),
                userSession.getUserId(), operateContent);
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, HandlerResult.SUCCESS));
    }
    
    /**
     * <p>Description: 进行唯一性检验</p>
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/getCheckRoleUnique.mvc")
    @ResponseBody
    public void getCheckRoleUnique(HttpServletRequest request, HttpServletResponse response) throws Exception {
        queryCheckUnique(request, response);
    }
    
    /**
     * 
     * <p>Description: 角色复制于功能</p>
     * @param request 请求对象
     * @param response 返回对象
     * @throws Exception 异常
     */
    @RequestMapping(value = "/copyRole.mvc")
    @ResponseBody
    public void copyRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //增加操作日志
        String roleName;
        roleName = request.getParameter("roleName");
        SessionManage sessionManage;
        sessionManage = SessionManage.getSessionManage();
        UserSession userSession;
        userSession = sessionManage.getUserSession(request.getSession());
        OperateContent operateContent;
        operateContent = new OperateContent();
        operateContent.setOperateName(roleName);
        operateContent.setOperateType(CommonConstants.LOG_CONTENT_COPY);
        this.operateLogService.insertOperateLog(request, userSession.getDispName(),
                userSession.getUserId(), operateContent);
        //复制角色
        String id;
        id = request.getParameter("id");
        String roleType;
        roleType = request.getParameter("roleType");
        String ignoreDecesion;
        ignoreDecesion = request.getParameter("ignoreDecesion");
        String remark;
        remark = request.getParameter("remark");
        this.roleService.copyRole(id, roleName, roleType, ignoreDecesion, remark);
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, HandlerResult.SUCCESS));
    }
}
