package com.swsm.system.system.dao.impl;

import com.core.dao.impl.BaseDaoImpl;
import com.swsm.system.system.model.GridConfig;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <p>ClassName: GridConfigDao</p>
 * <p>Description: GridConfig数据操作层</p>
 */
@Repository("gridConfigDao")
public class GridConfigDaoImpl extends BaseDaoImpl<GridConfig, String> {

    /**
     * 
     * <p>Description: 根据页面以及列表删除下面所有配置信息</p>
     * @param pageCode 页面
     * @param gridCode 列表
     */
    public void deleteByPageAndGridCode(String pageCode, String gridCode) {
        this.executeHql("delete from GridConfig gc where gc.pageCode = '" + pageCode + "' and gc.gridCode = '"
                + gridCode + "'", null);
    }
    @SuppressWarnings("unchecked")
    public List<GridConfig> getGridConfig(String pageCode, String gridCode, String userName) {
        StringBuilder hql;
        hql = new StringBuilder("from GridConfig gc");
        hql.append(" where gc.pageCode = '").append(pageCode).append("'");
        hql.append(" and gc.gridCode = '").append(gridCode).append("'");
        hql.append(" and gc.userName = '").append(userName).append("'");
        return findEntityListByHql(hql.toString(), null);
    }
    
}
