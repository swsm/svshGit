package com.core.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * <p>ClassName: BaseHibernateDao</p>
 * <p>Description: 数据访问支持类，用于提供实现功能的支持</p>
 */
@Repository
public class BaseHibernateDao extends HibernateDaoSupport {
    /**
     * <p>Description: 设置session工厂</p>
     *
     * @param sessionFactory session工厂对象
     */
    @Autowired
    public void setSuperSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
}
