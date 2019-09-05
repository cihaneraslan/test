package com.cgiser.moka.model;

import java.util.Map;

public class LegionGuard {
	private Long legionerId;
	private Long roleId;
	private String roleName;
	private int avatar;
	private int effective;
	private Map<String, Integer> stars;
	private String coins;
	private int sex;
	public Long getLegionerId() {
		return legionerId;
	}
	public void setLegionerId(Long legionerId) {
		this.legionerId = legionerId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public int getAvatar() {
		return avatar;
	}
	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}
	public int getEffective() {
		return effective;
	}
	public void setEffective(int effective) {
		this.effective = effective;
	}
	public Map<String, Integer> getStars() {
		return stars;
	}
	public void setStars(Map<String, Integer> stars) {
		this.stars = stars;
	}
	public String getCoins() {
		return coins;
	}
	public void setCoins(String coins) {
		this.coins = coins;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
}
