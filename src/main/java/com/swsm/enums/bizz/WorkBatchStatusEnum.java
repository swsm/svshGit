package com.swsm.enums.bizz;
/**
 * <p>ClassName: WorkBatchStatusEnum</p>
 * <p>Description:生产批次状态枚举</p>
 */
public enum WorkBatchStatusEnum {

	NO_BEGIN("未开始", "1"), NO_RELEASE("未下发", "2"), ING("进行中", "3"), FIN("已完成", "4"), STOP(
			"暂停", "5");
	/**
	 * 状态显示
	 */
	private String display;

	/**
	 * 状态值
	 */
	private String value;

	private WorkBatchStatusEnum(String display, String value) {
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
