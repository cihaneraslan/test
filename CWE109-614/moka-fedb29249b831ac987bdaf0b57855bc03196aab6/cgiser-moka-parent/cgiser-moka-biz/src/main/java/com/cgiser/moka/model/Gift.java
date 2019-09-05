package com.cgiser.moka.model;

import java.util.Date;

public class Gift {
	private int giftId;
	private String giftName;
	private String giftValue;
	private Date start;
	private Date end;
	private int type;
	private int state;
	public int getGiftId() {
		return giftId;
	}
	public void setGiftId(int giftId) {
		this.giftId = giftId;
	}
	public String getGiftName() {
		return giftName;
	}
	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}
	public String getGiftValue() {
		return giftValue;
	}
	public void setGiftValue(String giftValue) {
		this.giftValue = giftValue;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
