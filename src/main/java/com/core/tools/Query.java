/**
 * Query.java
 * Created at 2014-8-26
 * Created by Lee
 * Copyright (C) 2014 SHANGHAI BRODATEXT, All rights reserved.
 */
package com.core.tools;

/**
 * 
 * <p>ClassName: Query</p>
 * <p>Description: 查询条件</p>
 * <p>Author: Lee</p>
 * <p>Date: 2014-8-26</p>
 */
public class Query {
    /**
     * 条件
     */
    private Condition condition;
    /**
     * 值
     */
    private Object value;

    /**
     * 值
     */
    private boolean ignoreCase;

    /**
     * 
     * <p>Description: 构造函数，用于快速初始化</p>
     * @param field 字段
     * @param condition 条件
     * @param value 值
     */
    public Query(Condition condition, Object value) {
        this.condition = condition;
        this.value = value;
    }
    

    /**
     * 
     * <p>Description: 构造函数，用于快速初始化</p>
     * @param field 字段
     * @param condition 条件
     * @param value 值
     * @param ignoreCase 忽略大小写
     */
    public Query(Condition condition, Object value, boolean ignoreCase) {
        this.condition = condition;
        this.value = value;
        this.ignoreCase = ignoreCase;
    }
    
    public Condition getCondition() {
        return this.condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }


    public boolean isIgnoreCase() {
        return this.ignoreCase;
    }


    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }
}
