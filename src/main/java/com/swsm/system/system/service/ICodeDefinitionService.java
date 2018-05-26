package com.swsm.system.system.service;

import com.core.tools.Filter;
import com.swsm.system.system.model.CodeDefDetail;
import com.swsm.system.system.model.CodeDefinition;

import java.util.List;


/**
 * <p>ClassName: ICodeDefinitionService</p>
 * <p>Description: 编码定义接口定义</p>
 */
public interface ICodeDefinitionService {

    /**
     * 
     * <p>Description: 保存编码定义,区分新增、更新操作</p>
     * @param codeList 编码定义信息列表
     * @param userInfo 用户信息
     */
    public void saveCodeDefinition(List<CodeDefinition> codeList, String[] userInfo);
    
    /**
     * 
     * <p>Description:删除编码定义信息，同时删除明细信息</p>
     * @param ids 要删除的对象id数组
     */
    public void deleteCodeDefinition(String[] ids);
    
    /**
     * 
     * <p>Description: 根据条件分页查询编码定义信息</p>
     * @param filter 过滤器
     * @return 查询结果
     */
    public List<CodeDefinition> queryCodeDefinition(Filter filter);
    
    /**
     * 
     * <p>Description: 根据编码id获取明细信息</p>
     * @param codeId 编码id
     * @return 编码明细信息
     */
    public List<CodeDefDetail> getCodeDefDetail(String codeId);
    
    /**
     * 
     * <p>Description: 保存编码明细信息</p>
     * @param detList 编码明细信息
     * @param userInfo 用户信息
     */
    public void saveCodeDefDetail(List<CodeDefDetail> detList, String[] userInfo);
}
