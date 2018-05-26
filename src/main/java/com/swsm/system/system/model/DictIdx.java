package com.swsm.system.system.model;

import com.core.entity.BaseModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * <p>ClassName: DictIdx</p>
 * <p>Description: 字典索引的Model类</p>
 */
@Data
@Entity
@Table(name = "SYS_DICT_IDX")
public class DictIdx extends BaseModel implements java.io.Serializable {

    /**
     * 序列号
     */
    private static final long serialVersionUID = 7530202458836884341L;

    // Fields
    /**
     * 字典标识
     */
    @Column(name = "dict_key")
    private String dictKey;

    /**
     * 字典名称
     */
    @Column(name = "dict_name")
    private String dictName;

    /**
     * 字典类型
     */
    @Column(name = "dict_type")
    private int dictType;

    // Constructors

    /**
     * 默认的构造函数
     */
    public DictIdx() {
    }

}