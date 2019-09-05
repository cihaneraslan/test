package com.cgiser.moka.model;

import java.util.Date;

public class RobLog {
	private Long robId;
	private Long roleId;
	private Long robRoleId;
	private int robRoleCoins;
	private Date robTime;
	private int state;
	public Long getRobId() {
		return robId;
	}
	public void setRobId(Long robId) {
		this.robId = robId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getRobRoleId() {
		return robRoleId;
	}
	public void setRobRoleId(Long robRoleId) {
		this.robRoleId = robRoleId;
	}
	public int getRobRoleCoins() {
		return robRoleCoins;
	}
	public void setRobRoleCoins(int robRoleCoins) {
		this.robRoleCoins = robRoleCoins;
	}
	public Date getRobTime() {
		return robTime;
	}
	public void setRobTime(Date robTime) {
		this.robTime = robTime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
}
