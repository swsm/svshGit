package com.swsm.system.service;

import com.core.exception.BaseException;
import com.swsm.system.model.Organ;

import java.util.List;


/**
 * <p>ClassName: IOrganService</p>
 * <p>Description: 机构服务接口</p>
 */
public interface IOrganService {
    
    /**
     * <p>Description: 新框架  根据parentId获取其下所有子部门</p>
     * @param parentId 父部门id
     * @return 部门列表
     * @throws BaseException 异常
     */
    public List<Organ> findOrganByPanrentId(String parentId) throws BaseException;

    /**
     * <p>Description: 删除某个部门(假删除) 机构管理模块</p>
     * @param id 部门id
     * @param username 操作人
     * @return 查询结果
     * @throws BaseException 异常
     */
    public String deleteOrgan(String id, String username) throws BaseException;

    /**
     * <p>Description: 保存Organ 新框架</p>
     * @param organ 前台输入值后封装的organ对象
     * @param userName 操作人
     * @return 操作结果
     */
    public String saveOrgan(Organ organ, String userName);
    
    /**
     * <p>Description: 更新Organ 新框架</p>
     * @param organ 前台输入值后封装的organ对象
     * @param username 操作人
     * @return 操作结果
     */
    public String updateOrgan(Organ organ, String username);
    
    /**
     * <p>Description: 机构模块 树 移动节点</p>
     * @param sourceNode 被移动节点id
     * @param targetNode 目标节点id
     * @param username 操作人
     * @param dropPosition 在node的操作位置
     * @return 操作结果
     */
    public String treeDropMove(String sourceNode, String targetNode, String username, String dropPosition);
    
    /**
     * <p>Description: 机构模块 树 复制节点</p>
     * @param sourceNode 被移动节点id
     * @param targetNode 目标节点id
     * @param username 操作人
     * @param dropPosition 在node的操作位置
     * @return 操作结果
     */
    public String treeDropCopy(String sourceNode, String targetNode, String username, String dropPosition);


}
