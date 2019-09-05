package com.cgiser.moka.result;

import java.util.List;

import com.cgiser.moka.model.UserCardGroup;

public class UserCardGroupResult {
	private List<UserCardGroup> userCardGroups;
	private int effective;
	public List<UserCardGroup> getUserCardGroups() {
		return userCardGroups;
	}
	public void setUserCardGroups(List<UserCardGroup> userCardGroups) {
		this.userCardGroups = userCardGroups;
	}
	public int getEffective() {
		return effective;
	}
	public void setEffective(int effective) {
		this.effective = effective;
	}
}
