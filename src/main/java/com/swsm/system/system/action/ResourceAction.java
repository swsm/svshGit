package com.swsm.system.system.action;

import com.alibaba.fastjson.JSON;
import com.core.action.EntityHandler;
import com.core.action.HandlerResult;
import com.core.service.IEntityService;
import com.core.tools.*;
import com.core.tools.file.ExcelUtil;
import com.swsm.system.constant.CommonConstants;
import com.swsm.system.log.model.OperateContent;
import com.swsm.system.log.service.IOperateLogService;
import com.swsm.system.login.session.SessionManage;
import com.swsm.system.login.session.UserSession;
import com.swsm.system.system.model.Dict;
import com.swsm.system.system.model.Resource;
import com.swsm.system.system.model.Role;
import com.swsm.system.system.service.IDictService;
import com.swsm.system.system.service.IResourceService;
import com.swsm.system.system.service.IRoleService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>ClassName: ResourceAction</p>
 * <p>Description: 资源Action</p>
 */
@Controller
@RequestMapping("/main/system")
public class ResourceAction extends EntityHandler<Resource> {
    
    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(ResourceAction.class);
    
    /**
     * 资源ResourceService
     */
    @Autowired
    @Qualifier("resourceService")
    private IResourceService resourceService;
    
    /**
     * 操作日志OperateLogService
     */
    @Autowired
    @Qualifier("operateLogService")
    private IOperateLogService operateLogService;
    
    /**
     * 字典DictServiceImpl
     */
    @Autowired
    @Qualifier("dictService")
    private IDictService dictServiceImpl;
    
    /**
     * 角色RoleService
     */
    @Autowired
    @Qualifier("roleService")
    private IRoleService roleService;
    
    @Override
    @Autowired
    @Qualifier("resourceService")
    public void setEntityServiceImpl(IEntityService<Resource> entityServiceImpl) {
        this.entityServiceImpl = entityServiceImpl;
        this.clazz = Resource.class;
    }
    
