package com.swsm.system.enums.bizz;
/**
 * <p>ClassName: WorkMatProStatusEnum</p>
 * <p>Description: 物料加工制品状态枚举</p>
 */
public enum WorkMatProStatusEnum {

    ONGOING("进行中", "10"), FINISH("已完成", "20"), SELFCHECKED("已自检", "30"), QUALITIED("已品质确认", "40"), TRANSFERRED("已转出",
            "50"), ISUSED("已使用", "60");
    /**
     * 状态显示
     */
    private String display;

    /**
     * 状态值
     */
    private String value;

    private WorkMatProStatusEnum(String display, String value) {
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
