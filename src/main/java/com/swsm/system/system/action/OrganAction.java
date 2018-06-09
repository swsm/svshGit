package com.swsm.system.system.action;

import com.core.action.BaseHandler;
import com.core.exception.BaseException;
import com.core.service.impl.EntityServiceImpl;
import com.core.session.Jdf3UserSession;
import com.core.tools.JsonUtil;
import com.core.tools.MesBaseUtil;
import com.core.tools.PageInfo;
import com.core.tools.format.MesJsonUtil;
import com.swsm.constant.ActionConstants;
import com.swsm.constant.CommonConstants;
import com.swsm.log.model.OperateContent;
import com.swsm.log.service.IOperateLogService;
import com.swsm.system.system.model.DictIdx;
import com.swsm.system.system.model.Organ;
import com.swsm.system.system.model.Role;
import com.swsm.system.system.model.User;
import com.swsm.system.system.service.IOrganService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * <p>ClassName: OrganAction</p>
 * <p>Description: 机构Action</p>
 */
@Controller
@RequestMapping("/main/system")
public class OrganAction extends BaseHandler {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(OrganAction.class);

    /**
     * Organservice
     */
    @Autowired
    @Qualifier("organService")
    private IOrganService organService;
    
    /**
     * 操作日志OperateLogService
     */
    @Autowired
    @Qualifier("operateLogService")
    private IOperateLogService operateLogService;

