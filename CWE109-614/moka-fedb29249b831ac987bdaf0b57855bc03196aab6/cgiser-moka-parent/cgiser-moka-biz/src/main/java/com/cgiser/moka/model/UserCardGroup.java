package com.cgiser.moka.model;

import java.util.List;

public class UserCardGroup {
	private Long groupId;
	private Long roleId;
	private String userCardIds;
	private List<UserCard> userCardInfo;
	private String userRuneIds;
	private List<UserRune> userRuneInfo;
	private String groupName;
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getUserCardIds() {
		return userCardIds;
	}
	public void setUserCardIds(String userCardIds) {
		this.userCardIds = userCardIds;
	}
	public List<UserCard> getUserCardInfo() {
		return userCardInfo;
	}
	public void setUserCardInfo(List<UserCard> userCardInfo) {
		this.userCardInfo = userCardInfo;
	}
	public String getUserRuneIds() {
		return userRuneIds;
	}
	public void setUserRuneIds(String userRuneIds) {
		this.userRuneIds = userRuneIds;
	}
	public List<UserRune> getUserRuneInfo() {
		return userRuneInfo;
	}
	public void setUserRuneInfo(List<UserRune> userRuneInfo) {
		this.userRuneInfo = userRuneInfo;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