    /**
     * <p>Description: 查询资源树，用于在角色管理中选择资源</p>
     * @param request 请求对象
     * @param response 返回对象
     * @throws Exception 异常
     */
    @RequestMapping(value = "/getResourceTree.mvc")
    @ResponseBody
    public void getResourceTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String jsonStr = null;
        String parentId;
        parentId = request.getParameter("parentId");
        String roleId;
        roleId = request.getParameter("roleId");
        String flag;
        flag = request.getParameter("flag");
        //执行查询
        List<Resource> resList;
//        resList = this.resourceService.getResourceTreeByFilter(parentId, roleId, flag);
        resList = this.resourceService.getResourceTree(parentId, roleId, flag);
        jsonStr = this.getJsonStr(resList);
        logger.info(jsonStr);
        MesBaseUtil.writeJsonStr(response, jsonStr);
    }
    
    /**
     * <p>Description: resList不为空时的jsonStr</p>
     * @param resList resList
     * @return jsonStr
     */
    private String getJsonStr(List<Resource> resList) {
        String jsonStr = null;
        if (resList != null) {
            Map<String, Object> causeMaps;
            causeMaps = new HashMap<String, Object>();
            causeMaps.put("children", resList);
            Map<Class<?>, String[]> excludeMap;
            excludeMap = new HashMap<Class<?>, String[]>();
            excludeMap.put(Resource.class, new String[]{"roleList", "parentResource"});
            jsonStr = JsonUtil.getJsonStringFromMap(causeMaps, excludeMap);
        }
        return jsonStr;
    }
    
    /**
     * 
     * <p>Description: 通用条件查询，如要组装查询条件，请重写doQueryList方法</p>
     * @param request 请求对象
     * @param response 返回对象
     * @throws Exception 异常
     */
    @RequestMapping(value = "/queryModules.mvc")
    @ResponseBody
    public void queryModules(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //构建查询条件
        Map<String, Query> queryMap;
        queryMap = new HashMap<String, Query>();
        queryMap.put("parentResource.id", new Query(Condition.NULL, null));
        //构建过滤器
        Filter filter;
        filter = doFilter(request, queryMap);
        //执行查询
        List<Resource> resList; 
        resList = this.resourceService.getModulesByFilter(filter);
        Map<Class<?>, String[]> excludeMap;
        excludeMap = new HashMap<Class<?>, String[]>();
        excludeMap.put(Resource.class, new String[]{"roleList", "children", "childResList", "parentResource"});
        MesBaseUtil.writeJsonStr(response, this.toJsonStrForGrid(resList, filter.getPageInfo(), excludeMap));
    }
    
    /**
     * <p>Description: 查询资源信息</p>
     * @param request 请求对象
     * @param response 返回对象
     * @throws Exception 异常
     */
    @RequestMapping(value = "/queryResource.mvc")
    @ResponseBody
    public void getResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //构建过滤器
        Filter filter;
        filter = createFilter(request);
        List<Resource> list;
        list = this.resourceService.queryPagedEntityList(filter, Resource.class);
        Map<Class<?>, String[]> excludeMap;
        excludeMap = new HashMap<Class<?>, String[]>();
        excludeMap.put(Resource.class, new String[]{"roleList", "children", "childResList", "parentResource"});
        MesBaseUtil.writeJsonStr(response, this.toJsonStrForGrid(list, filter.getPageInfo(), excludeMap));
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
        queryMap.put("parentResource", new Query(Condition.LEFT_JOIN, null));
        if (StringUtils.isNotEmpty(request.getParameter("resName"))) {
            queryMap.put("resName", new Query(Condition.LIKE, request.getParameter("resName")));
        }
        if (StringUtils.isNotEmpty(request.getParameter("resType"))) {
            queryMap.put("resType", new Query(Condition.EQ, request.getParameter("resType")));
        }
        filter.setQueryMap(queryMap);
        return filter;
    }
    
    /**
     * 
     * <p>Description: 查询资源树，用于在维护资源时选择上级资源</p>
     * @param request 请求对象
     * @param response 返回对象
     * @throws Exception 异常
     */
    @RequestMapping(value = "/queryTreeForParent.mvc")
    @ResponseBody
    public void queryTreeForParent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String resName;
        resName = request.getParameter("resName");
        //执行查询
        List<Map<String, Object>> list;
        list = this.resourceService.getResourceTreeForParentByFilter(resName);
        Map<String, Object> map;
        map = new HashMap<String, Object>();
        map.put("parentId", null);
        map.put("id", "-1");
        map.put("text", "根节点");
        this.findFirstNode(list);
        if (!list.isEmpty()) {
            this.loadNodeChildren(map, list);
        }
        String jsonStr;
        jsonStr = JsonUtil.getJsonStringFromMap(map);
        logger.info(jsonStr);
        WebUtil.response(response, jsonStr);
    }
    
    /**
     * <p>Description: 查询一级资源</p>
     * @param list 机构树
     */
    private void findFirstNode(List<Map<String, Object>> list) {
        for (Map<String, Object> map : list) {
            if (map.get("parentId") == null) {
                map.put("parentId", "-1");
            }
        }
    }

    /**
     * <p>Description: 递归非叶子节点</p>
     * @param currendNode 当前节点
     * @param list 机构树
     */
    private void loadNodeChildren(Map<String, Object> currendNode, List<Map<String, Object>> list) {
        String parentId;
        parentId = currendNode.get("id").toString();
        List<Map<String, Object>> returnList;
        returnList = this.getChildrenNodes(parentId, list);
        if (returnList.isEmpty()) {
            currendNode.put("leaf", true);
        } else {
            currendNode.put("leaf", false);
            currendNode.put("expanded", true);
            currendNode.put("children", returnList);
            for (Map<String, Object> map : returnList) {
                this.loadNodeChildren(map, list);
            }
        }
    }

    /**
     * <p>Description: 查询当前节点下的节点</p>
     * @param parentId 当前节点id
     * @param list 机构树
     * @return returnList 当前节点下的节点
     */
    private List<Map<String, Object>> getChildrenNodes(String parentId, List<Map<String, Object>> list) {
        List<Map<String, Object>> returnList; 
        returnList = new LinkedList<Map<String, Object>>();
        for (Map<String, Object> map : list) {
            if ((map.get("parentId") + "").equals(parentId)) {
                returnList.add(map);
            }
        }
        return returnList;
    }
    
    /**
     * <p>Description: 新增或修改资源</p>
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/saveResource.mvc")
    @ResponseBody
    public void saveOrUpdateResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String rows;
        rows = WebUtil.fetch(request, "storeDate");
        List<Resource> objList;
        objList = JSON.parseArray(rows, Resource.class);
        SessionManage sessionManage;
        sessionManage = SessionManage.getSessionManage();
        UserSession userSession;
        userSession = sessionManage.getUserSession(request.getSession());
        this.resourceService.saveOrUpdateResource(objList, userSession.getUserName());
        //增加操作日志
        List<OperateContent> list;
        list = new ArrayList<OperateContent>();
        for (Resource resource : objList) {
            OperateContent operateContent;
            operateContent = new OperateContent();
            operateContent.setOperateName(resource.getResName());
            if (StringUtils.isEmpty(resource.getUpdateUser())) {
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
     * <p>Description: 进行唯一性检验</p>
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/getResourceUnique.mvc")
    @ResponseBody
    public void getResourceUnique(HttpServletRequest request, HttpServletResponse response) throws Exception {
        queryCheckUnique(request, response);
    }
    
    /**
     * <p>Description: 删除资源</p>
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/deleteResource.mvc")
    @ResponseBody
    public void deleteResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ids;
        ids = request.getParameter("ids");
        List<Resource> resourceList;
        resourceList = new ArrayList<Resource>();
        for (String id : ids.split(",")) {
            Resource res;
            res = new Resource();
            res.setId(id);
            resourceList.add(res);
        }
        this.resourceService.realDeleteEntity(resourceList);
        //增加操作日志
        String resNames;
        resNames = request.getParameter("resNames");
        SessionManage sessionManage;
        sessionManage = SessionManage.getSessionManage();
        UserSession userSession;
        userSession = sessionManage.getUserSession(request.getSession());
        List<OperateContent> list;
        list = new ArrayList<OperateContent>();
        for (String resName : resNames.split(",")) {
            OperateContent operateContent;
            operateContent = new OperateContent();
            operateContent.setOperateName(resName);
            operateContent.setOperateType(CommonConstants.LOG_CONTENT_DELETE);
            list.add(operateContent);
        }
        this.operateLogService.insertOperateLog(request, userSession.getDispName(),
                userSession.getUserId(), list);
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, HandlerResult.SUCCESS));
    }
    
    /**
     * <p>Description: 更新资源状态</p>
     * @param request HTTP请求信息
     * @param response HTTP响应信息
     * @throws Exception 异常信息
     */
    @RequestMapping(value = "/updateResourceEnabled.mvc")
    @ResponseBody
    public void updateResourceEnabled(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ids;
        ids = request.getParameter("ids");
        this.resourceService.updateResourceEnabled(request.getParameter("enabled"), ids.split(","));
        //增加操作日志
        String resNames;
        resNames = request.getParameter("resNames");
        SessionManage sessionManage;
        sessionManage = SessionManage.getSessionManage();
        UserSession userSession;
        userSession = sessionManage.getUserSession(request.getSession());
        List<OperateContent> list;
        list = new ArrayList<OperateContent>();
        for (String resName : resNames.split(",")) {
            OperateContent operateContent;
            operateContent = new OperateContent();
            operateContent.setOperateName(resName);
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
        //执行保存
        WebUtil.response(response, this.toJsonStrActionMessage(true, HandlerResult.SUCCESS));
    }
    
    /**
     * <p>
     * Description: 资源导出
     * </p>
     * 
     * @param request HTTP请求信息
     * @param response HTTP响应信息
     * @throws Exception 异常信息
     */
    @RequestMapping(value = "/exportResource.mvc")
    @ResponseBody
    public void exportResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String[] columnNames = null;
        String[] fileNames = null;
        Filter filter;
        filter = createFilterExport(request);
        //根据结果集进行判断
        List<Resource> list;
        list = this.resourceService.queryPagedEntityList(filter, Resource.class);
        //获得对应的字典值
        List<Dict> listDict;
        listDict = this.dictServiceImpl.getDictListyByParentKey("SYS_ZYLBDM");
        //获得对应的字典值
        List<Dict> listDictBelong;
        listDictBelong = this.dictServiceImpl.getDictListyByParentKey("BELONG_SYSTEM");
        //数据重置
        for (int i = 0; i < list.size(); i++) {
            //list.get(i).setPreResName(list.get(i).getParentResource().getResName());
            //进行字典值赋值
            for (Dict dict : listDictBelong) {
                if (dict.getDictKey().equals(list.get(i).getBelongSystem())) {
                    list.get(i).setBelongSystem(dict.getDictValue());
                    break;
                }
            }
            //进行字典值赋值
            for (Dict dict : listDict) {
                if (dict.getDictKey().equals(list.get(i).getResType())) {
                    list.get(i).setResType(dict.getDictValue());
                    break;
                }
            }
        }
        columnNames = new String[] { "资源名称", "资源Code", "资源类型", "上级资源", "所属系统", "备注", "排序", "创建日期" };
        fileNames = new String[] { "resName", "resCode", "resType", "parentResName", "belongSystem", "remark", "resOrder", "createDate"};
        ExcelUtil.exportExcel(response, list, Resource.class, columnNames, fileNames,
                "资源管理" + dateFormat.format(new Date()));
        //构建过滤器
        /*filter.getQueryMap().remove("parentResource");
        List<Map<String, Object>> list;
        list = this.resourceService.exportResourceList(filter);
        columnNames = new String[] { "资源名称", "资源Code", "资源类型", "上级资源", "所属系统", "备注", "排序", "创建日期" };
        fileNames = new String[] { "resName", "resCode", "resType", "preResName", "belongSystem", "remark", "resOrder", "createDate"};
        ExcelUtil.exportExcel(response, list, columnNames, fileNames,
                "资源管理" + dateFormat.format(new Date()));*/
        
    }

    /**
     * 
     * <p>Description: 创建过滤器</p>
     * @param request 请求
     * @return 结果集
     * @throws UnsupportedEncodingException 异常
     */
    private Filter createFilterExport(HttpServletRequest request) throws UnsupportedEncodingException {
        Filter filter;
        filter = new Filter();
        filter.getPageInfo().start = 0;
        filter.getPageInfo().limit = 65535;
        //构建查询条件
        Map<String, Query> queryMap;
        queryMap = new HashMap<String, Query>();
        queryMap.put("parentResource", new Query(Condition.LEFT_JOIN, null));
        if (StringUtils.isNotEmpty(request.getParameter("resName"))) {
            queryMap.put("resName", new Query(Condition.LIKE, java.net.URLDecoder.decode(request.getParameter("resName"), "UTF-8"))); //解码
        }
        if (StringUtils.isNotEmpty(request.getParameter("resType")) 
                && !"null".equals(String.valueOf(request.getParameter("resType")))) {
            queryMap.put("resType", new Query(Condition.EQ, request.getParameter("resType")));
        }
        filter.setQueryMap(queryMap);
        return filter;
    }
    
    /**
     * <p>
     * Description: 角色资源树导出
     * </p>
     * 
     * @param request HTTP请求信息
     * @param response HTTP响应信息
     * @throws Exception 异常信息
     */
    @RequestMapping(value = "/exportResourceTree.mvc")
    @ResponseBody
    public void exportResourceTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleId;
        roleId = request.getParameter("roleId");
        Role role;
        role = this.roleService.getRoleById(roleId);
        String roleName = "";
        if (role != null) {
            roleName = role.getRoleName();
        }
        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String[] columnNames = null;
        String[] fileNames = null;
        //根据结果集进行判断
        columnNames = new String[] { "序号", "资源名称", "资源Code", "资源类别"};
        fileNames = new String[] { "xh", "resName", "resCode", "resType"};
        List<Map<String, Object>> list;
        list = this.resourceService.getResourceTreeForExport(roleId);
        ExcelUtil.exportExcel(response, list, columnNames, fileNames,
                (roleName + "_") + dateFormat.format(new Date()));
        
    }
}
