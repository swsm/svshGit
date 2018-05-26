package com.core.service;

import com.core.dao.impl.BaseDaoImpl;
import com.core.entity.BaseModel;
import com.core.exception.BaseDaoException;
import com.core.tools.Filter;
import com.core.tools.Query;
import org.hibernate.criterion.DetachedCriteria;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * ClassName: IEntityService
 * </p>
 * <p>
 * Description: 泛型的service
 * </p>
 * <p>
 * Author: Administrator
 * </p>
 */
public interface IEntityService<T extends BaseModel> extends IBaseService {

    public List<T> findByCriteria(DetachedCriteria criteria) throws BaseDaoException;

    /**
     * <p>
     * Description: 默认基于Hibernate的面向对象式来进行带条件的翻页查询
     * 如果需要基于SQL或HQL进行查询，则需要重写此方法，并调用findPagesListBySql或者findPagesListByHql方法
     * </p>
     *
     * @param filter 过滤器（包含查询条件queryMap和翻页信息PageInfo）
     * @param clazz 要查询的类
     * @return 查询结果
     * @throws Exception 运行时异常
     */
    public List<T> queryPagedEntityList(Filter filter, Class<T> clazz) throws Exception;

    /**
     * <p>
     * Description: 默认基于Hibernate的面向对象式来进行带条件的无翻页的查询 如果需要基于SQL或HQL进行查询，则需要重写此方法，
     * 并调用findEntityListBySql或者findEntityListByHql方法
     * </p>
     *
     * @param filter 过滤器
     * @param clazz 要查询的类
     * @return 查询结果
     * @throws Exception 异常
     */
    public List<T> queryEntityList(Filter filter, Class<T> clazz) throws Exception;

    /**
     *
     * <p>
     * Description: 保存对象
     * </p>
     *
     * @param clazz 要保存的类
     * @param paramterMap 参数表
     * @param userInfo 当前登陆用户信息数组，传入getUserInfo()即可
     * @return 保存后的对象
     * @throws InstantiationException 实例化异常
     * @throws IllegalAccessException 非法读取异常
     * @throws InvocationTargetException 无效反射异常
     */
    public T saveEntity(Class<T> clazz, Map<String, Object> paramterMap, String[] userInfo)
            throws IllegalAccessException, InstantiationException, InvocationTargetException;

    /**
     * <p>
     * Description: 批量插入对象
     * </p>
     *
     * @param clazz 要保存的类
     * @param objList 对象列表
     * @param userInfo 当前登陆用户信息数组，传入getUserInfo()即可
     * @throws InstantiationException 实例化异常
     * @throws IllegalAccessException 非法读取异常
     * @throws InvocationTargetException 无效反射异常
     */
    public void insertBatch(Class<T> clazz, List<T> objList, String[] userInfo) throws IllegalAccessException,
            InstantiationException, InvocationTargetException;

    /**
     * <p>
     * Description: 更新对象
     * </p>
     *
     * @param clazz 要保存的类
     * @param paramterMap 参数表
     * @param userInfo 当前登陆用户信息数组，传入getUserInfo()即可
     * @return 更新后的对象
     * @throws InstantiationException 实例化异常
     * @throws IllegalAccessException 非法读取异常
     * @throws InvocationTargetException 无效反射异常
     */
    public T updateEntity(Class<T> clazz, Map<String, Object> paramterMap, String[] userInfo)
            throws IllegalAccessException, InstantiationException, InvocationTargetException;

    /**
     * <p>
     * Description: 批量更新对象
     * </p>
     *
     * @param clazz 要保存的类
     * @param objList 对象列表
     * @param userInfo 当前登陆用户信息数组，传入getUserInfo()即可
     * @throws InstantiationException 实例化异常
     * @throws IllegalAccessException 非法读取异常
     * @throws InvocationTargetException 无效反射异常
     */
    public void updateBatch(Class<T> clazz, List<T> objList, String[] userInfo) throws IllegalAccessException,
            InstantiationException, InvocationTargetException;

    /**
     * <p>
     * Description: 保存或者更新单个对象
     * </p>
     *
     * @param clazz 要保存的类
     * @param paramterMap 参数表
     * @param userInfo 当前登陆用户信息数组，传入getUserInfo()即可
     * @throws InstantiationException 实例化异常
     * @throws IllegalAccessException 非法读取异常
     * @throws InvocationTargetException 无效反射异常
     */
    public T saveOrUpdate(Class<T> clazz, Map<String, Object> paramterMap, String[] userInfo) throws BaseDaoException,
            IllegalAccessException, InvocationTargetException, InstantiationException;

    /**
     * <p>
     * Description: 保存或者更新对象集合
     * </p>
     *
     * @param modelList 对象集合
     * @throws BaseDaoException 数据库操作异常
     */
    public void saveOrUpdate(List<T> modelList) throws BaseDaoException;

    /**
     *
     * <p>
     * Description: 逻辑删除，设置对象为删除状态
     * </p>
     *
     * @param t 需要删除的对象
     */
    public void deleteEntity(T t);

    /**
     *
     * <p>
     * Description: 逻辑删除，设置对象为删除状态
     * </p>
     *
     * @param clazz 需要删除的类
     * @param pkId 主键
     */
    public void deleteEntity(Class<T> clazz, String pkId);

    /**
     *
     * <p>
     * Description: 从数据中物理的删除对象
     * </p>
     *
     * @param t 需要删除的对象
     */
    public void realDeleteEntity(T t);

    /**
     * <p>
     * Description: 从数据中物理的删除对象
     * </p>
     *
     * @param clazz 需要删除的类
     * @param pkId 主键
     */
    public void realDeleteEntity(Class<T> clazz, String pkId);

    /**
     *
     * <p>
     * Description: 删除对象列表
     * </p>
     *
     * @param entityList 需要删除的对象列表
     */
    public void realDeleteEntity(List<T> entityList);

    /**
     *
     * <p>
     * Description: 删除对象列表
     * </p>
     *
     * @param entityList 需要删除的对象列表
     */
    public void deleteEntity(List<T> entityList);

    /**
     *
     * <p>
     * Description: 查询一个实体类对象
     * </p>
     *
     * @param clazz 实体类
     * @param pkId 主键
     * @return 查询出来的实体类对象
     */
    public T findEntity(Class<T> clazz, String pkId);

    /**
     * <p>
     * Description: 根据查询条件判断是否存在，用于查重
     * </p>
     *
     * @param queryMap 查询条件
     * @param clazz 要查询的类
     * @return 是否存在
     */
    public boolean checkExits(Map<String, Query> queryMap, Class<T> clazz);

    /**
     * <p>
     * Description: 是否有效查询
     * </p>
     *
     * @param fieldName 查询字段
     * @param queryMap 查询条件集合
     * @return 是否有效
     */
    public boolean checkValid(String fieldName, Map<String, Query> queryMap);

    /**
     *
     * <p>
     * Description: 对某一字段进行唯一性检验
     * </p>
     *
     * @param uniqueFields 要验证的字段
     * @param deleteList 删除对象
     * @param insertList 插入对象
     * @param updateList 更新对象
     * @param clazz 操作类
     * @return 检查结果
     */
    @SuppressWarnings("rawtypes")
    public boolean queryCheckUnique(String[] uniqueFields, List<Map> deleteList, List<Map> insertList,
                                    List<Map> updateList, Class<T> clazz);

    /**
     *
     * <p>
     * Description: 需要实现类自己注入需要的Dao
     * </p>
     *
     * @param baseDaoImpl 注入方法
     */
    public void setBaseDao(BaseDaoImpl<T, String> baseDaoImpl);


}
