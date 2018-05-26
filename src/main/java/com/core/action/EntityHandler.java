package com.core.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.core.entity.BaseModel;
import com.core.service.IEntityService;
import com.core.service.impl.EntityServiceImpl;
import com.core.tools.Filter;
import com.core.tools.JsonUtil;
import com.core.tools.WebUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * <p>
 * ClassName: EntityHandler
 * </p>
 * <p>
 * Description: 通用实体handler
 * </p>
 */
public abstract class EntityHandler<T extends BaseModel> extends BaseHandler {
    /**
     * 操作类别 新增
     */
    protected static final int SAVE = 1;
    /**
     * 操作类别 更新
     */
    protected static final int UPDATE = 2;
    /**
     * 操作类别 删除
     */
    protected static final int DELETE = 3;

    /**
     * 实体对应的service对象
     */
    protected IEntityService<T> entityServiceImpl;

    /**
     * 类对象
     */
    protected Class<T> clazz;

    /**
     * <p>
     * Description: 通用查询，查询一个实体
     * </p>
     * 
     * @param request 请求对象
     * @param response 返回对象
     * @throws Exception 异常
     */
    public void getOne(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pkId;
        pkId = request.getParameter(EntityServiceImpl.PK);
        //执行查询
        WebUtil.response(response, this.entityServiceImpl.findEntity(this.clazz, pkId));
    }

    /**
     * <p>
     * Description: 通用条件查询，如要组装查询条件，请重写doQueryList方法
     * </p>
     * 
     * @param request 请求对象
     * @param response 返回对象
     * @throws Exception 异常
     */
    public void query(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //构建过滤器
        Filter filter;
        //构建查询条件
        Map<String, com.core.tools.Query> queryList;
        queryList = doQueryList(request);
        filter = doFilter(request, queryList);

        List<T> list;
        list = this.entityServiceImpl.queryPagedEntityList(filter, this.clazz);
        //执行查询
        WebUtil.response(response, this.toJsonStrForGrid(list, filter.getPageInfo(), getJsonExcludedProperties()));
    }

    /**
     * <p>
     * Description: 用于在执行CRUD之前进行的业务验证
     * </p>
     * 
     * @param request 请求对象
     * @param response 返回对象
     * @param paramterMap 参数表
     * @param type 操作类型
     * @return 验证是否通过
     * @throws Exception 其他异常
     */
    protected boolean doValidate(HttpServletRequest request, HttpServletResponse response,
            Map<String, Object> paramterMap, int type) throws Exception {
        //返回对象
        ValidateResult vr = null;
        //判断操作类型
        switch (type) {
        case SAVE:
            vr = doSaveValidate(request, response, paramterMap);
            break;
        case UPDATE:
            vr = doUpdateValidate(request, response, paramterMap);
            break;
        case DELETE:
            vr = doDeleteValidate(request, response, paramterMap);
            break;
        default:
            //TODO 不处理
        }
        return doResponse(request, vr);
    }

    /**
     * 
     * <p>
     * Description: 删除操作的验证方法，如果需要验证一些删除前的先决条件，则重载此方法
     * </p>
     * 
     * @param request 请求对象
     * @param response 返回对象
     * @param paramterMap 参数表
     * @return 验证是否通过
     * @throws Exception 其他异常
     */
    protected ValidateResult doDeleteValidate(HttpServletRequest request, HttpServletResponse response,
            Map<String, Object> paramterMap) throws Exception {
        return null;
    }

    /**
     * <p>
     * Description: 更新操作的验证方法，如果要验证唯一性等需要重载此方法
     * 可参考com.broadtext.commonsys.user.wb.UserWebHandler.doUpdateValidate
     * </p>
     * 
     * @param request 请求对象
     * @param response 返回对象
     * @param paramterMap 参数表
     * @return 验证是否通过
     * @throws Exception 其他异常
     */
    protected ValidateResult doUpdateValidate(HttpServletRequest request, HttpServletResponse response,
            Map<String, Object> paramterMap) throws Exception {
        return null;
    }

