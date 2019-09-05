package com.cgiser.moka.model;

import java.io.Serializable;
import java.util.Date;

public class Salary implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6980561874703841313L;
	private int id;
	private int awardType;
	private int awardValue;
	private Date time;
	private int type;
	private Long roleId;
	private String salaryDesc;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAwardType() {
		return awardType;
	}
	public void setAwardType(int awardType) {
		this.awardType = awardType;
	}
	public int getAwardValue() {
		return awardValue;
	}
	public void setAwardValue(int awardValue) {
		this.awardValue = awardValue;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getSalaryDesc() {
		return salaryDesc;
	}
	public void setSalaryDesc(String salaryDesc) {
		this.salaryDesc = salaryDesc;
	}
	
}
