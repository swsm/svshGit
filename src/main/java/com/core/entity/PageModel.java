/**
 * PageModel.java
 * Created at 2017-2-24
 * Created by Administrator
 * Copyright (C) 2017 BROADTEXT SOFTWARE, All rights reserved.
 */
package com.core.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 分页model
 * @param <T> 泛型
 */
public class PageModel<T> implements Serializable {

    /**
     * <p>Field serialVersionUID: 序列化</p>
     */
    private static final long serialVersionUID = -1780855870232340683L;
    
    /**
     * 数据信息集合
     */
    private List<T> datas;
    
    /**
     * 总记录数
     */
    private int totalRecords;


    public List<T> getDatas() {
        return this.datas;
    }


    public void setDatas(List<T> datas) {
        this.datas = datas;
    }


    public long getTotalRecords() {
        return this.totalRecords;
    }


    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

}
