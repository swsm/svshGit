package com.swsm.system.sequence.model;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 序号生成机制信息
 * 
 * @time: 14:57:44
 * @author lift
 */
public class SeqGenInfo implements java.io.Serializable {
    /**
     * 日志信息
     */
    private static Logger logger = LoggerFactory.getLogger(SeqGenInfo.class);
    /**
     * <p>
     * Field serialVersionUID: 版本序列号
     * </p>
     */
    private static final long serialVersionUID = 1L;
    /**
     * 表名称
     */
    private String tableName = null;
    /**
     * 字段名
     */
    private String fieldName = null;
    /**
     * 前缀
     */
    private String prefix = null;
    /**
     * 是否存在二级代码
     */
    private boolean hasSecondCode = true;
    /**
     * 循环周期
     */
    private String loop = "yyyyMMdd";
    /**
     * seq的序号长度
     */
    private int seqLength = 3;
    /**
     * 分割字符
     */
    private String seperator = "-";
    /**
     * 日期掩码
     */
    private String dateMask = "yyyyMMdd";

    /**
     * 设置表名称
     * 
     * @param tableName 表名称
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 获得表名称
     * 
     * @return 表名称
     */
    public String getTableName() {
        return this.tableName;
    }
    
    /**
     * 设置字段名
     * 
     * @param fieldName
     *            字段名
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * 获得字段名
     * 
     * @return 字段名
     */
    public String getFieldName() {
        return this.fieldName;
    }

    /**
     * 设置前缀
     * 
     * @param prefix 前缀
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 获得前缀
     * 
     * @return 前缀
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * 设置是否存在二级代码
     * 
     * @param hasSecondCode 是否存在二级代码
     */
    public void setHasSecondCode(boolean hasSecondCode) {
        this.hasSecondCode = hasSecondCode;
    }

    /**
     * 获得是否存在二级代码
     * 
     * @return 是否存在二级代码
     */
    public boolean getHasSecondCode() {
        return this.hasSecondCode;
    }

    /**
     * 设置循环周期
     * 
     * @param loop 循环周期
     */
    public void setLoop(String loop) {
        this.loop = loop;
    }

    /**
     * 获得循环周期
     * 
     * @return 循环周期
     */
    public String getLoop() {
        return this.loop;
    }

    /**
     * 设置分割字符
     * 
     * @param seperator 分割字符
     */
    public void setSeperator(String seperator) {
        this.seperator = seperator;
    }

    /**
     * 获得分割字符
     * 
     * @return 分割字符
     */
    public String getSeperator() {
        return this.seperator;
    }

    /**
     * 设置日期掩码
     * 
     * @param dateMask 日期掩码
     */
    public void setDateMask(String dateMask) {
        this.dateMask = dateMask;
    }

    /**
     * 获得日期掩码
     * 
     * @return 日期掩码
     */
    public String getDateMask() {
        return this.dateMask;
    }

    /**
     * 设置seq的序号长度
     * 
     * @param seqLength seq的序号长度
     */
    public void setSeqLength(int seqLength) {
        this.seqLength = seqLength;
    }

    /**
     * 获得seq的序号长度
     * 
     * @return seq的序号长度
     */
    public int getSeqLength() {
        return this.seqLength;
    }

    /**
     * 
     * <p>
     * Description: 判断该对象是否有效；主要验证日期掩码，循环周期以及表名
     * 
     * @return true表示有效，false表示无效
     *         </p>
     */
    public boolean isValid() {
        boolean valid = true;
        if (StringUtils.isEmpty(this.tableName)) {
            logger.warn("tableName is empty,please check tableName!");
            valid = false;
        }
        if (StringUtils.isEmpty(this.fieldName)) {
            logger.warn("fieldName is empty,please check fieldName!");
            valid = false;
        }
        /**
         * 会出现日期为null的情况
        if (StringUtils.isEmpty(this.dateMask)) {
            logger.warn("dateMask is empty,please check dateMask!");
            valid = false;
        }
        if (StringUtils.isEmpty(this.loop)) {
            logger.warn("loop is empty,please check loop!");
            valid = false;
        }
        */
        return valid;
    }

}
