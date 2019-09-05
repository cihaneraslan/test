package com.cgiser.moka.result;

public class RobRole {
	private Long roleId;
	private String roleName;
	private int effective;
	private int avatar;
	private int robCoins;
	private int level;
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
	public int getEffective() {
		return effective;
	}
	public void setEffective(int effective) {
		this.effective = effective;
	}
	public int getAvatar() {
		return avatar;
	}
	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}
	public int getRobCoins() {
		return robCoins;
	}
	public void setRobCoins(int robCoins) {
		this.robCoins = robCoins;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}
