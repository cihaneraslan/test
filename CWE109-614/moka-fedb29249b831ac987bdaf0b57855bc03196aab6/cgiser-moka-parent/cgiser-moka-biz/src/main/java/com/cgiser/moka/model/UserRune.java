package com.cgiser.moka.model;

public class UserRune {
	private Long userRuneId;
	private int runeId;
	private Long roleId;
	private int exp;
	private int level;
	private int state;
	private int groupId;
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public Long getUserRuneId() {
		return userRuneId;
	}
	public void setUserRuneId(Long userRuneId) {
		this.userRuneId = userRuneId;
	}
	public int getRuneId() {
		return runeId;
	}
	public void setRuneId(int runeId) {
		this.runeId = runeId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}
