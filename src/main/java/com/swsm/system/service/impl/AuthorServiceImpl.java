package com.swsm.system.service.impl;


import com.core.exception.BaseException;
import com.swsm.system.dao.impl.ResourceDaoImpl;
import com.swsm.system.service.IAuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


/**
 * 
 * <p>
 * ClassName:AuthorServiceImpl
 * </p>
 * <p>
 * Description:权限服务接口实现类
 * </p>
 */
@Service("authorService")
public class AuthorServiceImpl implements IAuthorService {
    /**
     * 日志信息
     */
    private static Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);
    /**
     * 资源DAO
     */
    @Autowired
    @Qualifier("resourceDao")
    private ResourceDaoImpl resourceDao;

    @Override
    public boolean hasAuthor(String userId, String resCode) throws BaseException {
        Object o;
        o = this.resourceDao.hasAuthor(userId, resCode);
        if (Integer.parseInt(o.toString()) > 0) {
            return true;
        }
        return false;
    }

}
