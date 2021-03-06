package com.swsm.enums.bizz;

/**
 * <p>ClassName: OrderStatusEnum</p>
 * <p>Description: 订单状态枚举</p>
 */
public enum OrderStatusEnum {

    UNRELEASE("待下发", "1"), RELEASE("待加工", "2"), ONGOING("加工中", "3"), FINISH("已完成", "4"), STOP(
            "已冻结", "5");
    /**
     * 状态显示
     */
    private String display;

    /**
     * 状态值
     */
    private String value;

    private OrderStatusEnum(String display, String value) {
        this.display = display;
        this.value = value;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
