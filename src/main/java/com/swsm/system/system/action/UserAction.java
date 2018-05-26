package com.swsm.system.system.action;

import com.core.action.BaseHandler;
import com.core.service.impl.EntityServiceImpl;
import com.core.session.Jdf3UserSession;
import com.core.tools.*;
import com.core.tools.file.ExcelUtil;
import com.swsm.system.constant.CommonConstants;
import com.swsm.system.log.model.OperateContent;
import com.swsm.system.log.service.IOperateLogService;
import com.swsm.system.login.session.SessionManage;
import com.swsm.system.login.session.UserSession;
import com.swsm.system.system.model.DictIdx;
import com.swsm.system.system.model.Organ;
import com.swsm.system.system.model.Role;
import com.swsm.system.system.model.User;
import com.swsm.system.system.service.IDictService;
import com.swsm.system.system.service.IUserService;
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
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * ClassName: UserAction
 * </p>
 * <p>
 * Description: 用户Action
 * </p>
 */
@Controller
@RequestMapping("/main/system")
public class UserAction extends BaseHandler {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(UserAction.class);

    /**
     * 员工service
     */
    @Autowired
    @Qualifier("userService")
    private IUserService userService;

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
     * 获取用户信息
     * 
     * @param request 请求
     * @param response 回答
     * @throws Exception 异常
     */
    @RequestMapping(value = "/getUser.mvc")
    @ResponseBody
    public void getUserInfos(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Filter filter;
        filter = createFilter(request);
        List<User> list;
        list = this.userService.queryPagedEntityListForUser(filter, User.class);
        String gridJsonStr = this.toJsonStrForGrid(list, filter.getPageInfo(), this.getExcludedProperties(User.class));
        logger.info(gridJsonStr);
        MesBaseUtil.writeJsonStr(response, gridJsonStr);
    }

