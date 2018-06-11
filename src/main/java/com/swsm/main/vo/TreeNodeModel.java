package com.swsm.main.vo;

import lombok.Data;

import java.util.Set;

/**
 * 
 * <p>
 * ClassName: TreeNodeModel
 * </p>
 * <p>
 * Description: extjs树节点模型类，描叙树上的一个节点
 * </p>
 */
@Data
public class TreeNodeModel implements java.io.Serializable {
    /**
     * 版本号
     */
    private static final long serialVersionUID = -6200763215958653L;
    /**
     * ID
     */
    private String id;
    /**
     * 节点显示值
     */
    private String text;
    /**
     * 叶子节点标志
     */
    private boolean leaf;
    /**
     * 资源编码唯一标识
     */
    private String resCode;
    /**
     * 孩子节点
     */
    private Set<TreeNodeModel> children;
    /**
     * 图标样式表
     */
    private String iconCls;
    /**
     * 孩子数量
     */
    private int childrenCount;
    /**
     * 节点顺序
     */
    private int nodeOrder;
    /**
     * 是否隐藏
     */
    private String isHidden;
    /**
     * 图标地址
     */
    private String icon;
    /**
     * 节点样式
     */
    private String cls;

    /**
     * 父节点
     */
    private TreeNodeModel parentNode;


}
