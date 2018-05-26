package com.core.service.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.core.entity.BaseModel;
import com.core.exception.BaseDaoException;
import com.core.exception.BaseException;
import com.core.service.IEntityService;
import com.core.tools.Condition;
import com.core.tools.DateUtil;
import com.core.tools.Filter;
import com.core.tools.Query;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * ClassName: EntityServiceImpl
 * </p>
 * <p>
 * Description: 通用实体操作类
 * </p>
 */
public abstract class EntityServiceImpl<T extends BaseModel> extends BaseServiceImpl implements
        IEntityService<T> {

    /**
     * 注入数据库访问对象
     */
    protected BaseDaoImpl<T, String> baseDaoImpl;

    /**
     * <p>
     * Description: 取得Hibernate的数据库操作对象
     * </p>
     *
     * @return Hibernate的数据库操作对象
     */
    protected Session getSession() {
        return baseDaoImpl.getSessionFactory().getCurrentSession();
    }

    /**
     *
     * <p>
     * Description: 在外部构造好面向对象式查询条件后进行查询，不翻页
     * </p>
     *
     * @param criteria 查询对象
     * @return model对象列表
     * @throws BaseDaoException 数据访问异常
     */
    @SuppressWarnings("unchecked")
    public List<T> findByCriteria(DetachedCriteria criteria) throws BaseDaoException {
        return (List<T>) this.baseDaoImpl.getHibernateTemplate().findByCriteria(criteria);
    }

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
    public List<T> queryPagedEntityList(Filter filter, Class<T> clazz) throws Exception {
        //创建查询对象
        DetachedCriteria criteria;
        criteria = DetachedCriteria.forClass(clazz);
        //封装过滤器、传入查询条件
        doFilter(filter, criteria);
        //过滤逻辑删除
        criteria.add(Property.forName("delFlag").eq(DEL_FALSE));
        //执行查询
        List<T> list;
        list = this.baseDaoImpl.findByCriteria(criteria, filter.getPageInfo());
        //将查询列表返回
        return list;
    }

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
    public List<T> queryEntityList(Filter filter, Class<T> clazz) throws Exception {
        //创建查询对象
        DetachedCriteria criteria;
        criteria = DetachedCriteria.forClass(clazz);
        //封装过滤器、传入查询条件
        doFilter(filter, criteria);
        //过滤逻辑删除
        criteria.add(Property.forName("delFlag").eq(DEL_FALSE));
        //执行查询
        List<T> list;
        list = this.baseDaoImpl.findByCriteria(criteria);
        return list;
    }


    public T saveEntity(Class<T> clazz, Map<String, Object> paramterMap, String[] userInfo)
            throws InvocationTargetException, IllegalAccessException, InstantiationException {
        T t;
        t = (T) clazz.newInstance();
        BeanUtils.populate(t, paramterMap);
        t.setDelFlag(DEL_FALSE);
        t.setCreateDate(new Date());
        t.setCreateUser(userInfo[1]);
        this.baseDaoImpl.save(t);
        return t;
    }

    /**
     * <p>
     * Description: 批量插入对象
     * </p>
     *
     * @param clazz 要保存的类
     * @param objList 对象列表
     * @param userInfo 当前登陆用户信息数组，传入getUserInfo()即可
     * @throws BaseException 异常
     */
    public void insertBatch(Class<T> clazz, List<T> objList, String[] userInfo) throws BaseException {
        for (T t : objList) {
            t.setDelFlag(DEL_FALSE);
            t.setCreateDate(new Date());
            t.setCreateUser(userInfo[1]);
            t.setUpdateDate(new Date());
            t.setUpdateUser(userInfo[1]);
        }
        this.baseDaoImpl.save(objList);
    }

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
            throws IllegalAccessException, InstantiationException, InvocationTargetException {

        T t;
        t = this.baseDaoImpl.getById(clazz, (String) paramterMap.get(PK));

        BeanUtils.populate(t, paramterMap);
        t.setDelFlag(DEL_FALSE);
        t.setUpdateDate(new Date());
        t.setUpdateUser(userInfo[1]);

        this.baseDaoImpl.update(t);

        return t;
    }

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
            InstantiationException, InvocationTargetException {
        for (T t : objList) {
            t.setDelFlag(DEL_FALSE);
            t.setUpdateDate(new Date());
            t.setUpdateUser(userInfo[1]);
        }
        this.baseDaoImpl.update(objList);
    }

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
    public T saveOrUpdate(Class<T> clazz, Map<String, Object> paramterMap, String[] userInfo) throws
            IllegalAccessException, InvocationTargetException, InstantiationException {
        String pkId;
        pkId = (String) paramterMap.get(PK);
        T t;
        if (StringUtils.isEmpty(pkId)) {
            t = (T) clazz.newInstance();
            BeanUtils.populate(t, paramterMap);
            t.setDelFlag(DEL_FALSE);
            t.setCreateDate(new Date());
            t.setCreateUser(userInfo[1]);
        } else {
            t = this.baseDaoImpl.getById(clazz, pkId);
            BeanUtils.populate(t, paramterMap);
            t.setDelFlag(DEL_FALSE);
            t.setUpdateDate(new Date());
            t.setUpdateUser(userInfo[1]);
        }

        this.baseDaoImpl.saveOrUpdate(t);
        return t;
    }

    /**
     * <p>
     * Description: 保存或者更新对象集合
     * </p>
     *
     * @param modelList 对象集合
     * @throws BaseDaoException 数据库操作异常
     */
    public void saveOrUpdate(List<T> modelList) throws BaseDaoException {
        this.baseDaoImpl.saveOrUpdate(modelList);
    }

    /**
     *
     * <p>
     * Description: 逻辑删除，设置对象为删除状态
     * </p>
     *
     * @param t 需要删除的对象
     */
    public void deleteEntity(T t) {
        t.setDelFlag(DEL_TRUE);
        this.baseDaoImpl.update(t);
    }

    /**
     *
     * <p>
     * Description: 逻辑删除，设置对象为删除状态
     * </p>
     *
     * @param clazz 需要删除的类
     * @param pkId 主键
     */
    public void deleteEntity(Class<T> clazz, String pkId) {
        T t;
        t = this.findEntity(clazz, pkId);
        t.setDelFlag(DEL_TRUE);
        this.baseDaoImpl.update(t);
    }

    /**
     *
     * <p>
     * Description: 从数据中物理的删除对象
     * </p>
     *
     * @param t 需要删除的对象
     */
    public void realDeleteEntity(T t) {
        this.baseDaoImpl.delete(t);
    }

    /**
     * <p>
     * Description: 从数据中物理的删除对象
     * </p>
     *
     * @param clazz 需要删除的类
     * @param pkId 主键
     */
    public void realDeleteEntity(Class<T> clazz, String pkId) {
        this.baseDaoImpl.deleteById(clazz, pkId);
    }

    /**
     *
     * <p>
     * Description: 删除对象列表
     * </p>
     *
     * @param entityList 需要删除的对象列表
     */
    public void realDeleteEntity(List<T> entityList) {
        this.baseDaoImpl.delete(entityList);
    }

    /**
     *
     * <p>
     * Description: 删除对象列表
     * </p>
     *
     * @param entityList 需要删除的对象列表
     */
    public void deleteEntity(List<T> entityList) {
        for (T t : entityList) {
            t.setDelFlag(DEL_TRUE);
        }
        this.baseDaoImpl.update(entityList);
    }

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
    public T findEntity(Class<T> clazz, String pkId) {
        T t;
        t = this.baseDaoImpl.getById(clazz, pkId);
        return t;
    }

    /**
     * <p>
     * Description: 根据查询条件判断是否存在，用于查重
     * </p>
     *
     * @param queryMap 查询条件
     * @param clazz 要查询的类
     * @return 是否存在
     */
    public boolean checkExits(Map<String, Query> queryMap, Class<T> clazz) {
        //创建查询对象
        DetachedCriteria criteria;
        criteria = DetachedCriteria.forClass(clazz);
        //封装过滤器、传入查询条件
        makeCriteria(queryMap, criteria, null);
        //过滤逻辑删除
        criteria.add(Property.forName("delFlag").eq(DEL_FALSE));
        //执行查询
        List<T> list;
        list = this.baseDaoImpl.findByCriteria(criteria);

        return !(list == null || list.size() == 0);
    }

    /**
     * <p>
     * Description: 是否有效查询
     * </p>
     *
     * @param fieldName 查询字段
     * @param queryMap 查询条件集合
     * @return 是否有效
     */
    public boolean checkValid(String fieldName, Map<String, Query> queryMap) {
        if (queryMap.get(fieldName) == null) {
            return false;
        } else if (queryMap.get(fieldName).getValue() == null) {
            return false;
        }
        return true;
    }

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
                                    List<Map> updateList, Class<T> clazz) {
        //把新增与更新的数据放在一个集合
        List<Map> checkList = null;
        if (insertList != null && updateList != null) {
            checkList = insertList;
            for (Map map : updateList) {
                checkList.add(map);
            }
        } else if (insertList != null) {
            checkList = insertList;
        } else if (updateList != null) {
            checkList = updateList;
        }
        //验证新增与更新的数据内部的唯一性
        for (int i = 0; i < checkList.size(); i++) {
            for (int j = 1 + i; j < checkList.size(); j++) {
                boolean b;
                b = true;
                for (String uniqueField : uniqueFields) {
                    b = b && checkList.get(i).get(uniqueField).equals(checkList.get(j).get(uniqueField));
                }
                if (b) {
                    return false;
                }
            }
        }
        //把所有数据都放在deleteList里
        if (checkList != null) {
            for (Map map : checkList) {
                deleteList.add(map);
            }
        }
        //验证新增与更新的数据的唯一性
        Map<String, Query> queryMap;
        queryMap = new HashMap<String, Query>();
        for (Map map : checkList) {
            for (String uniqueField : uniqueFields) {

                Field[] fields = clazz.getDeclaredFields();
                for(Field fld : fields){
                    System.out.println(fld.getType());
                    System.out.println(fld.getName());

                    if(fld.getName().equals(uniqueField)){
                        if(fld.getType().equals(Date.class)){
                            queryMap.put(uniqueField, new Query(Condition.EQ, DateUtil.getDateTime(map.get(uniqueField) + "")));
                        }else{
                            queryMap.put(uniqueField, new Query(Condition.EQ, map.get(uniqueField)));
                        }
                    }
                }

            }
            if (!this.checkExits(queryMap, deleteList, uniqueFields, clazz)) {
                return false;
            }
        }

        return true;
    }

    /**
     *
     * <p>
     * Description: 根据查询条件判断是否存在，用于查重
     * </p>
     *
     * @param queryMap 查询条件Map
     * @param allList 查询条件集合
     * @param uniqueFields 要验证的字段
     * @param clazz 要查询的类
     * @return 检查结果
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean checkExits(Map<String, Query> queryMap, List<Map> allList, String[] uniqueFields, Class clazz) {
        //拼查询条件
        StringBuilder hql;
        hql = new StringBuilder("from ").append(clazz.getName()).append(" clazz").append(" where clazz.delFlag = '0'");
        if (!allList.isEmpty()) {
            hql.append(" and clazz.id not in('");
            int i = 0;
            for (Map map : allList) {
                if (i < allList.size() - 1) {
                    hql.append(map.get("id")).append("','");
                } else {
                    hql.append(map.get("id")).append("')");
                }
                i++;
            }
        }
        for (String uniqueField : uniqueFields) {
            hql.append(" and clazz.").append(uniqueField).append(" =:").append(uniqueField);
        }
        String hqlStr;
        hqlStr = hql.toString();
        //查询
        List<T> list;
        list = this.baseDaoImpl.findByQuery(hqlStr, queryMap).list();
        //判断查询结果是否为空，并返回判断结果
        if (list.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     *
     * <p>
     * Description: 需要实现类自己注入需要的Dao
     * </p>
     *
     * @param baseDaoImpl 注入方法
     */
    public abstract void setBaseDao(BaseDaoImpl<T, String> baseDaoImpl);

}
