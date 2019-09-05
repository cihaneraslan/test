package com.cgiser.moka.model;

public class UserCard {
	private Long userCardId;
	private int cardId;
	private Long roleId;
	private int exp;
	private int level;
	private int groupId;
	private UserSoul userSoul;
	private int state;
	public Long getUserCardId() {
		return userCardId;
	}
	public void setUserCardId(Long userCardId) {
		this.userCardId = userCardId;
	}
	public int getCardId() {
		return cardId;
	}
	public void setCardId(int cardId) {
		this.cardId = cardId;
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
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public UserSoul getUserSoul() {
		return userSoul;
	}
	public void setUserSoul(UserSoul userSoul) {
		this.userSoul = userSoul;
	}
}
