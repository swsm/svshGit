package com.swsm.system.system.action;

import com.alibaba.fastjson.JSON;
import com.core.action.EntityHandler;
import com.core.service.IEntityService;
import com.core.session.Jdf3UserSession;
import com.core.tools.*;
import com.swsm.system.constant.CommonConstants;
import com.swsm.system.log.model.OperateContent;
import com.swsm.system.log.service.IOperateLogService;
import com.swsm.system.system.model.Dict;
import com.swsm.system.system.model.Organ;
import com.swsm.system.system.model.Role;
import com.swsm.system.system.model.User;
import com.swsm.system.system.service.IDictService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ClassName: DictAction
 * </p>
 * <p>
 * Description: 字典Action
 * </p>
 */
@Controller
@RequestMapping("/main/system")
public class DictAction extends EntityHandler<Dict> {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(DictAction.class);

    /**
     * 字典service
     */
    @Autowired
    @Qualifier("dictService")
    private IDictService dictService;
    
    @Autowired
    @Qualifier("dictService")
    @Override
    public void setEntityServiceImpl(IEntityService<Dict> entityServiceImpl) {
        this.entityServiceImpl = entityServiceImpl;
        this.clazz = Dict.class;
    }
    
    /**
     * 操作日志OperateLogService
     */
    @Autowired
    @Qualifier("operateLogService")
    private IOperateLogService operateLogService;


    /**
     * 查询字典
     * 
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/getDict.mvc")
    @ResponseBody
    public void getDicts(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Filter filter;
        filter = createFilter(request);
        List<Dict> list;
        list = this.dictService.getDictForGrid(filter, Dict.class);
        MesBaseUtil.writeJsonStr(response,
                this.toJsonStrForGrid(list, filter.getPageInfo(), getJsonExcludedProperties()));
    }

    /**
     * <p>
     * Description: 建立filter
     * </p>
     * 
     * @param request 请求
     * @return 过滤器
     * @throws UnsupportedEncodingException 编码异常
     */
    private Filter createFilter(HttpServletRequest request) throws UnsupportedEncodingException {
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
        Map<String, Query> map;
        map = new HashMap<String, Query>();
        Query query;
        if (request.getParameter("dictName") != null && !request.getParameter("dictName").isEmpty()) {
            query = new Query(Condition.LIKE, request.getParameter("dictName"));
            map.put("dictName", query);
        }
        if (request.getParameter("parentKey") != null && !request.getParameter("parentKey").isEmpty()) {
            query = new Query(Condition.EQ, new String(
                    request.getParameter("parentKey").getBytes("iso-8859-1"), "utf-8"));
            map.put("parentKey", query);
        }
        filter.setQueryMap(map);
        return filter;
    }

    /**
     * 保存或更新字典
     * 
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/saveOrUpdateDict.mvc")
    @ResponseBody
    public void saveOrUpdateDict(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String rows;
        rows = WebUtil.fetch(request, "storeDate");
        List<Dict> objList;
        objList = JSON.parseArray(rows, Dict.class);
        Jdf3UserSession userSession;
        userSession = super.getJdf3UserSession(request);
        String result;
        result = this.dictService.saveOrUpdateDict(objList, userSession.getUserName());
        //增加操作日志
        if ("success".equals(result)) {
            List<OperateContent> list;
            list = new ArrayList<OperateContent>();
            for (Dict dict : objList) {
                OperateContent operateContent;
                operateContent = new OperateContent();
                operateContent.setOperateName(dict.getDictValue() + "-" + dict.getParentKey());
                if (StringUtils.isEmpty(dict.getUpdateUser())) {
                    operateContent.setOperateType(CommonConstants.LOG_CONTENT_INSERT);
                } else {
                    operateContent.setOperateType(CommonConstants.LOG_CONTENT_UPDATE);
                }
                list.add(operateContent);
            }
            this.operateLogService.insertOperateLog(request, userSession.getDispName(),
                    userSession.getUserId(), list);
        }
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, result));
    }

    /**
     * 删除字典索引
     * 
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/deleteDict.mvc")
    @ResponseBody
    public void deleteDict(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ids;
        ids = request.getParameter("ids");
        Jdf3UserSession userSession;
        userSession = super.getJdf3UserSession(request);
        String result;
        result = this.dictService.deleteDict(ids, userSession.getUserName());
        //增加操作日志
        if ("success".equals(result)) {
            String dictValues;
            dictValues = request.getParameter("dictValues");
            List<OperateContent> list;
            list = new ArrayList<OperateContent>();
            for (String dictValue : dictValues.split(",")) {
                OperateContent operateContent;
                operateContent = new OperateContent();
                operateContent.setOperateName(dictValue);
                operateContent.setOperateType(CommonConstants.LOG_CONTENT_DELETE);
                list.add(operateContent);
            }
            this.operateLogService.insertOperateLog(request, userSession.getDispName(),
                    userSession.getUserId(), list);
        }
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, result));
    }

    /**
     * <p>
     * Description: 设置构造json字符串的属性排除数组
     * </p>
     * 
     * @return 排除数组
     */
    protected Map<Class<?>, String[]> getJsonExcludedProperties() {
        Map<Class<?>, String[]> excludeMap;
        excludeMap = new HashMap<Class<?>, String[]>();
        excludeMap.put(User.class, new String[] { "roleList", "organList" });
        excludeMap.put(Role.class, new String[] { "userList" });
        excludeMap.put(Organ.class, new String[] { "children", "parentOrgan", "leaf", "userList" });
        return excludeMap;
    }

    /**
     * 
     * <p>
     * Description: 操作的返回信息
     * </p>
     * 
     * @param isSuccess 是否成功
     * @param message 返回消息
     * @return json格式字符串
     */
    protected String toJsonStrActionMessage(boolean isSuccess, String message) {
        return toJsonStrActionMessage(isSuccess, message, null, null, null);
    }

    /**
     * 
     * <p>
     * Description: 操作的返回信息
     * </p>
     * 
     * @param isSuccess 是否成功
     * @param message 返回消息
     * @param obj 返回对象
     * @param args 其他参数
     * @param excludes 转json字符串过程中要排除的属性，避免循环引用
     * @return json格式字符串
     */
    protected String toJsonStrActionMessage(boolean isSuccess, String message, Dict obj, Map<String, Object> args,
            Map<Class<?>, String[]> excludes) {
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
    
    /**
     * <p>Description: 进行唯一性检验</p>
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/checkDictUnique.mvc")
    @ResponseBody
    public void checkDictUnique(HttpServletRequest request, HttpServletResponse response) throws Exception {
        queryCheckUnique(request, response);
    }
}
