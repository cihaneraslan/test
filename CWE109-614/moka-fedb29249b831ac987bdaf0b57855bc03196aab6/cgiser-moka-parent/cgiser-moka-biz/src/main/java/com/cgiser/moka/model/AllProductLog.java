package com.cgiser.moka.model;

import java.util.Date;

public class AllProductLog {
	private Long storeId;
	private Long roleId;
	private String productId;
	private String cooOrderSerial;//COOORDERSERIAL
	private Date storeTime;
	private int type;
	private int state;
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getCooOrderSerial() {
		return cooOrderSerial;
	}
	public void setCooOrderSerial(String cooOrderSerial) {
		this.cooOrderSerial = cooOrderSerial;
	}
	public Date getStoreTime() {
		return storeTime;
	}
	public void setStoreTime(Date storeTime) {
		this.storeTime = storeTime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}
