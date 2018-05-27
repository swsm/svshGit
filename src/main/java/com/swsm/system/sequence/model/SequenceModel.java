package com.swsm.system.sequence.model;

import com.core.entity.BaseModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 序列表Model --Last updated:2006-03-24 11:17:29---
 * 
 * @author codegen
 */
@Data
@Entity
@Table(name = "SYS_SEQUENCE")
public class SequenceModel extends BaseModel implements java.io.Serializable {


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
    }
