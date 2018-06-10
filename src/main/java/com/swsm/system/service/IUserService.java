package com.swsm.system.service;

import com.core.tools.Filter;
import com.swsm.system.model.Role;
import com.swsm.system.model.User;
import org.hibernate.criterion.DetachedCriteria;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * ClassName: UserService
 * </p>
 * <p>
 * Description: 用户管理的业务接口
 * </p>
 */
public interface IUserService {
    
    /**
     * 启用或禁用用户
     * 
     * @param users 用户列表
     * @param status 状态
     */
    public void updateUserStatus(List<User> users, String status);

    /**
     * 
     * <p>
     * Description: 条件查询
     * </p>
     * 
     * @param filter 过滤器
     * @param clazz 要查询的类
     * @return 查询结果
     * @throws Exception 运行时异常
     */
    public List<User> queryPagedEntityListForUser(Filter filter, Class<User> clazz)
        throws Exception;

    /**
     * <p>
     * Description: 根据criteria对象获取实体
     * </p>
     * 
     * @param criteria criteria对象
     * @return list
     */
    public List<User> queryEntityByCriteria(DetachedCriteria criteria);

    /**
     * <p>
     * Description: 测试事务
     * </p>
     * 
     * @return 返回值
     */
    public String insertTest();

    /**
     * <p>
     * Description: 查询人员信息 人员共同选择器模块
     * </p>
     * 
     * @author liujie
     * @param filter 过滤器（包含查询条件queryMap和翻页信息PageInfo）
     * @param clazz 要查询的类
     * @return 查询结果
     * @throws Exception 运行时异常
     */
    public List<User> getUserForGrid(Filter filter, Class<User> clazz) throws Exception;

    /**
     * <p>
     * Description: 修改密码
     * </p>
     * 
     * @param newPassword 新密码
     * @param oldPassword 原始密码
     * @param username 用户名
     * @return String 操作结果
     */
    public String updatePassword(String newPassword, String oldPassword, String username);

    /**
     * <p>
     * Description: 密码规则
     * </p>
     * 
     * @param password 密码
     * @return 字符串
     */
    public String passwordRole(String password);

    /**
     * 
     * <p>
     * Description: 更新用户信息
     * </p>
     * @param paramterMap 用户信息
     * @param userInfo 当前用户信息
     * @return User 用户对象
     * @throws InvocationTargetException 异常
     * @throws IllegalAccessException 异常
     */
    public User updateUser(Map<String, Object> paramterMap, String[] userInfo) throws IllegalAccessException,
            InvocationTargetException;

    /**
     * <p>Description: 禁用用户 新框架</p>
     * @param ids  用户ids
     * @param username 操作人
     * @return String 操作结果
     */
    public String disableUser(String ids, String username);
    
    /**
     * <p>Description: 启用用户 新框架</p>
     * @param ids  用户ids
     * @param username 操作人
     * @return String 操作结果
     */
    public String enableUser(String ids, String username);
    
    /**
     * <p>Description: 删除用户 假删除 新框架</p>
     * @param ids  用户ids
     * @param username 操作人
     * @return String 操作结果
     */
    public String deleteUser(String ids, String username);

    /**
     * <p>Description: 获取角色 新框架</p>
     * @param filter 过滤器
     * @param roleClass Role类
     * @return role列表
     */
    public List<Role> getRoleForUserMng(Filter filter, Class<Role> roleClass);

    /**
     * <p>Description: 保存用户 新框架</p>
     * @param user 用户对象
     * @param username 操作人
     * @return String 操作结果
     */
    public String saveUser(User user, String username);
    
    /**
     * <p>Description: 更新用户 新框架</p>
     * @param user 需更新的用户对象
     * @param username 操作人
     * @return String 操作结果
     */
    public String updateUser(User user, String username);

    /**
     * <p>Description: 根据机构名称获取相近的机构id</p>
     * @param organName 机构名称
     * @return 相近的机构id 字符串
     */
    public String getLikeOrganByOrganName(String organName);
    
    
}
