package com.cgiser.moka.data;

import java.util.Date;

public class UserStageDo {
	private int stageId;
	private int mapId;
	private Date lastFinishedTime;
	private int finishedStage;
	private Long counterAttackTime;
	private int type;
	private Long roleId;
	public int getStageId() {
		return stageId;
	}
	public void setStageId(int stageId) {
		this.stageId = stageId;
	}
	public int getMapId() {
		return mapId;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getFinishedStage() {
		return finishedStage;
	}
	public void setFinishedStage(int finishedStage) {
		this.finishedStage = finishedStage;
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
	public Date getLastFinishedTime() {
		return lastFinishedTime;
	}
	public void setLastFinishedTime(Date lastFinishedTime) {
		this.lastFinishedTime = lastFinishedTime;
	}
	public Long getCounterAttackTime() {
		return counterAttackTime;
	}
	public void setCounterAttackTime(Long counterAttackTime) {
		this.counterAttackTime = counterAttackTime;
	}
}
