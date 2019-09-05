package com.cgiser.moka.model;

import java.io.Serializable;
import java.util.Date;

public class MatchRoleCache implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -176002063205576023L;
	private Long roleId;
	private Date matchLastTime;
	private int matchHasTimes;
	private Date matchLastAddCd;
	private Long matchCutDown;
	public Date getMatchLastTime() {
		return matchLastTime;
	}
	public void setMatchLastTime(Date matchLastTime) {
		this.matchLastTime = matchLastTime;
	}
	public int getMatchHasTimes() {
		return matchHasTimes;
	}
	public void setMatchHasTimes(int matchHasTimes) {
		this.matchHasTimes = matchHasTimes;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Date getMatchLastAddCd() {
		return matchLastAddCd;
	}
	public void setMatchLastAddCd(Date matchLastAddCd) {
		this.matchLastAddCd = matchLastAddCd;
	}
	public Long getMatchCutDown() {
		return matchCutDown;
	}
	public void setMatchCutDown(Long matchCutDown) {
		this.matchCutDown = matchCutDown;
	}

}
