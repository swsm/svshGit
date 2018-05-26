package com.core.action;

import com.alibaba.fastjson.JSON;
import com.core.session.Jdf3SessionManage;
import com.core.session.Jdf3UserSession;
import com.core.tools.Filter;
import com.core.tools.JsonUtil;
import com.core.tools.PageInfo;
import com.core.tools.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ClassName: BaseHandler
 * </p>
 * <p>
 * Description: 交互的handler父类，提供一些基础的方法
 * </p>
 */
public class BaseHandler {
    /**
     * 日志信息
     */
    private Logger logger = LoggerFactory.getLogger(BaseHandler.class);

    /**
     * 
     * <p>
     * Description: 设置验证的返回值
     * </p>
     * 
     * @param request 请求对象
     * @param result 返回对象
     * @return 返回是否成功
     * @throws Exception 异常
     */
    protected boolean doResponse(HttpServletRequest request, ValidateResult result) throws Exception {
        if (result != null) {
            request.setAttribute("validateSuccess", result.getValidateSuccess());
            request.setAttribute("validateMessage", result.getValidateMessage());
            if (!ValidateResult.SUCCESS.equals(result.getValidateSuccess())) {
                return false;
            } else {
                request.setAttribute("success", "success");
            }
        }
        return true;
    }

    /**
     * 
     * <p>
     * Description: List转jsonstr
     * </p>
     * 
     * @param list 需要转换的对象List
     * @param pageInfo 翻页消息
     * @return json格式的字符串
     */
    protected String toJsonStrForGrid(List list, PageInfo pageInfo) {
        String jsonStr = null;
        Map<String, Object> causeMaps;
        causeMaps = new HashMap<String, Object>();
        if (list != null) {
            causeMaps.put("total", pageInfo.count);
            causeMaps.put("rows", list);
            jsonStr = JsonUtil.getJsonStringFromMap(causeMaps);
        }
        return jsonStr;
    }

    /**
     * 
     * <p>
     * Description: List转jsonstr
     * </p>
     * 
     * @param list 需要转换的对象List
     * @param pageInfo 翻页消息
     * @param excludes 需要排除的属性
     * @return json格式的字符串
     */
    protected String toJsonStrForGrid(List list, PageInfo pageInfo, Map<Class<?>, String[]> excludes) {
        String jsonStr = null;
        Map<String, Object> causeMaps;
        causeMaps = new HashMap<String, Object>();
        if (list != null) {
            causeMaps.put("total", pageInfo.count);
            causeMaps.put("rows", list);
            jsonStr = JsonUtil.getJsonStringFromMap(causeMaps, excludes);
        }
        return jsonStr;
    }

    /**
     * 
     * <p>
     * Description: 组装query方法中的查询条件，形如 Map<String, Query> queryMap; queryMap =
     * new HashMap<String, Query>(); queryMap.put("workNo", new
     * Query(Condition.EQ, parameterMap.get("workNo"))); queryMap.put("delFlag",
     * new Query(Condition.EQ, EntityServiceImpl.DEL_FALSE));
     * 
     * </p>
     * 
     * @param request 请求对象
     * @return 查询条件列表
     */
    protected Map<String, com.core.tools.Query> doQueryList(HttpServletRequest request) {
        return new HashMap<String, com.core.tools.Query>();
    }

    /**
     * <p>
     * Description: 为避免循环嵌套，取得构造Json格式数据时需要排除的属性列表，不重写则默认为空数组，写法示意如下：
     * 
     * return new HashMap<Class<?>, String[]>() { { put(Role.class, new
     * String[]{"userList", "resList"}); } };
     * </p>
     * 
     * @return 要排除的属性列表
     */
    protected Map<Class<?>, String[]> getJsonExcludedProperties() {
        return new HashMap<Class<?>, String[]>();
    }

    /**
     * <p>
     * Description: 为避免循环嵌套，取得构造Json格式数据时需要排除的属性列表，不重写则默认为空数组，写法示意如下：
     * 
     * return new HashMap<Class<?>, String[]>() { { put(Role.class, new
     * String[]{"userList", "resList"}); } };
     * </p>
     * 
     * @return 要排除的属性列表
     */
    protected Map<Class<?>, String[]> getExcludedProperties(Class clazz) {
        Map<Class<?>, String[]> map = new HashMap<Class<?>, String[]>();
        map.put(clazz, JsonUtil.getExcludeFields(clazz));
        return map;
    }

