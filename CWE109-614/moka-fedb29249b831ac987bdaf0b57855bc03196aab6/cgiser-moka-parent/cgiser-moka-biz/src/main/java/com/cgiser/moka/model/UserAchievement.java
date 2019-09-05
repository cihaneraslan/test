package com.cgiser.moka.model;

import java.util.Date;

public class UserAchievement {
	private Long userAchievementId;
	private Long roleId;
	private Date CreateTime;
	private int achievementId;
	private int finishState;
	public Long getUserAchievementId() {
		return userAchievementId;
	}
	public void setUserAchievementId(Long userAchievementId) {
		this.userAchievementId = userAchievementId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Date getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}
	public int getAchievementId() {
		return achievementId;
	}
	public void setAchievementId(int achievementId) {
		this.achievementId = achievementId;
	}
	public int getFinishState() {
		return finishState;
	}
	public void setFinishState(int finishState) {
		this.finishState = finishState;
	}
	
}
