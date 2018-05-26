package com.swsm.system.sequence.model;

import com.core.entity.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 序列表Model --Last updated:2006-03-24 11:17:29---
 * 
 * @author codegen
 */
@Entity
@Table(name = "SYS_SEQUENCE")
public class SequenceModel extends BaseModel {


    /**
     * <p>Field serialVersionUID: 版本序列号</p>
     */
    private static final long serialVersionUID = 1L;
    /**
     * 表名称
     */
    @Column(name = "TABLE_NAME")
    private String tableName = null;
    /**
     * 最大编号
     */
    @Column(name = "MAX_SEQ")
    private String maxSeq = null;

    /**
     * 日期
     */
    @Column(name = "SEQ_DATE")
    private String seqDate = null;

    /**
     * 日期
     */
    @Column(name = "SECOND_CODE")
    private String secondCode = null;

    /**
     * 设置日期
     * 
     * @param seqDate 日期
     */
    public void setSeqDate(String seqDate) {
        this.seqDate = seqDate;
    }

    /**
     * 获得日期
     * 
     * @return 日期
     */
    public String getSeqDate() {
        return this.seqDate;
    }

    /**
     * 设置最大编号
     * 
     * @param maxSeq 最大编号
     */
    public void setMaxSeq(String maxSeq) {
        this.maxSeq = maxSeq;
    }

    /**
     * 获得最大编号
     * 
     * @return 最大编号
     */
    public String getMaxSeq() {
        return this.maxSeq;
    }

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

    public String getSecondCode() {
        return this.secondCode;
    }

    public void setSecondCode(String secondCode) {
        this.secondCode = secondCode;
    }
}
