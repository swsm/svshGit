package com.swsm.system.system.service.impl;

import com.core.tools.MesBaseUtil;
import com.swsm.system.system.dao.impl.GridConfigDaoImpl;
import com.swsm.system.system.model.GridConfig;
import com.swsm.system.system.service.IGridConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>ClassName: GridConfigServiceImpl</p>
 * <p>Description: 列表配置的方法服务</p>
 */
@Service("gridConfigService")
public class GridConfigServiceImpl implements IGridConfigService {
    /**
     * gridConfigDao注入
     */
    @Autowired
    @Qualifier("gridConfigDao")
    private GridConfigDaoImpl gridConfigDao;
    
    @Override
    public List<GridConfig> getGridConfig(String pageCode, String gridCode, String userName) {
        List<GridConfig> list;
        list = this.gridConfigDao.getGridConfig(pageCode, gridCode, userName);
        return list;
    }

    @Override
    public void saveGridConfig(List<GridConfig> cfgList, String userName) {
        if (cfgList.isEmpty()) {
            return;
        }
        this.gridConfigDao.deleteByPageAndGridCode(cfgList.get(0).getPageCode(), cfgList.get(0).getGridCode());
        for (GridConfig cfg : cfgList) {
            cfg.setUserName(userName);
            MesBaseUtil.setDefaultFiledsBaseModel(cfg, new String[]{"", userName}, true);
        }
        this.gridConfigDao.save(cfgList);
    }

}
