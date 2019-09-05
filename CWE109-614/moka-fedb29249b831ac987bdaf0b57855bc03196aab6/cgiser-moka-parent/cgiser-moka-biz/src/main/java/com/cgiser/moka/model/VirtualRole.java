package com.cgiser.moka.model;

public class VirtualRole {
	private Long roleId;
	private int level;
	private int avatar;
	private String cards;
	private String runes;
	private String souls;
	private int state;
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getAvatar() {
		return avatar;
	}
	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}
	public String getCards() {
		return cards;
	}
	public void setCards(String cards) {
		this.cards = cards;
	}
	public String getRunes() {
		return runes;
	}
	public void setRunes(String runes) {
		this.runes = runes;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getSouls() {
		return souls;
	}
	public void setSouls(String souls) {
		this.souls = souls;
	}
}
