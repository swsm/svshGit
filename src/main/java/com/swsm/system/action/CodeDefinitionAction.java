package com.swsm.system.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.core.action.EntityHandler;
import com.core.service.IEntityService;
import com.core.tools.Condition;
import com.core.tools.Filter;
import com.core.tools.Query;
import com.swsm.system.model.CodeDefDetail;
import com.swsm.system.model.CodeDefinition;
import com.swsm.system.service.ICodeDefinitionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>ClassName: CodeDefinitionAction</p>
 * <p>Description: 编码定义action交互层</p>
 */
@Controller
@RequestMapping("main/codeDefinition")
public class CodeDefinitionAction extends EntityHandler<CodeDefinition> {
    
    /**
     * ICodeDefinitionService
     */
    @Autowired
    @Qualifier("codeDefinitionService")
    private ICodeDefinitionService codeDefinitionService;
    
    @Autowired
    @Qualifier("codeDefinitionService")
    @Override
    public void setEntityServiceImpl(IEntityService<CodeDefinition> entityServiceImpl) {
        this.entityServiceImpl = entityServiceImpl;
        this.clazz = CodeDefinition.class;
    }
    
    /**
     * 
     * <p>Description: 保存编码</p>
     * @param request 请求对象
     */
    @ResponseBody
    @RequestMapping("/saveCodeDefinition.mvc")
    public void saveCodeDefinition(HttpServletRequest request) {
        List<CodeDefinition> codeList;
        codeList = JSONArray.parseArray(request.getParameter("codeStr"), CodeDefinition.class);
        this.codeDefinitionService.saveCodeDefinition(codeList, this.getUserInfo(request));
    }
    
    /**
     * 
     * <p>Description: 删除编码</p>
     * @param ids 要删除的id集合
     */
    @ResponseBody
    @RequestMapping("/deleteCodeDefinition.mvc")
    public void deleteCodeDefinition(String ids) {
        this.codeDefinitionService.deleteCodeDefinition(ids.split(","));
    }
    
    /**
     * 
     * <p>Description: 根据条件查询编码信息</p>
     * @param request 请求对象
     * @return 查询结果
     * @throws Exception 异常
     */
    @ResponseBody
    @RequestMapping("/queryCodeDefinition.mvc")
    @SuppressWarnings({ "static-access", "unchecked" })
    public Map<String, Object> queryCodeDefinition(HttpServletRequest request) throws Exception {
        Map<String, String> paramMap;
        paramMap = (Map<String, String>) JSON.parse(request.getParameter("queryParams"));
        Map<String, Query> queryMap;
        queryMap = this.getQueryMap(paramMap);
        Filter filter;
        filter = this.doFilter(request, queryMap);
        List<CodeDefinition> list;
        list = this.codeDefinitionService.queryCodeDefinition(filter);
        Map<String, Object> rltMap;
        rltMap = new HashMap<>();
        rltMap.put("total", filter.getPageInfo().count);
        rltMap.put("rows", list);
        return rltMap;
    }

    /**
     * 
     * <p>Description: 拼装查询map</p>
     * @param paramMap 参数
     * @return 查询map，Map<String, Query>
     */
    private Map<String, Query> getQueryMap(Map<String, String> paramMap) {
        Map<String, Query> queryMap;
        queryMap = new HashMap<>();
        if (StringUtils.isNotEmpty(paramMap.get("codeName"))) {
            queryMap.put("codeName", new Query(Condition.LIKE, paramMap.get("codeName")));
        }
        if (StringUtils.isNotEmpty(paramMap.get("codeType"))) {
            queryMap.put("codeType", new Query(Condition.EQ, paramMap.get("codeType")));
        }
        return queryMap;
    }
    
    /**
     * 
     * <p>Description: 查询编码的明细信息</p>
     * @param codeId 编码id
     * @return 明细信息
     */
    @ResponseBody
    @RequestMapping("/getCodeDefDetail.mvc")
    public List<CodeDefDetail> getCodeDefDetail(String codeId) {
        return this.codeDefinitionService.getCodeDefDetail(codeId);
    }
    
    /**
     * 
     * <p>Description: 保存编码明细</p>
     * @param request 请求对象
     */
    @ResponseBody
    @RequestMapping("/saveCodeDefDetail.mvc")
    public void saveCodeDefDetail(HttpServletRequest request) {
        List<CodeDefDetail> detList;
        detList = JSONArray.parseArray(request.getParameter("detStr"), CodeDefDetail.class);
        this.codeDefinitionService.saveCodeDefDetail(detList, this.getUserInfo(request));
    }
    
    /**
     * <p>Description: 进行唯一性检验</p>
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/checkCodeUnique.mvc")
    @ResponseBody
    public void checkCodeUnique(HttpServletRequest request, HttpServletResponse response) throws Exception {
        queryCheckUnique(request, response);
    }

}
