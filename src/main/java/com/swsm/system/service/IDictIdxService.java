package com.swsm.system.service;


import com.core.tools.Filter;
import com.swsm.system.model.DictIdx;

import java.util.List;


/**
 * <p>ClassName: DictIdxService</p>
 * <p>Description: 字典索引的业务接口类</p>
 */
public interface IDictIdxService {

    
    /**
     * <p>Description: 检查字典索引是否重复</p>
     * @param filter 查询条件
     * @param dictId 主键
     * @param dictKey 字典标识
     * @param dictName 字典名称
     * @return 重复了的字典
     * @throws Exception 异常信息
     */
    public DictIdx checkDictIdx(Filter filter, String dictId, String dictKey, String dictName) throws Exception;
    
    /**
     * <p>Description: 查询字典索引</p>
     * @param filter 过滤器
     * @param dictIdx 字典索引
     * @return 字典索引列表
     */
    public List<DictIdx> getDictIdxForGrid(Filter filter, Class<DictIdx> dictIdx);
    

    /**
     * <p>Description: 保存或更新字典索引</p>
     * @param objList 需要操作的字典索引对象列表
     * @param userName 登录人用户名
     * @return String 操作结果
     */
    public String saveOrUpdateDictIndex(List<DictIdx> objList, String userName);
    
    /**
     * <p>Description: 保存或更新字典索引 测试</p>
     * @param dictIndex 需要操作的字典索引对象
     * @param oldDictKey 旧的dictKey
     * @param newDictKey 更新的dictKey
     * @param userName 登录人用户名
     * @return String 操作结果
     */
    public String saveOrUpdateDictIndexDemo(DictIdx dictIndex, String oldDictKey, String newDictKey, String userName);

    /**
     * <p>Description: 删除字典索引</p>
     * @param ids 字典索引id 数组
     * @param username 操作人
     * @return 操作结果
     */
    public String deleteDictIndex(String ids, String username);

}
