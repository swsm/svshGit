package com.swsm.system.service;

import com.core.tools.Filter;
import com.swsm.system.model.Dict;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * <p>ClassName: DictService</p>
 * <p>Description: 字典明细的接口类</p>
 */
public interface IDictService {

    
    /**
     * <p>Description: 查询字典下所有的节点值</p>
     * @param parentKey 字典标识
     * @return 字典清单列表
     */
    public List<Dict> findAllDicts(String parentKey);
	
    /**
     * <p>Description: 检查字典是否重复</p>
     * @param filter 查询条件
     * @param dictId 主键
     * @param parentKey 所属字典的索引值
     * @param dictKey 字典键
     * @param dictValue 字典显示值
     * @throws Exception 异常信息
     * @return 重复了的字典
     */
    public Dict checkDict(Filter filter, String dictId, String parentKey, String dictKey, String dictValue)
            throws Exception;

	/**
	 * <p>Description: 根据字典键和索引代码查询对应的值</p>
	 * @param filter 查询条件
	 * @return 字典显示值
	 */
    public String findDictValueByKey(Filter filter);
    
    /**
     * <p>Description: 获取某一字典索引下的所有字典值</p>
     * @param filter 过滤器
     * @param dictClass 字典类 
     * @return 字典列表
     * @throws UnsupportedEncodingException 异常
     */
    public List<Dict> getDictForGrid(Filter filter, Class<Dict> dictClass) throws UnsupportedEncodingException;
    
    /**
     * <p>Description: 保存或更新字典</p>
     * @param objList 需要操作的字典对象列表
     * @param userName 登录人用户名
     * @return String 操作结果
     */
    public String saveOrUpdateDict(List<Dict> objList, String userName);
    
    /**
     * <p>Description: 删除字典</p>
     * @param ids 字典索引id 数组
     * @param userName 操作人
     * @return 操作结果
     */
    public String deleteDict(String ids, String userName);

    /**
     * 
     * <p>Description: 根据字典名称以及字典key值查找字典的value值</p>
     * @param parentKey 字典名
     * @param dictKey key
     * @return value
     */
    public String getDictValueByKey(String parentKey, String dictKey);

    /**
     * 
     * <p>Description: 根据字典名称以及字典value值查找字典的key值</p>
     * @param parentKey 字典名
     * @param dictValue value
     * @return key
     */
    public String getDictKeyByValue(String parentKey, String dictValue);

    /**
     * 
     * <p>
     * Description: 通过父key获得所有的字典对象
     * </p>
     * 
     * @param parentKey 父key
     * @return 结果集
     */
    public List<Dict> getDictListyByParentKey(String parentKey);
}
