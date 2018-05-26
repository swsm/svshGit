package com.core.dao;

import com.core.exception.BaseDaoException;
import com.core.tools.Filter;
import com.core.tools.PageInfo;
import com.core.tools.Query;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @param <T>
 * @param <PK>
 */
public interface IBaseDao <T, PK extends Serializable> extends ICommonDao {
    
    /**
     * 
     * <p>Description: 指定的对象清除一级缓存p>
     * @param t
     * @throws BaseDaoException
     */
    public void evict(T t) throws BaseDaoException;
    /**
     * 
     * <p>Description: 保存一个model到数据库，功能类似于insert into table</p>
     * @param t model对象
     * @throws BaseDaoException 数据访问异常
     */
    public void save(T t) throws BaseDaoException;

    /**
     * 
     * <p>Description: 保存一批model，功能类似于批量insert</p>
     * @param modelList model对象集合
     * @throws BaseDaoException 数据访问异常
     */
    public void save(List<T> modelList) throws BaseDaoException;

    /**
     * 
     * <p>Description: 保存或更新一个model，功能类似于insert或update，根据数据库有无来自动判断</p>
     * @param t model对象
     * @throws BaseDaoException 数据访问异常
     */
    public void saveOrUpdate(T t) throws BaseDaoException;
    /**
     * 
     * <p>Description: 保存或更新一批model，功能类似于批量insert或update，根据数据库有无来自动判断</p>
     * @param modelList model对象集合
     * @throws BaseDaoException 数据访问异常
     */
    public void saveOrUpdate(List<T> modelList) throws BaseDaoException;

    /**
     * 
     * <p>Description: 更新一个model，功能类似于update</p>
     * @param t model对象
     * @throws BaseDaoException 数据访问异常
     */
    public void update(T t) throws BaseDaoException ;
    
    /**
     * 
     * <p>Description: 批量更新一批model，功能类似于批量update</p>
     * @param modelList model对象集合
     * @throws BaseDaoException 数据访问异常
     */
    public void update(List<T> modelList) throws BaseDaoException;
    /**
     * 
     * <p>Description: 删除一个model，功能类似于delete</p>
     * @param t model对象
     * @throws BaseDaoException 数据访问异常
     */
    public void delete(T t) throws BaseDaoException;

    /**
     * 
     * <p>Description: 删除一批model，功能类似于批量delete</p>
     * @param modelList model对象集合
     * @throws BaseDaoException 数据访问异常
     */
    public void delete(List<T> modelList) throws BaseDaoException;
    
    /**
     * <p>Description: 根据对象的id来删除对象</p>
     * @param modelClass 对象类
     * @param id 对象id
     * @throws BaseDaoException 数据访问异常
     */
    public void deleteById(Class<T> modelClass, PK id) throws BaseDaoException;

   
    /**
     * 
     * <p>Description: 通过主键查询model对象</p>
     * @param modelClass model类对象
     * @param id 主键
     * @return model对象
     * @throws BaseDaoException 数据访问异常
     */
    public T getById(Class<T> modelClass, PK id) throws BaseDaoException ;
    
    /**
     * 
     * <p>Description: 通过某字段查询model对象列表</p>
     * @param modelClass model类对象
     * @param propertyName 属性
     * @param value 值
     * @return model对象
     * @throws BaseDaoException 数据访问异常
     */
    public List<T> findByProperty(Class<T> modelClass, String propertyName, Object value);
    
    /**
     * 
     * <p>Description: 通过其他唯一标识查询model对象</p>
     * @param modelClass model类对象
     * @param propertyName 唯一标识属性
     * @param value 值
     * @return model对象
     * @throws BaseDaoException 数据访问异常
     */
    public T findUniqueByProperty(Class<T> modelClass, String propertyName, Object value);
 
    /**
     * 
     * <p>Description: 构造一个面向对象式的查询条件，可变参数</p>
     * @param modelClass model类对象
     * @param criterions 可变参数
     * @return 查询条件
     */
    public Criteria createCriteria(Class<T> modelClass, Criterion... criterions) ;
    
    /**
     * 
     * <p>Description: 面向对象式不翻页的查询，返回满足条件的全部记录</p>
     * @param detachedCriteria 查询对象
     * @return model对象列表
     * @throws BaseDaoException 数据访问异常
     */
    public List<T> findByCriteria(DetachedCriteria detachedCriteria) throws BaseDaoException;

    /**
     * <p>Description: 根据构造的面向对象式的查询条件进行翻页查询</p>
     * @param detachedCriteria 构造的查询条件组合
     * @param pageInfo 翻页信息
     * @return 查询结果
     * @throws BaseDaoException Dao异常
     */
    public List<T> findByCriteria(DetachedCriteria detachedCriteria, PageInfo pageInfo) throws BaseDaoException;
    
    /**
     * 根据属性值判断是否唯一，true表示唯一，false表示不唯一。
     * @param currentId 当前记录ID
     * @param uniquePropertys 属性键值对;例如：｛"categoryCode":"0001","":"categoryName":"A类"｝
     * @param isAnd 属性值间的关系，true表示and；false：or
     * @param clazz 要查询的类
     * @return 是否唯一
     */
    public boolean isUniqueByPropertys(String currentId, Map<String, Object> uniquePropertys, boolean isAnd, Class<T> clazz) throws BaseDaoException;
    
    /**
     * 根据属性值判断是否唯一，true表示唯一，false表示不唯一。
     * @param uniquePropertys 属性键值对;例如：｛"categoryCode":"0001","":"categoryName":"A类"｝
     * @param isAnd 属性值间的关系，true表示and；false：or
     * @param clazz 要查询的类
     * @return 是否唯一
     */
    public boolean isUniqueByPropertys(Map<String, Query> uniquePropertys, boolean isAnd, Class<T> clazz) throws BaseDaoException;

    /**
     *
     * <p>Description: 根据参数查询单条记录</p>
     * @param modelClass model类对象
     * @param params 参数键值对 例如：｛"categoryCode":"0001"} 默认eq 若使用in，参数值使用list
     * @param filter 分页排序条件 可为空
     * @return 符合条件的第一条记录
     */
    public T getOneByPropertys(Class<T> modelClass, Map<String, Object> params, Filter filter);
    
    /**
    *
    * <p>Description: 根据参数查询单条记录</p>
    * @param modelClass model类对象
    * @param params 参数键值对 例如：｛"categoryCode":"0001"} 默认eq 若使用in，参数值使用list
    * @return 符合条件的第一条记录
    */
   public T getOneByPropertys(Class<T> modelClass, Map<String, Object> params);
    
    /**
     * 
     * <p>Description:  根据参数查询表中记录</p>
     * @param modelClass model类对象
     * @param params 参数键值对 例如：｛"categoryCode":"0001"}  默认EQ 参数值为list时使用IN
     * @param filter 分页排序条件 为空查询全部
     * @return 返回查询对象列表
     */
    public List<T> getByPropertys(Class<T> modelClass, Map<String, Object> params, Filter filter);
   
    
}
