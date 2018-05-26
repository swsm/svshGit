package com.swsm.system.system.model;

import com.core.entity.BaseModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "SYS_CODE_DEFINITION")
public class CodeDefinition extends BaseModel implements java.io.Serializable{

    /**
     * 序列号
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 编码名称
     */
    @Column(name = "CODE_NAME")
    private String codeName;
    
    /**
     * 编码类型
     */
    @Column(name = "CODE_TYPE")
    private String codeType;
    
    /**
     * 段数
     */
    @Column(name = "SEGMENT_NUM")
    private int segmentNum;
    
    /**
     * 间隔符
     */
    @Column(name = "SPACER")
    private String spacer;
     
    /**
     * 编码示例
     */
    @Column(name = "CODE_EXAMPLE")
    private String codeExample;

}
