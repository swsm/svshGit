package com.core.dao.impl;

import com.core.dao.IBaseDao;
import com.core.entity.BaseModel;
import com.core.exception.BaseDaoException;
import com.core.tools.Condition;
import com.core.tools.Filter;
import com.core.tools.PageInfo;
import com.core.tools.Query;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;

/**
 * <p>ClassName: BaseDaoImpl</p>
 * <p>Description: 数据访问的简单实现，这里封装了关于一个实体类基于Hibernate的CRUD操作</p>
 */
public class BaseDaoImpl<T extends BaseModel, PK extends Serializable> extends CommonDao implements IBaseDao<T, PK> {

    /**
     * 检索属性重合记录查询条数
     */
    private final Integer UNIQUE_SEARCH_NUM = 2;

    /**
     * 检索属性重合记录查询条数起始
     */
    private final Integer UNIQUE_SEARCH_START_NUM = 0;

    /**
     * 检索单条记录数量
     */
    private final Integer ONE_SEARCH_NUM = 1;


    /**
     * <p>Description: 指定的对象清除一级缓存p>
     *
     * @param t
     * @throws BaseDaoException
     */
    public void evict(T t) throws BaseDaoException {
        this.getHibernateTemplate().evict(t);
    }

    /**
     * <p>Description: 保存一个model到数据库，功能类似于insert into table</p>
     *
     * @param t model对象
     * @throws BaseDaoException 数据访问异常
     */
    public void save(T t) throws BaseDaoException {
        this.getHibernateTemplate().save(t);
    }

    /**
     * <p>Description: 保存一批model，功能类似于批量insert</p>
     *
     * @param modelList model对象集合
     * @throws BaseDaoException 数据访问异常
     */
    public void save(List<T> modelList) throws BaseDaoException {
        for (T t : modelList) {
            this.getHibernateTemplate().save(t);
        }
    }

    /**
     * <p>Description: 保存或更新一个model，功能类似于insert或update，根据数据库有无来自动判断</p>
     *
     * @param t model对象
     * @throws BaseDaoException 数据访问异常
     */
    public void saveOrUpdate(T t) throws BaseDaoException {
        this.getHibernateTemplate().saveOrUpdate(t);
    }

    /**
     * <p>Description: 保存或更新一批model，功能类似于批量insert或update，根据数据库有无来自动判断</p>
     *
     * @param modelList model对象集合
     * @throws BaseDaoException 数据访问异常
     */
    public void saveOrUpdate(List<T> modelList) throws BaseDaoException {
        for (T t : modelList) {
            this.getHibernateTemplate().saveOrUpdate(t);
        }
    }

    /**
     * <p>Description: 更新一个model，功能类似于update</p>
     *
     * @param t model对象
     * @throws BaseDaoException 数据访问异常
     */
    public void update(T t) throws BaseDaoException {
        this.getHibernateTemplate().update(t);
    }

    /**
     * <p>Description: 批量更新一批model，功能类似于批量update</p>
     *
     * @param modelList model对象集合
     * @throws BaseDaoException 数据访问异常
     */
    public void update(List<T> modelList) throws BaseDaoException {
        for (T t : modelList) {
            this.getHibernateTemplate().update(t);
        }
    }

    /**
     * <p>Description: 删除一个model，功能类似于delete</p>
     *
     * @param t model对象
     * @throws BaseDaoException 数据访问异常
     */
    public void delete(T t) throws BaseDaoException {
        this.getHibernateTemplate().delete(t);
    }

    /**
     * <p>Description: 删除一批model，功能类似于批量delete</p>
     *
     * @param modelList model对象集合
     * @throws BaseDaoException 数据访问异常
     */
    public void delete(List<T> modelList) throws BaseDaoException {
        for (T t : modelList) {
            this.getHibernateTemplate().delete(t);
        }
    }

    /**
     * <p>Description: 根据对象的id来删除对象</p>
     *
     * @param modelClass 对象类
     * @param id         对象id
     * @throws BaseDaoException 数据访问异常
     */
    public void deleteById(Class<T> modelClass, PK id) throws BaseDaoException {
        Object o = this.getById(modelClass, id);
        if (o != null) {
            this.getHibernateTemplate().delete(o);
        }
    }


    /**
     * <p>Description: 通过主键查询model对象</p>
     *
     * @param modelClass model类对象
     * @param id         主键
     * @return model对象
     * @throws BaseDaoException 数据访问异常
     */
    public T getById(Class<T> modelClass, PK id) throws BaseDaoException {

        return getHibernateTemplate().get(modelClass, id);
    }

    /**
     * <p>Description: 通过某字段查询model对象列表</p>
     *
     * @param modelClass   model类对象
     * @param propertyName 属性
     * @param value        值
     * @return model对象
     * @throws BaseDaoException 数据访问异常
     */
    @SuppressWarnings("unchecked")
    public List<T> findByProperty(Class<T> modelClass, String propertyName, Object value) {
        Assert.hasText(propertyName);
        return createCriteria(modelClass, Restrictions.eq(propertyName, value)).list();
    }

