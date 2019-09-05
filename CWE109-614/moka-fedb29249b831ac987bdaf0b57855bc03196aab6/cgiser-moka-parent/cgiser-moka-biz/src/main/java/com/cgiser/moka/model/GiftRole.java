package com.cgiser.moka.model;

import java.util.Date;

public class GiftRole {
	private int giftId;
	private Long roleId;
	private String code;
	private Date giftTime;
	private int state;
	public int getGiftId() {
		return giftId;
	}
	public void setGiftId(int giftId) {
		this.giftId = giftId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Date getGiftTime() {
		return giftTime;
	}
	public void setGiftTime(Date giftTime) {
		this.giftTime = giftTime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}