    /**
     * <p>Description: 获取当前部门下的所有子部门</p>
     * @param request 请求对象
     * @param response 响应对象
     * @throws Exception 异常
     * @return String 操作结果
     */
    @RequestMapping(value = "/getOrganTree.mvc", produces = "application/json; charset=utf-8")
    @ResponseBody
    public String findOrgan(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String parentId = request.getParameter("node");
            if (parentId == null || "root".equals(parentId)) {
                parentId = ActionConstants.ROOT_NODE_ID;
            }
            List<Organ> list;
            list = this.organService.findOrganByPanrentId(parentId);
            Map<Class<?>, String[]> excludes;
            excludes = new HashMap<Class<?>, String[]>();
            excludes.put(Organ.class, new String[]{"children", "userList"});
            String str;
            str = MesJsonUtil.toJsonStr(list, new PageInfo(), excludes);
            logger.info(str);
            return str;
        } catch (BaseException e) {
            throw e;
        }
    }
    
    /**
     * 删除部门 假删除 新框架
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/deleteOrgan.mvc")
    @ResponseBody
    public void deleteOrgan(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id;
        id = request.getParameter("organId");
        Jdf3UserSession userSession;
        userSession = super.getJdf3UserSession(request);
        String result;
        result =  this.organService.deleteOrgan(id, userSession.getUserName());
        //增加操作日志
        if ("success".equals(result)) {
            OperateContent operateContent;
            operateContent = new OperateContent();
            operateContent.setOperateName(request.getParameter("organName"));
            operateContent.setOperateType(CommonConstants.LOG_CONTENT_DELETE);
            this.operateLogService.insertOperateLog(request, userSession.getDispName(),
                    userSession.getUserId(), operateContent);
        }
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, result));
    }
    
    /**
     * 保存部门 
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/saveOrgan.mvc")
    @ResponseBody
    public void saveOrgan(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Organ organ;
        organ = new Organ();
        organ.setParentOrgan(((EntityServiceImpl<Organ>) this.organService)
                .findEntity(Organ.class, request.getParameter("parentOrganId")));
        organ.setOrganName(request.getParameter("organName"));
        organ.setOrganCode(request.getParameter("organCode"));
        organ.setDutyUsername(request.getParameter("dutyUsername"));
        organ.setOrganOrder(Integer.parseInt(request.getParameter("organOrder")));
        organ.setRemark(request.getParameter("remark"));
        Jdf3UserSession userSession;
        userSession = super.getJdf3UserSession(request);
        String result;
        result = this.organService.saveOrgan(organ, userSession.getUserName());
        //增加操作日志
        if ("success".equals(result)) {
            OperateContent operateContent;
            operateContent = new OperateContent();
            operateContent.setOperateName(organ.getOrganName());
            operateContent.setOperateType(CommonConstants.LOG_CONTENT_INSERT);
            this.operateLogService.insertOperateLog(request, userSession.getDispName(),
                    userSession.getUserId(), operateContent);
        }
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, result));
    }
    
    /**
     * 更新部门  新框架
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/updateOrgan.mvc")
    @ResponseBody
    public void updateOrgan(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Organ organ;
        organ = ((EntityServiceImpl<Organ>) this.organService)
                .findEntity(Organ.class, request.getParameter("organId"));
        organ.setParentOrgan(((EntityServiceImpl<Organ>) this.organService)
                .findEntity(Organ.class, request.getParameter("parentOrganId")));
        organ.setOrganName(request.getParameter("organName"));
        organ.setOrganCode(request.getParameter("organCode"));
        organ.setDutyUsername(request.getParameter("dutyUsername"));
        organ.setOrganOrder(Integer.parseInt(request.getParameter("organOrder")));
        organ.setRemark(request.getParameter("remark"));
        Jdf3UserSession userSession;
        userSession = super.getJdf3UserSession(request);
        String result;
        result = this.organService.updateOrgan(organ, userSession.getUserName());
        //增加操作日志
        if ("success".equals(result)) {
            OperateContent operateContent;
            operateContent = new OperateContent();
            operateContent.setOperateName(organ.getOrganName());
            operateContent.setOperateType(CommonConstants.LOG_CONTENT_UPDATE);
            this.operateLogService.insertOperateLog(request, userSession.getDispName(),
                    userSession.getUserId(), operateContent);
        }
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, result));
    }
    
    /**
     * 部门树子节点移动  新框架
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/treeDropMove.mvc")
    @ResponseBody
    public void treeDropMove(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String sourceNode;
        sourceNode = request.getParameter("sourceNode");
        String targetNode;
        targetNode = request.getParameter("targetNode");
        String dropPosition;
        dropPosition = request.getParameter("dropPosition");
        Jdf3UserSession userSession;
        userSession = super.getJdf3UserSession(request);
        String result;
        result = this.organService.treeDropMove(sourceNode, targetNode, userSession.getUserName(), dropPosition);
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, result));
    }
    
    /**
     * 部门树子节点复制  新框架
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/treeDropCopy.mvc")
    @ResponseBody
    public void treeDropCopy(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String sourceNode;
        sourceNode = request.getParameter("sourceNode");
        String targetNode;
        targetNode = request.getParameter("targetNode");
        String dropPosition;
        dropPosition = request.getParameter("dropPosition");
        Jdf3UserSession userSession;
        userSession = super.getJdf3UserSession(request);
        String result;
        result = this.organService.treeDropCopy(sourceNode, targetNode, userSession.getUserName(), dropPosition);
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, result));
    }

    /**
     * <p>Description: 设置构造json字符串的属性排除数组</p>
     * @return 排除数组
     */
    protected Map<Class<?>, String[]> getJsonExcludedProperties() {
        Map<Class<?>, String[]> excludeMap;
        excludeMap = new HashMap<Class<?>, String[]>();
        excludeMap.put(User.class, new String[]{"roleList", "organList"});
        excludeMap.put(Role.class, new String[]{"userList"});
        excludeMap.put(Organ.class, new String[]{"children", "parentOrgan", "leaf", "userList"});
        return excludeMap;
    }
    
    
    /**
     * 
     * <p>Description: 操作的返回信息</p>
     * @param isSuccess 是否成功
     * @param message 返回消息
     * @return json格式字符串
     */
    protected String toJsonStrActionMessage(boolean isSuccess, String message) {
        return toJsonStrActionMessage(isSuccess, message, null, null, null);
    }
    
    /**
     * 
     * <p>Description: 操作的返回信息</p>
     * @param isSuccess 是否成功
     * @param message 返回消息
     * @param obj 返回对象
     * @param args 其他参数
     * @param excludes 转json字符串过程中要排除的属性，避免循环引用
     * @return json格式字符串
     */
    protected String toJsonStrActionMessage(boolean isSuccess, String message, DictIdx obj,
            Map<String , Object> args, Map<Class<?>, String[]> excludes) {
        Map<String, Object> causeMaps;
        causeMaps = new HashMap<String, Object>();
        causeMaps.put("success", isSuccess);
        causeMaps.put("message", message);
        causeMaps.put("resultObj", obj);
        if (args != null) {
            causeMaps.putAll(args);
        }
        if (excludes != null && excludes.size() > 0) {
          //如果excludes中包含有在转json过程中要排除掉的属性
            return JsonUtil.getJsonStringFromMap(causeMaps, excludes);
        } else {
          //如果excludes中不包含在转json过程中要排除掉的属性
            return JsonUtil.getJsonStringFromMap(causeMaps);
        }
    }

}
