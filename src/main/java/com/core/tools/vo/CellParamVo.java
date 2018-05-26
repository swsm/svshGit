/**
 * CellParam.java
 * Created at 2017-5-6
 * Created by Administrator
 * Copyright (C) 2017 BROADTEXT SOFTWARE, All rights reserved.
 */
package com.core.tools.vo;

import java.io.Serializable;

/**
 * <p>ClassName: CellParam</p>
 * <p>Description: 电芯参数Vo</p>
 * <p>Author: Administrator</p>
 * <p>Date: 2017-5-6</p>
 */
public class CellParamVo implements Serializable {

    /**
     * <p>Field serialVersionUID: 版本号</p>
     */
    private static final long serialVersionUID = 1L;
    /**
     * 电芯字段名称
     */
    private String cellColumnName;
    /**
     * 对应工序的URL
     */
    private String opUrl;
    /**
     * 结果参数名称
     */
    private String resultTarget;
    /**
     * 字段显示名称
     */
    private String text;
    /**
     * 电芯追溯时是否显示
     */
    private boolean isDisplay;
    
    public String getCellColumnName() {
        return cellColumnName;
    }

    public void setCellColumnName(String cellColumnName) {
        this.cellColumnName = cellColumnName;
    }

    public String getOpUrl() {
        return opUrl;
    }

    public void setOpUrl(String opUrl) {
        this.opUrl = opUrl;
    }

    public String getResultTarget() {
        return resultTarget;
    }

    public void setResultTarget(String resultTarget) {
        this.resultTarget = resultTarget;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isDisplay() {
        return isDisplay;
    }

    public void setDisplay(boolean isDisplay) {
        this.isDisplay = isDisplay;
    }

}
