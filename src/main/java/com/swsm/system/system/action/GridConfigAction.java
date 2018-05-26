package com.swsm.system.system.action;

import com.alibaba.fastjson.JSONArray;
import com.core.action.BaseHandler;
import com.swsm.system.system.model.GridConfig;
import com.swsm.system.system.service.IGridConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>ClassName: GridConfigAction</p>
 * <p>Description: 列表配置的action交互层</p>
 */
@Controller
@RequestMapping("/main/gridConfig")
public class GridConfigAction extends BaseHandler {


    /**
     * gridConfigService注入
     */
    @Autowired
    @Qualifier("gridConfigService")
    private IGridConfigService gridConfigService;
    
    /**
     * 
     * <p>Description: 获取列表配置信息</p>
     * @param request 请求对象
     * @return 列表配置信息
     */
    @ResponseBody
    @RequestMapping(value = "/getGridConfig.mvc")
    public List<GridConfig> getGridConfig(HttpServletRequest request) {
        return this.gridConfigService.getGridConfig(request.getParameter("pageCode"), request.getParameter("gridCode"),
                this.getUserInfo(request)[1]);
    }
    
    /**
     * 
     * <p>Description: 保存列表配置信息</p>
     * @param request 请求对象
     */
    @ResponseBody
    @RequestMapping(value = "/saveGridConfig.mvc")
    public void saveGridConfig(HttpServletRequest request) {
        String cfgs;
        cfgs = request.getParameter("cfgs");
        List<GridConfig> cfgList;
        cfgList = JSONArray.parseArray(cfgs, GridConfig.class);
        this.gridConfigService.saveGridConfig(cfgList, this.getUserInfo(request)[1]);
    }
}
