/**
 * Scalar.java
 * Created at 2015-6-4
 * Created by Administrator
 * Copyright (C) 2015 SHANGHAI BROADTEXT, All rights reserved.
 */
package com.core.tools;

import org.hibernate.type.AbstractStandardBasicType;

/**
 * <p>ClassName: Scalar</p>
 * <p>Description: 查询结果字段的映射定义，对于用sql进行列表查询时，往往需要将sql查询结果的字段类型进行映射</p>
 */
public class Scalar {
    
    /**
     * 字段名
     */
    private String fieldName;
    
    /**
     * 字段类型
     */
    private AbstractStandardBasicType type;
    
    /**
     * <p>Description: 默认构造函数</p>
     */
    public Scalar(){
        
    }
    
    /**
     * <p>Description: 构造函数</p>
     */
    public Scalar(String fieldName, AbstractStandardBasicType type){
        this.setFieldName(fieldName);
        this.setType(type);
    }
    
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public AbstractStandardBasicType getType() {
        return type;
    }
    public void setType(AbstractStandardBasicType type) {
        this.type = type;
    }
    
    
}
