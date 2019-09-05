package com.cgiser.moka.model;

import java.util.Date;

public class FreeFightModel {
	private String aRole;
	private String dRole;
	private String battleId;
	private Date date;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getaRole() {
		return aRole;
	}
	public void setaRole(String aRole) {
		this.aRole = aRole;
	}
	public String getdRole() {
		return dRole;
	}
	public void setdRole(String dRole) {
		this.dRole = dRole;
	}
	public String getBattleId() {
		return battleId;
	}
	public void setBattleId(String battleId) {
		this.battleId = battleId;
	}
	
}
