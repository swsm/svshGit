/**
 * Condition.java
 * Created at 2014-8-26
 * Created by Lee
 * Copyright (C) 2014 SHANGHAI BRODATEXT, All rights reserved.
 */
package com.core.tools;

/**
 * 
 * <p>ClassName: Condition</p>
 * <p>Description: 用于做高级查询的条件</p>
 * <p>Author: Lee</p>
 * <p>Date: 2014-8-26</p>
 */
public enum Condition {
    /**
     * 不等于
     */
    NE,
    /**
     * 等于
     */
    EQ,     
    /**
     * 包含
     */
    LIKE,
    /**
     * 大于
     */
    GT,   
    /**
     * 大于等于
     */
    GE,     
    /**
     * 小于
     */
    LT,     
    /**
     * 小于等于
     */
    LE,     
    /**
     * 包含
     */
    IN,     
    /**
     * 为空
     */
    NULL,   
    /**
     * 不为空
     */
    NOTNULL, 
    /**
     * 关联表
     */
    JOIN_SELECT,
    /**
     * 左关联
     */
    LEFT_JOIN,
    /**
     * 内关联
     */
    INNER_JOIN,
    /**
     * 全关联
     */
    FULL_JOIN,
    /**
     * 并且
     */
    AND,
    /**
     * 或
     */
    OR,
    /**
     * 精准的
     */
    JUST
}
