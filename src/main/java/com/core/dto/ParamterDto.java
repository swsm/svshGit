package com.core.dto;

import com.core.tools.Condition;

import java.io.Serializable;


/**
 * <p>
 * ClassName: ParamDto
 * </p>
 * <p>
 * Description: 参数DTO
 * </p>
 */
public class ParamterDto implements Serializable {

    /**
     * <p>
     * Field serialVersionUID: TODO
     * </p>
     */
    private static final long serialVersionUID = -6589773107257721944L;

    /**
     * 参数编码
     */
    private String paramCode;

    /**
     * 参数值
     */
    private Object paramValue;

    /**
     * 参数运算符
     */
    private Condition paramOperate;

    /**
     * 
     * <p>
     * Description: 构造器
     * </p>
     * 
     * @param paramCode 参数编码
     * @param paramValue 参数值
     */
    public ParamterDto(String paramCode, Object paramValue) {
        this(paramCode, paramValue, Condition.EQ);
    }

    /**
     * 
     * <p>
     * Description: 构造器
     * </p>
     * 
     * @param paramCode 参数编码
     * @param paramValue 参数值
     * @param paramOperate 运算
     */
    public ParamterDto(String paramCode, Object paramValue, Condition paramOperate) {
        this.paramCode = paramCode;
        this.paramValue = paramValue;
        this.paramOperate = paramOperate;
    }

    public String getParamCode() {
        return this.paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public Object getParamValue() {
        return this.paramValue;
    }

    public void setParamValue(Object paramValue) {
        this.paramValue = paramValue;
    }

    public Condition getParamOperate() {
        return this.paramOperate;
    }

    public void setParamOperate(Condition paramOperate) {
        this.paramOperate = paramOperate;
    }

    @Override
    public String toString() {
        return "SingleParamDto [paramCode=" + this.paramCode + ", paramValue=" + this.paramValue + ", paramOperate="
                + this.paramOperate + "]";
    }

}
