package com.swsm.system.service;

import com.core.exception.BaseException;
import com.swsm.system.model.VarConfig;

import java.util.List;


/**
 * <p>ClassName: IVarConfigService</p>
 * <p>Description: 配置项管理的接口类</p>
 */
public interface IVarConfigService {
    
    /**
     * 
     * <p>Description: 获取当前配置项下的所有子项</p>
     * @param parentId 父节点
     * @return 配置项信息
     * @throws BaseException 异常
     */
    public List<VarConfig> findVarConfigByPanrentId(String parentId) throws BaseException;
    
    /**
     * 
     * <p>Description: 删除配置项以及下面的所有子项</p>
     * @param id 配置项的id
     * @param userName 用户名
     * @return success
     * @throws BaseException 异常
     */
    public String deleteVarConfig(String id, String userName) throws BaseException;
    
    /**
     * 
     * <p>Description: 保存新增的配置项信息</p>
     * @param varConfig 配置项信息的集合
     * @param userName 用户名
     * @return success
     */
    public String saveVarConfig(VarConfig varConfig, String userName);
    
    /**
     * 
     * <p>Description: 更新修改的配置项信息</p>
     * @param varConfig 配置项信息的集合
     * @param userName 用户名 
     * @return success
     */
    public String updateVarConfig(VarConfig varConfig, String userName);
    
    /**
     * 
     * <p>Description: 配置项树子节点移动</p>
     * @param sourceNode 
     * @param targetNode
     * @param userName
     * @param dropPosition
     * @return
     */
    public String treeDropMove(String sourceNode, String targetNode, String userName, String dropPosition);
    
    /**
     * 
     * <p>Description: 配置项树子节点复制</p>
     * @param sourceNode
     * @param targetNode
     * @param userName
     * @param dropPosition
     * @return
     */
    public String treeDropCopy(String sourceNode, String targetNode, String userName, String dropPosition);

}
