package com.swsm.main.service.impl;

import com.core.exception.BaseException;
import com.swsm.constant.ActionConstants;
import com.swsm.main.service.IMainService;
import com.swsm.system.dao.impl.ResourceDaoImpl;
import com.swsm.system.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <p>
 * ClassName: MainService
 * </p>
 * <p>
 * Description:框架服务接口实现类
 * </p>
 */
@Service("mainService")
public class MainServiceImpl implements IMainService {
    /**
     * 日志信息
     */
    private static Logger logger = LoggerFactory.getLogger(MainServiceImpl.class);
    /**
     * 资源DAO
     */
    @Autowired
    @Qualifier("resourceDao")
    private ResourceDaoImpl resourceDao;

    public Resource[] getResourcesByParentId(String parentId, String loginName) {
        if (loginName == null) {
            logger.debug("loginName is null");
            return new Resource[0];
        }
        if (!ActionConstants.ADMIN_USER.equals(loginName)) {
            List<String> params = new ArrayList<>();
            if (ActionConstants.ROOT_NODE_ID.equals(parentId)) {
                params.add("parentId");
            }
            List<Resource> list = this.resourceDao.getResourcesByParentId(loginName, parentId, params);
            Resource[] array  = list.toArray(new Resource[list.size()]);
            return array;
        } else {
            return this.getChildResource(parentId);
        }
    }

    @Override
    public Resource[] getChildResource(String parentId) {
        List<Resource> list;
        List<String> params = new ArrayList<>();
        if (ActionConstants.ROOT_NODE_ID.equals(parentId)) {
            params.add("parentId");
        }
        list = this.resourceDao.getChildResource(parentId, params);
        return list.toArray(new Resource[0]);
    }

    /**
     * 
     * <p>
     * Description: 获取直接孩子的数量
     * </p>
     * 
     * @param parentId 父资源ID
     * @return 孩子节点数量
     * @throws BaseException 异常
     */
    @SuppressWarnings("unused")
    private int getChildCount(String parentId) throws BaseException {
        Object o;
        o = this.resourceDao.getChildCount(parentId);
        return Integer.parseInt(o.toString());
    }

    /**
     * <p>
     * Description: 获取直接孩子的数量
     * </p>
     * 
     * @param loginName 用户登录信息
     * @return 资源列表
     * @throws BaseException 异常
     */
    @Override
    public Resource[] getHavResByLoginName(String loginName) throws BaseException {
        List<String> params = new ArrayList<>();
        if (ActionConstants.ADMIN_USER.equals(loginName)) {
            params.add("loginName");
            List<Resource> list;
            list = this.resourceDao.getHavResByLoginName(loginName, params);
            return list.toArray(new Resource[0]);
        }
        List<Resource> list;
        list = this.resourceDao.getHavResByLoginName(loginName, params);
        return list.toArray(new Resource[0]);
    }
}