    /**
     * <p>Description: 通过其他唯一标识查询model对象</p>
     *
     * @param modelClass   model类对象
     * @param propertyName 唯一标识属性
     * @param value        值
     * @return model对象
     * @throws BaseDaoException 数据访问异常
     */
    @SuppressWarnings("unchecked")
    public T findUniqueByProperty(Class<T> modelClass, String propertyName, Object value) {
        Assert.hasText(propertyName);
        return (T) createCriteria(modelClass, Restrictions.eq(propertyName, value))
                .uniqueResult();
    }


    /**
     * <p>Description: 构造一个面向对象式的查询条件，可变参数</p>
     *
     * @param modelClass model类对象
     * @param criterions 可变参数
     * @return 查询条件
     */
    public Criteria createCriteria(Class<T> modelClass, Criterion... criterions) {
        Criteria criteria;
        criteria = this.getSessionFactory().getCurrentSession().createCriteria(modelClass);
        for (Criterion c : criterions) {
            criteria.add(c);
        }
        return criteria;
    }

    /**
     * <p>Description: 面向对象式不翻页的查询，返回满足条件的全部记录</p>
     *
     * @param detachedCriteria 查询对象
     * @return model对象列表
     * @throws BaseDaoException 数据访问异常
     */
    @SuppressWarnings("unchecked")
    public List findByCriteria(DetachedCriteria detachedCriteria) throws BaseDaoException {
        return getHibernateTemplate().findByCriteria(detachedCriteria);
    }


    /**
     * <p>Description: 根据构造的面向对象式的查询条件进行翻页查询</p>
     *
     * @param detachedCriteria 构造的查询条件组合
     * @param pageInfo         翻页信息
     * @return 查询结果
     * @throws BaseDaoException Dao异常
     */
    @SuppressWarnings("unchecked")
    public List<T> findByCriteria(DetachedCriteria detachedCriteria, PageInfo pageInfo) throws BaseDaoException {
        Criteria criteria;
        criteria = detachedCriteria.getExecutableCriteria(this.getSessionFactory().getCurrentSession());
        criteria.setCacheable(usingCache());
        long totalCount;
        Object totalCount2 = criteria.setProjection(Projections.rowCount()).uniqueResult();
        if (totalCount2 != null) {
            totalCount = ((Long) totalCount2).longValue();
        } else {
            totalCount = 0;
        }
        pageInfo.count = totalCount;
        criteria.setProjection(null);
        //设置查询结果为实体对象
        criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        List<T> resultList;
        resultList = criteria.setFirstResult(pageInfo.start).setMaxResults(pageInfo.limit).list();
        return resultList;
    }

    @Override
    public boolean isUniqueByPropertys(String currentId, Map<String, Object> uniquePropertys, boolean isAnd, Class<T> clazz)
            throws BaseDaoException {

        Assert.notNull(clazz, "clazz is null");
        Assert.notEmpty(uniquePropertys, "uniquePropertys is empty");

        //参数数组
        Map<String, com.core.tools.Query> queryMap;
        queryMap = new HashMap<String, com.core.tools.Query>();
        //拼查询条件
        StringBuilder hql;
        hql = new StringBuilder("from ").append(clazz.getSimpleName()).append(" clazz").append(" where clazz.delFlag = '0' and (");
        Set<String> keys = uniquePropertys.keySet();
        for (String key : keys) {
            hql.append(" clazz.").append(key).append("=:").append(key);
            //and与or关系
            if (isAnd) {
                hql.append(" AND");
            } else {
                hql.append(" OR");
            }
            //查询参数
            com.core.tools.Query query =
                    new com.core.tools.Query(Condition.EQ, uniquePropertys.get(key));
            queryMap.put(key, query);
        }
        String hqlStr;
        //去除循环结尾多余字符
        if (isAnd) {
            hqlStr = hql.substring(0, hql.length() - 3);
        } else {
            hqlStr = hql.substring(0, hql.length() - 2);
        }
        hqlStr += ")";

        //查询记录


        org.hibernate.Query queryRecords;
        queryRecords = findByQuery(hqlStr, queryMap);
        queryRecords.setCacheable(usingCache());
        queryRecords.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        //查询记录
        List<T> list;
        list = queryRecords.setFirstResult(UNIQUE_SEARCH_START_NUM).setMaxResults(UNIQUE_SEARCH_NUM).list();

        //未查询到记录，属性值唯一
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        //查询到一条记录
        if (list.size() == 1) {
            T t = list.get(0);
            if (t.getId().equals(currentId)) {//只有一条记录，且为传入的唯一id记录重合
                return true;
            } else {
                return false;
            }
        }
        //两条记录，不唯一
        return false;
    }

