/**
 * Filter.java
 * Created at 2014-8-26
 * Created by Lee
 * Copyright (C) 2014 SHANGHAI BRODATEXT, All rights reserved.
 */
package com.core.tools;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * <p>ClassName: Filter</p>
 * <p>Description: 查询过滤器（查询、分页、排序等）</p>
 * <p>Author: Lee</p>
 * <p>Date: 2014-8-26</p>
 */
public class Filter {
    /**
     * 分页工具类
     */
    private PageInfo pageInfo = new PageInfo();
    
    /**
     * 排序字段
     */
    private String[] orderStr;
    
    /**
     * 查询条件<字段名, 字段值>
     */
    private Map<String, com.core.tools.Query> queryMap = new HashMap<String, Query>();

    public String[] getOrderStr() {
        return orderStr;
    }

    public void setOrderStr(String[] orderStr) {
        this.orderStr = orderStr;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public Map<String, Query> getQueryMap() {
        return queryMap;
    }

    public void setQueryMap(Map<String, Query> queryMap) {
        this.queryMap = queryMap;
    }
}
