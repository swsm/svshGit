package com.swsm.main.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * <p>
 * ClassName: Product
 * </p>
 * <p>
 * Description: 产品信息定义类
 * </p>
 */
public class ProductModel implements java.io.Serializable {

    /**
     * <p>
     * Field serialVersionUID: TODO
     * </p>
     */
    private static final long serialVersionUID = 9088473143145349337L;
    /**
     * 打印单据厂商名称
     */
    private String printName;
    /**
     * 打印单据厂商logo的地址
     */
    private String printLogoUrl;

    /**
     * 提供的厂商名称
     */
    private String vendorName;
    /**
     * 提供的厂商logo的地址
     */
    private String logoUrl;
    /**
     * 系统名称
     */
    private String sysName;
    /**
     * 系统版本
     */
    private String sysVersion;
    /**
     * 系统制造商
     */
    private String sysMaking;
    
    /**
     * 是否存在erp系统
     */
    private String erpFlag;

    /**
     * 用户配置的var变量
     */
    private Map<String, String> varMap = new HashMap<String, String>();

    public Map<String, String> getVarMap() {
        return this.varMap;
    }

    public void setVarMap(Map<String, String> varMap) {
        this.varMap = varMap;
    }

    /**
     * 
     * <p>
     * Description: 增加变量
     * </p>
     * 
     * @param varName 变量名称
     * @param varValue 变量值
     */
    public void addVar(String varName, String varValue) {
        this.varMap.put(varName, varValue);
    }

    /**
     * 
     * <p>
     * Description: 获取配置的var变量值
     * </p>
     * 
     * @param varName 变量名称
     * @return 变量值
     */
    public String getVar(String varName) {
        return this.varMap.get(varName);
    }

    public String getSysName() {
        return this.sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysVersion() {
        return this.sysVersion;
    }

    public void setSysVersion(String sysVersion) {
        this.sysVersion = sysVersion;
    }

    public String getSysMaking() {
        return this.sysMaking;
    }

    public void setSysMaking(String sysMaking) {
        this.sysMaking = sysMaking;
    }

    public String getVendorName() {
        return this.vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getLogoUrl() {
        return this.logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getPrintName() {
        return this.printName;
    }

    public void setPrintName(String printName) {
        this.printName = printName;
    }

    public String getPrintLogoUrl() {
        return this.printLogoUrl;
    }

    public void setPrintLogoUrl(String printLogoUrl) {
        this.printLogoUrl = printLogoUrl;
    }

    public String getErpFlag() {
        return this.erpFlag;
    }

    public void setErpFlag(String erpFlag) {
        this.erpFlag = erpFlag;
    }
    
    
}
