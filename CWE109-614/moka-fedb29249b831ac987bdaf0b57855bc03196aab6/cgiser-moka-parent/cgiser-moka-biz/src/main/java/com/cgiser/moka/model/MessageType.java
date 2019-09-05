package com.cgiser.moka.model;

import org.apache.commons.lang.StringUtils;

public enum MessageType {
	SYSTEM(1005,""),
	LEGION(1003,""),
	GROUPFIGHT(1018,""),
	GETCARD(1, ""),
	GETRUNE(2,""),
	CARDSTRENG(3,""),
	RUNESRTRENG(4,""),
	MOSHSTART(5,""),
	MOSHAWARD(6,""),
	NEWEMAIL(7,""),
	NEWACHIEVEMENT(8,""),
	THIRD(9, ""),
	ACHIEVEMENT(10,""),
	MATCHSTART(11,""),
	DISSOLUTION(1,""),
	ROLELEAVE(2,""),
	ROLEIN(3,""),
	ROLECREATEROOM(4,""),
	STARTGROUPFIGHT(5,""),
	LEGIONSTART(12,"");
	

	private int code;

	private String description;

	private MessageType(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public static MessageType valueOf(int code) {
		switch (code) {
		case 1005:
			return GETCARD;
		default:
			return null;
		}
	}

	public static MessageType valueFromDesc(String desc) {
		if (StringUtils.equalsIgnoreCase("GETCARD", desc)) {
			return GETCARD;
		}
		return null;
	}
}
