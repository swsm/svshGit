package com.swsm.system.model;

import com.core.entity.BaseModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;


/**
 * <p>ClassName: Dict</p>
 * <p>Description: 附件表的Model类</p>
 */
@Data
@Entity
@Table(name = "TT_ACCESSORY")
public class Accessory extends BaseModel implements java.io.Serializable {

    /**
     * 序列号
     */
    private static final long serialVersionUID = 7530202458836884341L;

    // Fields
    /**
     * 附件名称
     */
    @Column(name = "ACCESSORY_NAME")
    private String accessoryName;
    
    /**
     * 附件路径
     */
    @Column(name = "ACCESSORY_PATH")
    private String accessoryPath; 
    
    /**
     * 附件路径
     */
    @Column(name = "RELATIVE_CLASS_ID")
    private String relativeClassId; 
    
    
    /**
     * 附件上传时间
     */
    @Column(name = "UPLOAD_DATE")
    private Date uploadDate;

}