    @Override
    public boolean isUniqueByPropertys(Map<String, com.core.tools.Query> uniquePropertys, boolean isAnd, Class<T> clazz)
            throws BaseDaoException {

        Assert.notNull(clazz, "clazz is null");
        Assert.notEmpty(uniquePropertys, "uniquePropertys is empty");


        //拼查询条件
        StringBuilder hql;
        //获取表名称

        hql = new StringBuilder("from ").append(clazz.getSimpleName()).append(" clazz").append(" where ");
        Set<String> keys = uniquePropertys.keySet();
        for (String key : keys) {

            //查询条件
            Query query = uniquePropertys.get(key);
            String sqlPart = getSqlByCondition(key, query);
            hql.append(sqlPart);

            //and与or关系
            if (isAnd) {
                hql.append(" AND");
            } else {
                hql.append(" OR");
            }
        }
        String hqlStr;
        //去除循环结尾多余字符
        if (isAnd) {
            hqlStr = hql.substring(0, hql.length() - 3);
        } else {
            hqlStr = hql.substring(0, hql.length() - 2);
        }

        org.hibernate.Query queryRecords;
        queryRecords = findByQuery(hqlStr, uniquePropertys);
        queryRecords.setCacheable(usingCache());
        queryRecords.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        //查询记录
        List list;

        list = queryRecords.setFirstResult(UNIQUE_SEARCH_START_NUM).setMaxResults(ONE_SEARCH_NUM).list();

        //未查询到记录，属性值唯一
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        return false;

    }

    /**
     * <p>Description: 根据查询条件拼接hql</p>
     *
     * @param key 查询字段
     * @param query 查询值
     * @return 查询sql
     */
    private String getSqlByCondition(String key, Query query) {
        //查询sql片段
        StringBuilder sql = new StringBuilder();
        sql.append(" clazz.").append(key);
        switch (query.getCondition()) {
            case IN:
                sql.append(" IN (:").append(key).append(")");
                break;
            case LIKE:
            case JUST:
            case EQ:
            case GE:
            case LE:
            case GT:
            case LT:
                sql.append("=:").append(key);
                break;
        }


        return sql.toString();
    }


    @Override
    public T getOneByPropertys(Class<T> modelClass, Map<String, Object> params, Filter filter) {
        if (null == filter) {
            filter = new Filter();
        }
        //设定只查一个
        filter.getPageInfo().start = UNIQUE_SEARCH_START_NUM;
        filter.getPageInfo().limit = ONE_SEARCH_NUM;
        List<T> result =
                getByPropertys(modelClass, params, filter);
        if (null != result && result.size() > 0) {
            return result.get(0);
        }
        return null;

    }

    @Override
    public List<T> getByPropertys(Class<T> modelClass, Map<String, Object> params, Filter filter) {

        Assert.notNull(modelClass, "clazz is null");
        //过滤器为null,则创建默认
        if (null == filter) {
            filter = new Filter();
        }

        //参数数组
        Map<String, com.core.tools.Query> queryMap;
        queryMap = new HashMap<String, com.core.tools.Query>();
        //拼查询条件
        StringBuilder hql;
        hql = new StringBuilder("from ").append(modelClass.getSimpleName())
                .append(" clazz").append(" where clazz.delFlag = '0' ");


        if (null != params && !params.isEmpty()) {
            //参数条件
            StringBuilder sb = new StringBuilder();
            sb.append(" and");
            Set<String> keys = params.keySet();
            for (String key : keys) {
                //查询值
                Object value = params.get(key);
                //list值则使用in
                //查询参数
                com.core.tools.Query query;

                if (value instanceof Collection) {
                    query = new com.core.tools.Query(Condition.IN, value);
                } else {
                    query = new com.core.tools.Query(Condition.EQ, value);
                }

                sb.append(getSqlByCondition(key, query));

                sb.append(" AND");

                queryMap.put(key, query);
            }

            String hqlStr = "";
            //去除循环结尾多余字符
            hqlStr = sb.substring(0, sb.length() - 3);

            hql.append(hqlStr);

        }

        if (null != filter.getOrderStr() && filter.getOrderStr().length > 0) {
            StringBuilder ordersb = new StringBuilder();
            ordersb.append(" order by ");
            for (String order : filter.getOrderStr()) {
                ordersb.append(order).append(",");
            }

            String orderStr = "";
            orderStr = ordersb.substring(0, ordersb.length() - 1);
            hql.append(orderStr);
        }


        //查询记录
        org.hibernate.Query queryRecords;
        queryRecords = findByQuery(hql.toString(), queryMap);
        queryRecords.setCacheable(usingCache());
        queryRecords.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);

        //查询记录
        List<T> list;
        list = queryRecords.setFirstResult(filter.getPageInfo().start)
                .setMaxResults(filter.getPageInfo().limit).list();
        return list;
    }


    @Override
    public T getOneByPropertys(Class<T> modelClass, Map<String, Object> params) {
        Filter filter = new Filter();
        filter.getPageInfo().start = UNIQUE_SEARCH_START_NUM;
        filter.getPageInfo().limit = ONE_SEARCH_NUM;
        return getOneByPropertys(modelClass, params, filter);
    }

    /**
     * <p>
     * Description: 是否有效查询
     * </p>
     *
     * @param fieldName 查询字段
     * @param queryMap  查询条件集合
     * @return 是否有效
     */
    protected boolean checkValid(String fieldName, Map<String, Query> queryMap) {
        if (queryMap.get(fieldName) == null) {
            return false;
        } else if (queryMap.get(fieldName).getValue() == null) {
            return false;
        }
        return true;
    }

}
