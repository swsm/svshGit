package com.core.service.impl;

import com.core.dao.impl.CommonDao;
import com.core.tools.Filter;
import com.core.tools.Query;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 * <p>ClassName: BaseServiceImpl</p>
 * <p>Description: 通用服务层实现</p>
 */
public abstract class BaseServiceImpl {
    /**
     * 注入数据库访问对象
     */
    protected CommonDao commonDao;

    /**
     * <p>Description: 取得Hibernate的数据库操作对象</p>
     * @return Hibernate的数据库操作对象
     */
    protected Session getSession(){
        return commonDao.getSessionFactory().getCurrentSession();
    }

    /**
     *
     * <p>Description: 封装查询条件</p>
     * @param filter 过滤器
     * @param criteria 查询条件
     */
    protected void doFilter(Filter filter , DetachedCriteria criteria) {
        //封装排序
        String[] orderString;
        orderString = filter.getOrderStr();
        if (orderString != null && orderString.length == 2) {
            String realFieldName;
            String fieldName;
            realFieldName = orderString[0];
            fieldName = getOrderMap().get(orderString[0]);
            if (StringUtils.isNotEmpty(fieldName)) {
                realFieldName = fieldName;
            }

            if ("ASC".equals(orderString[1])) {
                criteria.addOrder(Order.asc(realFieldName));
            } else {
                criteria.addOrder(Order.desc(realFieldName));
            }
        }
        makeCriteria(filter.getQueryMap(), criteria, null);
    }

    /**
     * <p>Description: 取得特殊的排序字段集合，一般的话不放在这里</p>
     * @return 特殊的排序字段集合
     */
    protected Map<String, String> getOrderMap() {
        Map<String, String> orderMap;
        orderMap = new HashMap<String, String>();
        return orderMap;
    }

    /**
     * <p>Description: 组装查询条件</p>
     * @param queryMap 查询条件集合
     * @param criteria 查询条件
     * @param junction 连接方式
     */
    protected void makeCriteria(Map<String, Query> queryMap, DetachedCriteria criteria, Junction junction) {
        //封装查询条件
        for (String fieldName : queryMap.keySet()) {
            Query query = queryMap.get(fieldName);
            if(query != null){
                SimpleExpression se = null;
                Criterion ct = null;
                switch (query.getCondition()) {
                    case LIKE:
                        if (StringUtils.isNotEmpty((String)query.getValue())) {
                            //                        se = Property.forName(query.getField()).like("%" + query.getValue() + "%");
                            //默认是不区分大小写进行模糊查询的
                            criteria.add(Restrictions.like(fieldName,
                                    (String)query.getValue(), MatchMode.ANYWHERE).ignoreCase());
                        }
                        break;
                    case EQ:
                        if (query.getValue() != null) {
                            se = Property.forName(fieldName).eq(query.getValue());
                        }
                        break;
                    case NE:
                        if (query.getValue() != null) {
                            se = Property.forName(fieldName).ne(query.getValue());
                        }
                        break;
                    case GT:
                        se = Property.forName(fieldName).gt(query.getValue());
                        break;
                    case GE:
                        se = Property.forName(fieldName).ge(query.getValue());
                        break;
                    case LT:
                        se = Property.forName(fieldName).lt(query.getValue());
                        break;
                    case LE:
                        se = Property.forName(fieldName).le(query.getValue());
                        break;
                    case NULL:
                        ct = Property.forName(fieldName).isNull();
                        break;
                    case IN:
                        ct = Property.forName(fieldName).in((Collection) query.getValue());
                        break;
                    case NOTNULL:
                        ct = Property.forName(fieldName).isNotNull();
                        break;
                    case JOIN_SELECT:
                        criteria.createAlias(fieldName, fieldName);
                        break;
                    case LEFT_JOIN:
                        criteria.createAlias(fieldName, fieldName, CriteriaSpecification.LEFT_JOIN);
                        break;
                    case FULL_JOIN:
                        criteria.createAlias(fieldName, fieldName, CriteriaSpecification.FULL_JOIN);
                        break;
                    case INNER_JOIN:
                        criteria.createAlias(fieldName, fieldName, CriteriaSpecification.INNER_JOIN);
                        break;
                    case AND:
                        Conjunction con;
                        con = Restrictions.conjunction();
                        makeCriteria((Map<String, Query>)query.getValue(), criteria, con);
                        criteria.add(con);
                        break;
                    case OR:
                        Disjunction dis;
                        dis = Restrictions.disjunction();
                        makeCriteria((Map<String, Query>)query.getValue(), criteria, dis);
                        criteria.add(dis);
                        break;
                    default:
                        break;
                }
                if (se != null) {
                    if (junction != null) {
                        junction.add(se);
                    } else {
                        criteria.add(se);
                    }
                }
                if (ct != null) {
                    if (junction != null) {
                        junction.add(ct);
                    } else {
                        criteria.add(ct);
                    }
                }
            }
        }
    }
}
