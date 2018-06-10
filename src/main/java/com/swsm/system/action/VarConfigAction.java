package com.swsm.system.action;

import com.core.action.BaseHandler;
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
import com.swsm.system.model.DictIdx;
import com.swsm.system.model.Role;
import com.swsm.system.model.User;
import com.swsm.system.model.VarConfig;
import com.swsm.system.service.IVarConfigService;
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
 * <p>ClassName: VarConfigAction</p>
 * <p>Description: 配置项管理的Action任务层</p>
 */
@Controller
@RequestMapping("/main/system")
public class VarConfigAction extends BaseHandler {
    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(VarConfigAction.class);
    
    /**
     * VarConfigservice
     */
    @Autowired
    @Qualifier("varConfigService")
    private IVarConfigService varConfigService;
    
    /**
     * 操作日志OperateLogService
     */
    @Autowired
    @Qualifier("operateLogService")
    private IOperateLogService operateLogService;
    
    /**
     * <p>Description: 获取当前配置项下的所有子项</p>
     * @param request 请求对象
     * @param response 响应对象
     * @throws Exception 异常
     * @return String 操作结果
     */
    @RequestMapping(value = "/getVarConfigTree.mvc", produces = "application/json; charset=utf-8")
    @ResponseBody
    public String findVarConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String parentId = request.getParameter("node");
        if (parentId == null || "root".equals(parentId)) {
            parentId = ActionConstants.ROOT_NODE_ID;
        }
        List<VarConfig> list;
        list = this.varConfigService.findVarConfigByPanrentId(parentId);
        Map<Class<?>, String[]> excludes;
        excludes = new HashMap<Class<?>, String[]>();
        excludes.put(VarConfig.class, new String[]{"children", "userList"});
        String str;
        str = MesJsonUtil.toJsonStr(list, new PageInfo(), excludes);
        logger.info(str);
        return str;
    }
    
    /**
     * 删除配置项 假删除 新框架
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/deleteVarConfig.mvc")
    @ResponseBody
    public void deleteVarConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id;
        id = request.getParameter("varId");
        Jdf3UserSession userSession;
        userSession = super.getJdf3UserSession(request);
        String result;
        result =  this.varConfigService.deleteVarConfig(id, userSession.getUserName());
        //增加操作日志
        if ("success".equals(result)) {
            OperateContent operateContent;
            operateContent = new OperateContent();
            operateContent.setOperateName(request.getParameter("varName"));
            operateContent.setOperateType(CommonConstants.LOG_CONTENT_DELETE);
            this.operateLogService.insertOperateLog(request, userSession.getDispName(),
                    userSession.getUserId(), operateContent);
        }
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, result));
    }
    
    /**
     * 保存配置项
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/saveVarConfig.mvc")
    @ResponseBody
    public void saveVarConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
        VarConfig varConfig;
        varConfig = new VarConfig();
        varConfig.setParentVarConfig(((EntityServiceImpl<VarConfig>) this.varConfigService)
                .findEntity(VarConfig.class, request.getParameter("parentVarConfigId")));
        varConfig.setVarName(request.getParameter("varName"));
        varConfig.setVarDisplay(request.getParameter("varDisplay"));
        varConfig.setVarValue(request.getParameter("varValue"));
        varConfig.setVarType(request.getParameter("varType"));
        varConfig.setVarOrder(Integer.parseInt(request.getParameter("varOrder")));
        varConfig.setRemark(request.getParameter("remark"));
        Jdf3UserSession userSession;
        userSession = super.getJdf3UserSession(request);
        String result;
        result = this.varConfigService.saveVarConfig(varConfig, userSession.getUserName());
        //增加操作日志
        if ("success".equals(result)) {
            OperateContent operateContent;
            operateContent = new OperateContent();
            operateContent.setOperateName(varConfig.getVarName());
            operateContent.setOperateType(CommonConstants.LOG_CONTENT_INSERT);
            this.operateLogService.insertOperateLog(request, userSession.getDispName(),
                    userSession.getUserId(), operateContent);
        }
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, result));
    }
    
    /**
     * 更新配置项  新框架
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/updateVarConfig.mvc")
    @ResponseBody
    public void updateVarConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
        VarConfig varConfig;
        varConfig = ((EntityServiceImpl<VarConfig>) this.varConfigService)
                .findEntity(VarConfig.class, request.getParameter("varId"));
        varConfig.setParentVarConfig(((EntityServiceImpl<VarConfig>) this.varConfigService)
                .findEntity(VarConfig.class, request.getParameter("parentVarConfigId")));
        varConfig.setVarName(request.getParameter("varName"));
        varConfig.setVarDisplay(request.getParameter("varDisplay"));
        varConfig.setVarValue(request.getParameter("varValue"));
        varConfig.setVarType(request.getParameter("varType"));
        varConfig.setVarOrder(Integer.parseInt(request.getParameter("varOrder")));
        varConfig.setRemark(request.getParameter("remark"));
        Jdf3UserSession userSession;
        userSession = super.getJdf3UserSession(request);
        String result;
        result = this.varConfigService.updateVarConfig(varConfig, userSession.getUserName());
        //增加操作日志
        if ("success".equals(result)) {
            OperateContent operateContent;
            operateContent = new OperateContent();
            operateContent.setOperateName(varConfig.getVarName());
            operateContent.setOperateType(CommonConstants.LOG_CONTENT_UPDATE);
            this.operateLogService.insertOperateLog(request, userSession.getDispName(),
                    userSession.getUserId(), operateContent);
        }
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, result));
    }
    
    /**
     * 配置项树子节点移动  新框架
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/treeVarDropMove.mvc")
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
        result = this.varConfigService.treeDropMove(sourceNode, targetNode, userSession.getUserName(), dropPosition);
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, result));
    }
    
    /**
     * 配置项树子节点复制  新框架
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/treeVarDropCopy.mvc")
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
        result = this.varConfigService.treeDropCopy(sourceNode, targetNode, userSession.getUserName(), dropPosition);
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, result));
    }

    /**
     * <p>Description: 设置构造json字符串的属性排除数组</p>
     * @return 排除数组
     */
    protected Map<Class<?>, String[]> getJsonExcludedProperties() {
        Map<Class<?>, String[]> excludeMap;
        excludeMap = new HashMap<Class<?>, String[]>();
        excludeMap.put(User.class, new String[]{"roleList", "varConfigList"});
        excludeMap.put(Role.class, new String[]{"userList"});
        excludeMap.put(VarConfig.class, new String[]{"children", "parentVarConfig", "leaf", "userList"});
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