    /**
     * <p>
     * Description: 创建过滤器
     * </p>
     * 
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
        //limit = "20";
        if (start != null) {
            filter.getPageInfo().start = Integer.parseInt(start);
        }
        if (limit != null) {
            filter.getPageInfo().limit = Integer.parseInt(limit);
        }
        //用户名
        Map<String, Query> map = new HashMap<String, Query>();
        if (request.getParameter("username") != null && !request.getParameter("username").isEmpty()) {
            Query query;
            query = new Query(Condition.LIKE, request.getParameter("username"));
            map.put("username", query);
        }
        //姓名
        if (request.getParameter("truename") != null && !request.getParameter("truename").isEmpty()) {
            Query query;
            query = new Query(Condition.LIKE, request.getParameter("truename"));
            map.put("truename", query);
        }
        //工号
        if (request.getParameter("workNo") != null && !request.getParameter("workNo").isEmpty()) {
            Query query;
            query = new Query(Condition.LIKE, request.getParameter("workNo"));
            map.put("workNo", query);
        }
        //机构
        if (request.getParameter("organName") != null && !request.getParameter("organName").isEmpty()) {
            Query query;
            query = new Query(Condition.IN, this.userService.getLikeOrganByOrganName(request.getParameter("organName")));
            map.put("organIds", query);
        }
        if (request.getParameter("roleName") != null && !request.getParameter("roleName").isEmpty()) {
            Query query;
            query = new Query(Condition.LIKE, request.getParameter("roleName"));
            map.put("roleName", query);
        }
        filter.setQueryMap(map);
        return filter;
    }

    /**
     * 禁用用户 新框架
     * 
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/disableUser.mvc")
    @ResponseBody
    public void disableUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ids;
        ids = request.getParameter("userIds");
        //        SessionManage sessionManage;
        //        sessionManage = SessionManage.getSessionManage();
        //        UserSession userSession;
        //        userSession = sessionManage.getUserSession(request.getSession());
        Jdf3UserSession userSession;
        userSession = super.getJdf3UserSession(request);
        String result;
        result = this.userService.disableUser(ids, userSession.getUserName());
        //增加操作日志
        if ("success".equals(result)) {
            String usernames;
            usernames = request.getParameter("usernames");
            List<OperateContent> list;
            list = new ArrayList<OperateContent>();
            for (String username : usernames.split(",")) {
                OperateContent operateContent;
                operateContent = new OperateContent();
                operateContent.setOperateName(username);
                operateContent.setOperateType(CommonConstants.LOG_CONTENT_DISABLE);
                list.add(operateContent);
            }
            this.operateLogService.insertOperateLog(request, userSession.getDispName(),
                    userSession.getUserId(), list);
        }
        WebUtil.response(response, this.toJsonStrActionMessage(true, result));
    }

    /**
     * 启用用户 新框架
     * 
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/enableUser.mvc")
    @ResponseBody
    public void enableUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ids;
        ids = request.getParameter("userIds");
        Jdf3UserSession userSession;
        userSession = super.getJdf3UserSession(request);
        String result;
        result = this.userService.enableUser(ids, userSession.getUserName());
        //增加操作日志
        if ("success".equals(result)) {
            String usernames;
            usernames = request.getParameter("usernames");
            List<OperateContent> list;
            list = new ArrayList<OperateContent>();
            for (String username : usernames.split(",")) {
                OperateContent operateContent;
                operateContent = new OperateContent();
                operateContent.setOperateName(username);
                operateContent.setOperateType(CommonConstants.LOG_CONTENT_ENABLE);
                list.add(operateContent);
            }
            this.operateLogService.insertOperateLog(request, userSession.getDispName(),
                    userSession.getUserId(), list);
        }
        WebUtil.response(response, this.toJsonStrActionMessage(true, result));
    }

    /**
     * 删除用户 假删除 新框架
     * 
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/deleteUser.mvc")
    @ResponseBody
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ids;
        ids = request.getParameter("userIds");
        Jdf3UserSession userSession;
        userSession = super.getJdf3UserSession(request);
        String result;
        result = this.userService.deleteUser(ids, userSession.getUserName());
        //增加操作日志
        if ("success".equals(result)) {
            String usernames;
            usernames = request.getParameter("usernames");
            List<OperateContent> list;
            list = new ArrayList<OperateContent>();
            for (String username : usernames.split(",")) {
                OperateContent operateContent;
                operateContent = new OperateContent();
                operateContent.setOperateName(username);
                operateContent.setOperateType(CommonConstants.LOG_CONTENT_DELETE);
                list.add(operateContent);
            }
            this.operateLogService.insertOperateLog(request, userSession.getDispName(),
                    userSession.getUserId(), list);
        }
        WebUtil.response(response, this.toJsonStrActionMessage(true, result));
    }

    /**
     * 查询角色 新框架 用户管理模块
     * 
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/getRoleForUserMng.mvc")
    @ResponseBody
    public void getRoleForUserMng(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Filter filter;
        filter = new Filter();
        filter.getPageInfo().start = 0;
        filter.getPageInfo().limit = 9999;
        List<Role> list;
        list = this.userService.getRoleForUserMng(filter, Role.class);
        String gridJsonStr;
        gridJsonStr = this.toJsonStrForGrid(list, filter.getPageInfo(), this.getExcludedProperties(User.class));
        logger.info(gridJsonStr);
        MesBaseUtil.writeJsonStr(response, gridJsonStr);
    }

    /**
     * <p>
     * 保存用户 新框架
     * </p>
     * 
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/saveUser.mvc")
    @ResponseBody
    public void saveUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user;
        user = new User();
        user.setUsername(request.getParameter("username"));
        user.setTruename(request.getParameter("truename"));
        user.setWorkNo(request.getParameter("workNo"));
        user.setPassword(MD5Util.getDigest(request.getParameter("password")));
        user.setMobile(request.getParameter("mobile"));
        user.setTelephone(request.getParameter("telephone"));
        user.setEmail(request.getParameter("email"));
        user.setAddress(request.getParameter("address"));
        user.setEnabled(request.getParameter("enable"));
        user.setWechatName(request.getParameter("wechatName"));
        user.setRemark(request.getParameter("remark"));

        //所属机构
        Set<Organ> organSet;
        organSet = new HashSet<Organ>();
        organSet.add(new Organ(request.getParameter("organId")));
        user.setOrganList(organSet);

        //所属角色
        Set<Role> roleSet;
        roleSet = new HashSet<Role>();
        for (String s : request.getParameter("role").split(",")) {
            roleSet.add(new Role(s));
        }
        user.setRoleList(roleSet);

        Jdf3UserSession userSession;
        userSession = super.getJdf3UserSession(request);
        String result = "success";
        result = this.userService.saveUser(user, userSession.getUserName());
        //增加操作日志
        if ("success".equals(result)) {
            List<OperateContent> list;
            list = new ArrayList<OperateContent>();
            OperateContent operateContent;
            operateContent = new OperateContent();
            operateContent.setOperateName(user.getUsername());
            operateContent.setOperateType(CommonConstants.LOG_CONTENT_INSERT);
            list.add(operateContent);
            this.operateLogService.insertOperateLog(request, userSession.getDispName(),
                    userSession.getUserId(), list);
        }
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, result));
    }

    /**
     * 更新用户 新框架
     * 
     * @param request 请求
     * @param response 回应
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/updateUser.mvc")
    @ResponseBody
    public void updateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user;
        user = ((EntityServiceImpl<User>) this.userService)
                .findEntity(User.class, request.getParameter("userId"));
        user.setUsername(request.getParameter("username"));
        user.setTruename(request.getParameter("truename"));
        user.setWorkNo(request.getParameter("workNo"));
        if (!user.getPassword().equals(request.getParameter("password"))) {
            //用户更改了密码
            user.setPassword(MD5Util.getDigest(request.getParameter("password")));
        }
        user.setMobile(request.getParameter("mobile"));
        user.setTelephone(request.getParameter("telephone"));
        user.setEmail(request.getParameter("email"));
        user.setAddress(request.getParameter("address"));
        user.setEnabled(request.getParameter("enable"));
        user.setWechatName(request.getParameter("wechatName"));
        user.setRemark(request.getParameter("remark"));

        //所属机构
        Set<Organ> organSet;
        organSet = new HashSet<Organ>();
        organSet.add(new Organ(request.getParameter("organId")));
        user.setOrganList(organSet);

        //所属角色
        Set<Role> roleSet;
        roleSet = new HashSet<Role>();
        for (String s : request.getParameter("role").split(",")) {
            roleSet.add(new Role(s));
        }
        user.setRoleList(roleSet);

        Jdf3UserSession userSession;
        userSession = super.getJdf3UserSession(request);
        String result = "success";
        result = this.userService.updateUser(user, userSession.getUserName());
        //增加操作日志
        if ("success".equals(result)) {
            OperateContent operateContent;
            operateContent = new OperateContent();
            operateContent.setOperateName(user.getUsername());
            operateContent.setOperateType(CommonConstants.LOG_CONTENT_UPDATE);
            this.operateLogService.insertOperateLog(request, userSession.getDispName(),
                    userSession.getUserId(), operateContent);
        }
        MesBaseUtil.writeJsonStr(response, this.toJsonStrActionMessage(true, result));
    }
    

    /**
     * 
     * <p>Description: 登录用户修改密码</p>
     * @author guhuajie 
     * @param request 请求
     * @param response 应答
     * @throws Exception 异常处理
     */
    @RequestMapping(value = "/updatePassword.mvc")
    @ResponseBody
    public void updatePassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String newPassword = null;
        if (request.getParameter("newPassword") != null && !request.getParameter("newPassword").isEmpty()) {
            newPassword = request.getParameter("newPassword");
        }
        String newPw;
        newPw = MD5Util.getDigest(newPassword);
        String oldPassword = null;
        if (request.getParameter("oldPassword") != null && !request.getParameter("oldPassword").isEmpty()) {
            oldPassword = request.getParameter("oldPassword");
        }
        String oldPw;
        oldPw = MD5Util.getDigest(oldPassword);
        SessionManage sessionManage;
        sessionManage = SessionManage.getSessionManage();
        UserSession userSession;
        userSession = sessionManage.getUserSession(request.getSession());
        
