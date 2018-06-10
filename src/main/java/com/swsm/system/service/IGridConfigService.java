package com.swsm.system.service;

import com.swsm.system.model.GridConfig;

import java.util.List;




/**
 * <p>ClassName: IGridConfigService</p>
 * <p>Description: 列表配置的相关接口定义</p>
 */
public interface IGridConfigService {

    /**
     * 
     * <p>Description: 根据页面、列表以及用户查询相应的</p>
     * @param pageCode 页面标识
     * @param gridCode 列表标识
     * @param userName 用户名
     * @return GridConfig列表配置信息
     */
    public List<GridConfig> getGridConfig(String pageCode, String gridCode, String userName);
    
    /**
     * 
     * <p>Description: 保存页面配置信息</p>
     * @param cfgList 页面配置
     * @param userName 用户名
     */
    public void saveGridConfig(List<GridConfig> cfgList, String userName);
}