    /**
     * <p>
     * Description: 将默认的request.getParameter进行调整，变成可读写的，并顺带将一个长度的字符串数组改为字符串
     * </p>
     * 
     * @param request http请求
     * @return 参数表
     */
    protected Map<String, Object> getParameterMap(HttpServletRequest request) {
        // 参数Map
        Map<String, Object> properties;
        properties = request.getParameterMap();
        // 返回值Map
        Map<String, Object> returnMap;
        returnMap = new HashMap<String, Object>();
        Iterator entries;
        entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj;
            valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
                returnMap.put(name, value);
            } else if (valueObj instanceof String[]) {
                String[] values;
                values = (String[]) valueObj;
                //如果长度为1，认为是一个字符串，而不是数组
                if (values.length == 1) {
                    returnMap.put(name, values[0]);
                } else {
                    returnMap.put(name, valueObj);
                }
            } else {
                value = valueObj.toString();
                returnMap.put(name, value);
            }
        }
        return returnMap;
    }

    /**
     * <p>
     * Description: 取得mock的查询条件
     * </p>
     * 
     * @param queryMap 查询条件
     * @return 查询条件
     */
    public static Filter getMockedFilter(Map<String, com.core.tools.Query> queryMap) {
        //新建过滤器
        Filter filter;
        filter = new Filter();
        //设置分页
        PageInfo pageInfo;
        pageInfo = new PageInfo();
        pageInfo.count = 10;
        pageInfo.end = 9999999;
        pageInfo.limit = 50;
        pageInfo.start = 0;
        filter.setPageInfo(pageInfo);
        //设置排序
        filter.setOrderStr(null);
        //设置查询条件
        if (queryMap != null) {
            for (String field : queryMap.keySet()) {
                if (queryMap.get(field) != null) {
                    filter.getQueryMap().put(field, queryMap.get(field));
                }
            }
        }
        return filter;
    }

    /**
     * 
     * <p>
     * Description: 初始化过滤器，将查询条件放到filter中，并从request中取得排序字符串，放到filter中
     * </p>
     * 
     * @param request 请求对象
     * @param queryMap 查询条件
     * @return 过滤器对象
     * @throws Exception 异常
     */
    protected static Filter doFilter(HttpServletRequest request, Map<String, com.core.tools.Query> queryMap) throws Exception {
        //新建过滤器
        Filter filter;
        filter = new Filter();
        //设置分页
        PageInfo pageInfo;
        pageInfo = WebUtil.getPage(request);
        filter.setPageInfo(pageInfo);
        //设置排序
        filter.setOrderStr(WebUtil.getSortInfo(request));
        //设置查询条件
        for (String field : queryMap.keySet()) {
            if (queryMap.get(field) != null) {
                filter.getQueryMap().put(field, queryMap.get(field));
            }
        }
        return filter;
    }

    /**
     * 
     * <p>
     * Description: json数组转java对象
     * </p>
     * 
     * @param jsonString json字符串
     * @param clazz 要转换的类
     * @return 转换后的类
     */
    protected static <T> List<T> getJsonArray(String jsonString, Class<T> clazz) {
        return JSON.parseArray(jsonString, clazz);
    }

    /**
     * <p>
     * Description: 取得默认的返回字符串，如果service层不想返回值，可以在action中用这个方法来返回值到response
     * </p>
     * 
     * @return 默认的返回字符串
     */
    protected String getDefaultRetStr() {
        Map<String, Object> causeMaps;
        causeMaps = new HashMap<String, Object>();
        causeMaps.put("success", true);
        causeMaps.put("message", "success");
        return JsonUtil.getJsonStringFromMap(causeMaps);
    }

    /**
     * 
     * <p>
     * Description: 获取用户信息
     * </p>
     * 
     * @return ['userId','userName','dispName','workNo']
     */
    public String[] getUserInfo(HttpServletRequest request) {
        Jdf3UserSession jdf3Usersession = null;
        jdf3Usersession = this.getJdf3UserSession(request);
        if (jdf3Usersession != null) {
            String userName = jdf3Usersession.getUserName();
            String userId = jdf3Usersession.getUserId();
            String dispName = jdf3Usersession.getDispName();
            String workNo = jdf3Usersession.getWorkNo();
            return new String[] { userId, userName, dispName, workNo };
        }
        return new String[4];
    }

    /**
     * 
     * <p>
     * Description: 获取Jdf3登录用户的session信息
     * </p>
     * 
     * @param request
     * @return
     */
    protected Jdf3UserSession getJdf3UserSession(HttpServletRequest request) {
        Jdf3SessionManage sessionManage = Jdf3SessionManage.getJdf3SessionManage();
        if (sessionManage != null) {
            Jdf3UserSession jdf3UserSession = sessionManage.getJdf3UserSession(request.getSession());
            return jdf3UserSession;
        } else {
            return null;
        }
    }
    /**
     * 
     * <p>Description: 获取当前登录用户的名称</p>
     * @param request
     * @return
     */
    protected String getCurrentLoginUserName(HttpServletRequest request) {
        String[] userInfo;
        userInfo = this.getUserInfo(request);
        return userInfo[1];
    }
}
