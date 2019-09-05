package com.cgiser.moka.model;

import java.io.Serializable;

public class AwardItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3126562600782726679L;
	private int type;
	private int value;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
}
