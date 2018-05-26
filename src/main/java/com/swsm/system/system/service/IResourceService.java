package com.swsm.system.system.service;

import com.core.tools.Filter;
import com.swsm.system.system.model.Resource;

import java.util.List;
import java.util.Map;


/**
 * <p>ClassName: IResourceService</p>
 * <p>Description: 资源管理接口</p>
 */
public interface IResourceService {

    /**
     * <p>Description: 为角色选择资源时弹出的资源树窗口</p>
     * @param parentId 上级资源id
     * @param roleId 角色Id，用来判断是否默认选中某资源
     * @param flag 查询按钮标识
     * @return 资源树json字符串
     */
    public List<Resource> getResourceTreeByFilter(String parentId, String roleId, String flag);

    /**
     * <p>Description: 查询模块级资源，形成下拉框，便于过滤</p>
     * @param filter 查询条件
     * @return 模块级资源形成的json字符串
     */
    public List<Resource> getModulesByFilter(Filter filter);

    /**
     * <p>Description: 条件查询</p>
     * @param filter 过滤器
     * @param clazz 实体类
     * @return 查询结果
     * @throws Exception 运行时异常
     */
    public List<Resource> queryPagedEntityList(Filter filter, Class<Resource> clazz) throws Exception;

    /**
     * <p>Description: 为了形成上级资源的下拉树形选择框，需要排除自身资源</p>
     * @param resName 资源名称
     * @return 资源形成的json字符串
     */
    public List<Map<String, Object>> getResourceTreeForParentByFilter(String resName);

    /**
     * <p>Description: 新增或修改资源</p>
     * @param objList 需要操作的资源对象列表
     * @param userName 登录人用户名
     */
    public void saveOrUpdateResource(List<Resource> objList, String userName);

    /**
     * <p>Description: 删除对象列表</p>
     * @param list 需要删除的对象列表
     */
    public void realDeleteEntity(List<Resource> list);

    /**
     * <p>Description: 启用或禁用资源</p>
     * @param enabled 状态
     * @param ids 资源id数组
     */
    public void updateResourceEnabled(String enabled, String[] ids);

    /**
     * 
     * <p>
     * Description: 导出数据进行查询
     * </p>
     * 
     * @param filter 过滤器
     * @return 结果集
     */
    public List<Map<String, Object>> exportResourceList(Filter filter);

    /**
     * 
     * <p>
     * Description: 导出资源数 
     * </p>
     * 
     * @param roleId 角色
     * @return 结果集
     */
    public List<Map<String, Object>> getResourceTreeForExport(String roleId);

    /**
     * <p>Description: 为角色选择资源时弹出的资源树窗口</p>
     * @param parentId 上级资源id
     * @param roleId 角色Id，用来判断是否默认选中某资源
     * @param flag 查询按钮标识
     * @return 资源树json字符串
     */
    public List<Resource> getResourceTree(String parentId, String roleId, String flag);
    
}