        //执行保存
        String result = "success";
        result = this.userService.updatePassword(newPw, oldPw, userSession.getLoginName());
        //增加操作日志
        if ("success".equals(result)) {
            List<OperateContent> list;
            list = new ArrayList<OperateContent>();
            OperateContent operateContent;
            operateContent = new OperateContent();
            operateContent.setOperateName(userSession.getDispName());
            operateContent.setOperateType(CommonConstants.LOG_CONTENT_INSERT);
            list.add(operateContent);
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
    protected String toJsonStrActionMessage(boolean isSuccess, String message, DictIdx obj, Map<String, Object> args,
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
     * <p>Description: 导出用户信息</p>
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
        List<User> list;
        list = this.userService.queryPagedEntityListForUser(filter, User.class);
        for (User user : list) {
            if (user.getEnabled() != null) {
                user.setEnabled(this.dictServiceImpl.getDictValueByKey("BOOL_FLAG", user.getEnabled()));
            }
        }
        columnNames = new String[] { "工号", "姓名", "用户名", "所属机构", "微信名", "角色", "是否启用", "手机", "联系电话", "联系地址", "邮箱地址",
                "创建日期" };
        fileNames = new String[] { "workNo", "truename", "username", "organName", "wechatName", "roleNameStr",
                "enabled", "mobile", "telephone", "address", "email" , "createDate"};
        //添加日志
//        Jdf3UserSession userSession;
//        userSession = super.getJdf3UserSession(request);
//        List<OperateContent> ocList;
//        ocList = new ArrayList<OperateContent>();
//        OperateContent operateContent;
//        operateContent = new OperateContent();
//        operateContent.setOperateName("用户信息");
//        operateContent.setOperateType(CommonConstants.LOG_CONTENT_EXPORT);
//        ocList.add(operateContent);
//        request.setAttribute("modual", "用户管理");
//        this.operateLogService.insertOperateLog(request, userSession.getDispName(),
//                userSession.getUserId(), ocList);
        ExcelUtil.exportExcel(response, list, User.class, columnNames, fileNames,
                "用户信息" + dateFormat.format(new Date()));
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
            String username;
            username = URLDecoder.decode(request.getParameter("username"), "utf-8");
            if (username != null && !username.isEmpty()) {
                Query query;
                query = new Query(Condition.LIKE, username);
                queryMap.put("username", query);
            }
            //姓名
            String truename;
            truename = URLDecoder.decode(request.getParameter("truename"), "utf-8");
            if (truename != null && !truename.isEmpty()) {
                Query query;
                query = new Query(Condition.LIKE, truename);
                queryMap.put("truename", query);
            }
            //工号
            String workNo;
            workNo = URLDecoder.decode(request.getParameter("workNo"), "utf-8");
            if (workNo != null && !workNo.isEmpty()) {
                Query query;
                query = new Query(Condition.LIKE, workNo);
                queryMap.put("workNo", query);
            }
            //机构
            String organName;
            organName = URLDecoder.decode(request.getParameter("organName"), "utf-8");
            if (organName != null && !organName.isEmpty()) {
                Query query;
                query = new Query(Condition.IN, this.userService.getLikeOrganByOrganName(organName));
                queryMap.put("organIds", query);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return queryMap;
    }

}
