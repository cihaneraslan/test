package com.cgiser.moka.model;

import org.apache.commons.lang.StringUtils;

public enum LegionEventEnum {
	CREATELEGION(1,"创建帮派"),
	APPLYLEGION(2,"申请加入帮派"),
	APPLYTOLEGION(3,"批准加入帮派"),
	DELLEGION(4,"拒绝加入帮派"),
	UPDATESLOGAN(5,"修改帮派口号"),
	UPDATENOTICE(6,"修改帮派公告"),
	OUTLEGION(7,"退出帮派"),
	GENGHUANLEGION(9,"更换帮主"),
	BREAKEUPLEGION(10,"解散帮派"),
	GUARDLEGION(11,"设置守卫"),
	BUYLEGIONTIMES(12,"购买帮派战次数"),
	ALLOTRESOURCE(13,"分配资产"),
	STARTFIGHTATTACK(14,"发起帮派战(功方)"),
	STARTFIGHTDEFEND(15,"发起帮派战(守方)"),
	GETOUTLEGION(8,"踢出帮派");
	private int code;

	private String description;

	private LegionEventEnum(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public static LegionEventEnum valueOf(int code) {
		switch (code) {
		case 1:
			return CREATELEGION;
		default:
			return null;
		}
	}

	public static LegionEventEnum valueFromDesc(String desc) {
		if (StringUtils.equalsIgnoreCase("CREATELEGION", desc)) {
			return CREATELEGION;
		}
		return null;
	}
}
