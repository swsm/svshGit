package com.swsm.system.main.service;


import com.core.exception.BaseException;
import com.swsm.system.system.model.Resource;

/**
 * <p>
 * ClassName: IFrameService
 * </p>
 * <p>
 * Description: 框架服务
 * </p>
 */
public interface IMainService {
    /**
     * 
     * <p>
     * Description: 根据parentId获取所有的孩子资源
     * </p>
     * 
     * @param parnetId 父节点
     * @return 资源列表
     * @throws BaseException 异常
     */
    public Resource[] getChildResource(String parnetId);

    /**
     * 
     * <p>
     * Description: 根据parentId获取指定用户的资源列表，admin用户拥有所有的权限
     * </p>
     * 
     * @param parnetId 父节点
     * @param loginName 当前用户信息
     */
    public Resource[] getResourcesByParentId(String parnetId, String loginName);

    /**
     * 
     * <p>
     * Description: 根据用户登录名获取用户有权限的资源列表
     * </p>
     * 
     * @param loginName 登录用户名
     * @return 拥有权限的资源集合
     * @throws BaseException 异常
     */
    public Resource[] getHavResByLoginName(String loginName) throws BaseException;
}