    /**
     * <p>
     * Description: 新增保存操作的验证方法，如果要验证唯一性等需要重载此方法
     * 可参考com.broadtext.commonsys.user.wb.UserWebHandler.doSaveValidate
     * </p>
     * 
     * @param request 请求对象
     * @param response 返回对象
     * @param paramterMap 参数表
     * @return 验证是否通过
     * @throws Exception 其他异常
     */
    protected ValidateResult doSaveValidate(HttpServletRequest request, HttpServletResponse response,
            Map<String, Object> paramterMap) throws Exception {
        return null;
    }

    /**
     * <p>
     * Description: 执行插入后的操作
     * </p>
     * 
     * @param request 请求对象
     * @param response 返回对象
     * @param paramterMap 参数表
     * @throws Exception 其他异常
     */
    protected void doBeforeSave(HttpServletRequest request, HttpServletResponse response,
            Map<String, Object> paramterMap) throws Exception {
    }

    /**
     * <p>
     * Description: 执行插入后的操作
     * </p>
     * 
     * @param request 请求对象
     * @param response 返回对象
     * @param paramterMap 参数表
     * @throws Exception 其他异常
     */
    protected void doAfterSave(HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramterMap)
            throws Exception {
    }

    /**
     * <p>
     * Description: 通用实体保存方法
     * </p>
     * 
     * @param request 请求对象
     * @param response 返回对象
     * @throws Exception 异常
     */
    public void save(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> paramterMap;
        paramterMap = getParameterMap(request);
        doBeforeSave(request, response, paramterMap);
        //執行驗證操作
        if (!this.doValidate(request, response, paramterMap, SAVE)) {
            return;
        }
        //执行保存

        T t = this.entityServiceImpl.saveEntity(this.clazz, paramterMap, getUserInfo(request));

        doAfterSave(request, response, paramterMap);

        WebUtil.response(response, this.toJsonStrActionMessage(true, HandlerResult.SUCCESS, t));
    }

   
    /**
     * 
     * <p>
     * Description: 通用实体更新方法
     * </p>
     * 
     * @param request 请求对象
     * @param response 返回对象
     * @throws Exception 异常
     */
    public void update(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> paramterMap;
        paramterMap = getParameterMap(request);

        doBeforeUpdate(request, response, paramterMap);
        //執行驗證操作
        if (!this.doValidate(request, response, paramterMap, UPDATE)) {
            return;
        }
        T t = this.entityServiceImpl.updateEntity(this.clazz, paramterMap, getUserInfo(request));
        doAfterUpdate(request, response, paramterMap);

        //执行保存
        WebUtil.response(response,
                this.toJsonStrActionMessage(true, HandlerResult.SUCCESS, t, null, getJsonExcludedProperties()));
    }

    /**
     * <p>
     * Description: 执行更新前的操作
     * </p>
     * 
     * @param request 请求对象
     * @param response 返回对象
     * @param paramterMap 参数表
     * @throws Exception 异常
     */
    protected void doBeforeUpdate(HttpServletRequest request, HttpServletResponse response,
            Map<String, Object> paramterMap) throws Exception {
    }

    /**
     * <p>
     * Description: 执行更新后的操作
     * </p>
     * 
     * @param request 请求对象
     * @param response 返回对象
     * @param paramterMap 参数表
     * @throws Exception 异常
     */
    protected void doAfterUpdate(HttpServletRequest request, HttpServletResponse response,
            Map<String, Object> paramterMap) throws Exception {
    }

