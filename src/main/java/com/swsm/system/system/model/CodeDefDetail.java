package com.swsm.system.system.model;

import com.core.entity.BaseModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "SYS_CODE_DEF_DETAIL")
public class CodeDefDetail extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * 编码定义id
     */
    @Column(name = "CODE_DEF_ID")
    private String codeDefId;
    
    /**
     * 编码段顺序
     */
    @Column(name = "SERIAL_NUM")
    private int serialNum;
    
    /**
     * 编码段类型
     */
    @Column(name = "SEGMENT_TYPE")
    private String segmentType;
    
    /**
     * 长度（位数）
     */
    @Column(name = "SEGMENT_LENGTH")
    private int segmentLength;
    
    /**
     * 补位（填充）方式
     */
    @Column(name = "FILLING_TYPE")
    private String fillingType;
    
    /**
     * 编码段内容
     */
    @Column(name = "SEGMENT_CONTENT")
    private String segmentContent;
    
    /**
     * 编码段类别
     */
    @Column(name = "SEGMENT_CATEGORY")
    private String segmentCategory;
    
    /**
     * 日期格式化
     */
    @Column(name = "DATE_FORMAT")
    private String dateFormat;
    
    /**
     * 编码段含义
     */
    @Column(name = "SEGMENT_DESC")
    private String segmentDesc;

}
