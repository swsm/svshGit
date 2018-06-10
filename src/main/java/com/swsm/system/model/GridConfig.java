package com.swsm.system.model;

import com.core.entity.BaseModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <p>ClassName: GridConfig</p>
 * <p>Description: 类别配置的model</p>
 */
@Data
@Entity
@Table(name = "SYS_GRID_CONFIG")
public class GridConfig extends BaseModel implements java.io.Serializable {
    /**
     * 序列号
     */
    private static final long serialVersionUID = 7530202458836884341L;
    
    /**
     * 页面标识
     */
    @Column(name = "PAGE_CODE") 
    private String pageCode;
    
    /**
     * 列表标识
     */
    @Column(name = "GRID_CODE") 
    private String gridCode;
    
    /**
     * 列标识
     */
    @Column(name = "COL_CODE") 
    private String colCode;
    
    /**
     * 用户名
     */
    @Column(name = "USER_NAME") 
    private String userName;
    
    /**
     * 是否隐藏
     */
    @Column(name = "HIDDEN")
    private String hidden;
    
    /**
     * 列排序
     */
    @Column(name = "COL_INDEX") 
    private Double colIndex;
    
    /**
     * 该列排序状态
     */
    @Column(name = "SORT_STATE")
    private String sortState;

}