    /**
     * 
     * <p>
     * Description: 实体删除方法 如果页面上删除了X行，那么这个方法就可以获得对应的X行数据，并转化为对象列表，传到后台进行删除
     * </p>
     * 
     * @param request 请求对象
     * @param response 返回对象
     * @throws Exception 异常
     */
    public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取从前台传递过来的对象,并组装为数组
        List<T> entityList;
        String result;
        String gridStr;
        gridStr = WebUtil.fetch(request, getInstanceName() + "Grid");
        //批量删除
        if (StringUtils.isNotEmpty(gridStr)) {
            entityList = getJsonArray(gridStr, this.clazz);
            doBeforeDelete(request, response, entityList);
            //执行删除
            if (getFakeDelFlag()) {
                //假删除
                this.entityServiceImpl.deleteEntity(entityList);
            } else {
                this.entityServiceImpl.realDeleteEntity(entityList);
            }
            doAfterDelete(request, response, entityList);
        } else {
            String pkId;
            pkId = request.getParameter(EntityServiceImpl.PK);
            //执行删除
            if (getFakeDelFlag()) {
                //假删除
                this.entityServiceImpl.deleteEntity(this.clazz, pkId);
            } else {
                this.entityServiceImpl.realDeleteEntity(this.clazz, pkId);
            }
        }
        //执行保存
        WebUtil.response(response, this.toJsonStrActionMessage(true, HandlerResult.SUCCESS));
    }

    /**
     * <p>
     * Description: 执行删除前的操作
     * </p>
     * 
     * @param request 请求对象
     * @param response 返回对象
     * @param entityList 类对象列表
     * @throws Exception 异常
     */
    protected void doBeforeDelete(HttpServletRequest request, HttpServletResponse response, List<T> entityList)
            throws Exception {
    }

    /**
     * <p>
     * Description: 执行删除后的操作
     * </p>
     * 
     * @param request 请求对象
     * @param response 返回对象
     * @param entityList 类对象列表
     * @throws Exception 异常
     */
    protected void doAfterDelete(HttpServletRequest request, HttpServletResponse response, List<T> entityList)
            throws Exception {
    }

    /********************* 可编辑GRID的增删改方法 begin **************************/
    /**
     * 
     * <p>
     * Description: 通用实体批量插入方法（数据来自于可编辑grid）
     * 如果页面上插入了X行，那么这个方法就可以获得对应的X行数据，并转化为对象列表，传到后台进行插入
     * </p>
     * 
     * @param request 请求对象
     * @param response 返回对象
     * @throws Exception 异常
     */
    public void insertBatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String rows;
        rows = WebUtil.fetch(request, getInstanceName() + "Grid");
        List<T> objList;
        objList = JSON.parseArray(rows, this.clazz);
        this.entityServiceImpl.insertBatch(this.clazz, objList, getUserInfo(request));
        //执行保存
        WebUtil.response(response, this.toJsonStrActionMessage(true, HandlerResult.SUCCESS));
    }

    /**
     * 
     * <p>
     * Description: 通用实体批量更新方法（数据来自于可编辑grid）
     * 如果页面上改动了X行，那么这个方法就可以获得对应的X行数据，并转化为对象列表，传到后台进行更新
     * </p>
     * 
     * @param request 请求对象
     * @param response 返回对象
     * @throws Exception 异常
     */
    public void updateBatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String rows;
        rows = (String) WebUtil.fetch(request, getInstanceName() + "Grid");
        List<T> objList;
        objList = JSON.parseArray(rows, this.clazz);
        this.entityServiceImpl.updateBatch(this.clazz, objList, getUserInfo(request));
        //执行保存
        WebUtil.response(response, this.toJsonStrActionMessage(true, HandlerResult.SUCCESS));
    }

    /********************* 可编辑GRID的增删改方法 END **************************/

    /********************* 操作结果返回字符串的转换方法 begin **************************/

    /**
     * 
     * <p>
     * Description: List转jsonstr
     * </p>
     * 
     * @param t 需要转换的对象
     * @param excludes 需要排除的属性
     * @return json格式的字符串
     */
    protected String toJsonStr(T t, Map<Class<?>, String[]> excludes) {
        String jsonStr = null;
        Map<String, Object> causeMaps;
        causeMaps = new HashMap<String, Object>();
        if (t != null) {
            causeMaps.put("obj", t);
            jsonStr = JsonUtil.getJsonStringFromMap(causeMaps, excludes);
        }
        return jsonStr;
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
     * @return json格式字符串
     */
    protected String toJsonStrActionMessage(boolean isSuccess, String message, T obj) {
        return toJsonStrActionMessage(isSuccess, message, obj, null, null);
    }

    /**
     * 
     * <p>
     * Description: 操作的返回信息
     * </p>
     * 
     * @param isSuccess 是否成功
     * @param message 返回消息
     * @param excludes 需要排除的属性
     * @return json格式字符串
     */
    protected String toJsonStrActionMessage(boolean isSuccess, String message, Map<Class<?>, String[]> excludes) {
        return toJsonStrActionMessage(isSuccess, message, null, null, excludes);
    }

    /**
     * 
     * <p>
     * Description: 操作的返回信息
     * </p>
     * 
     * @param isSuccess 是否成功
     * @param message 返回消息
     * @param obj 对象
     * @param excludes 对象中要排除循环引用属性
     * @return json格式字符串
     */
    protected String toJsonStrActionMessage(boolean isSuccess, String message, T obj, Map<Class<?>, String[]> excludes) {
        return toJsonStrActionMessage(isSuccess, message, obj, null, excludes);
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
    protected String toJsonStrActionMessage(boolean isSuccess, String message, T obj, Map<String, Object> args,
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

    /********************* 操作结果返回字符串的转换方法 end **************************/

    /**
     * <p>
     * Description: 是真删除还是假删除,默认假删除
     * </p>
     * 
     * @return 删除类别
     */
    protected boolean getFakeDelFlag() {
        return true;
    }

    /**
     * <p>
     * Description: 根据类名取得实例对象名，比如： 你的类名叫User，那么返回user 你的类名叫Role，那么返回role
     * 你的类名叫VehicleModel，那么返回vehicleModel
     * </p>
     * 
     * @return 实例对象名
     */
    protected String getInstanceName() {
        String instanceName;
        instanceName = this.clazz.getSimpleName().substring(0, 1).toLowerCase()
                + this.clazz.getSimpleName().substring(1);

        return instanceName;
    }

    /**
     * 
     * <p>
     * Description: 用于实现类注入service实现功能
     * </p>
     * 
     * @param entityServiceImpl service实例
     */
    public abstract void setEntityServiceImpl(IEntityService<T> entityServiceImpl);

    /**
     * 
     * <p>
     * Description: 对某一字段进行唯一性检验
     * </p>
     * 
     * @param request 请求对象
     * @param response 返回对象
     * @throws Exception 异常
     */
    @SuppressWarnings("rawtypes")
    public void queryCheckUnique(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获得需要验证的字段
        String uniqueField;
        uniqueField = request.getParameter("uniqueField");
        String[] uniqueFields = uniqueField.split(",");
        //获得要删除的对象
        List<Map> deleteList;
        deleteList = this.getCheckUniqueFields("delete", request);
        //获得要插入的对象
        List<Map> insertList;
        insertList = this.getCheckUniqueFields("insert", request);
        //获得要更新的对象
        List<Map> updateList;
        updateList = this.getCheckUniqueFields("update", request);
        //进行验证查询
        boolean isUnique;
        isUnique = this.entityServiceImpl
                .queryCheckUnique(uniqueFields, deleteList, insertList, updateList, this.clazz);
        //封装查询结果并返回结果
        Map<String, Object> causeMaps;
        causeMaps = new HashMap<String, Object>();
        causeMaps.put("success", true);
        String result;
        if (isUnique) {
            result = HandlerResult.SUCCESS;
        } else {
            result = HandlerResult.FAILED;
        }
        causeMaps.put("message", result);
        WebUtil.response(response, JsonUtil.getJsonStringFromMap(causeMaps));
    }

    /**
     * 
     * <p>
     * Description: 根据要验证的类型以及字段获取相应的数据
     * </p>
     * 
     * @param checkField 验证的类型
     * @param request 请求对象
     * @return 相应的数据
     */
    @SuppressWarnings({ "rawtypes" })
    private List<Map> getCheckUniqueFields(String checkField, HttpServletRequest request) {
        //从前台获取数据
        String rltFields;
        rltFields = request.getParameter(checkField);
        JSONArray array;
        array = JSON.parseArray(rltFields);
        //转化成List<Map>形式
        List<Map> list;
        list = new LinkedList<Map>();
        if (array != null) {
            for (int i = 0; i < array.size(); i++) {
                Map rltMap;
                rltMap = (Map) JSON.parse(array.get(i).toString());
                list.add(rltMap);
            }
        }
        return list;
    }

}
