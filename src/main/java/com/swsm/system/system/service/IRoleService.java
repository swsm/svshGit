package com.swsm.system.system.service;

import com.core.tools.Filter;
import com.swsm.system.system.model.Role;

import java.util.List;


/**
 * <p>ClassName: IRoleService</p>
 * <p>Description: 角色接口</p>
 */
public interface IRoleService {

    /**
     * <p>Description: 新增或修改角色</p>
     * @param objList 需要操作的角色对象列表
     * @param userName 登录人用户名
     */
    public void saveOrUpdateRole(List<Role> objList, String userName);

    /**
     * <p>Description: 删除角色</p>
     * @param ids 角色id数组
     */
    public void deleteRole(String[] ids);

    /**
     * <p>Description: 启用或禁用角色</p>
     * @param enabled 状态
     * @param ids 角色id数组
     */
    public void updateRoleEnabled(String enabled, String[] ids);

    /**
     * <p>
     * Description: 更改资源与角色的对应关系
     * </p>
     * @param roleId 角色id
     * @param resIdStr 资源id字符串
     */
    public void updateRoleResource(String roleId, String resIdStr);

    /**
     * <p>Description: 查询角色信息</p>
     * @param filter 过滤器
     * @return List<Role>
     */
    public List<Role> getRole(Filter filter) ;

    /**
     * 
     * <p>Description: 复制角色</p>
     * @param id 被复制的角色id
     * @param roleName 角色名
     * @param roleType 角色类别
     * @param ignoreDecesion 忽略决策
     * @param remark 备注
     */
    public void copyRole(String id, String roleName, String roleType, String ignoreDecesion, String remark);

    /**
     * 
     * <p>
     * Description: 根据id获得角色信息
     * </p>
     * 
     * @param roleId 角色id
     * @return 角色对象
     */
    public Role getRoleById(String roleId);

}
