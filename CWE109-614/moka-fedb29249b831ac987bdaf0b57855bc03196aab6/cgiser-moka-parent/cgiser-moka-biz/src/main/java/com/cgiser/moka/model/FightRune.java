package com.cgiser.moka.model;

import java.io.Serializable;

public class FightRune implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -197533899567922727L;
	private int remainTimes;
	private Skill skill;
	private int level;
	private int runeId;
	private Long userRuneId;
	private String uUID;
	public int getRemainTimes() {
		return remainTimes;
	}
	public void setRemainTimes(int remainTimes) {
		this.remainTimes = remainTimes;
	}
	public Skill getSkill() {
		return skill;
	}
	public void setSkill(Skill skill) {
		this.skill = skill;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getRuneId() {
		return runeId;
	}
	public void setRuneId(int runeId) {
		this.runeId = runeId;
	}
	public Long getUserRuneId() {
		return userRuneId;
	}
	public void setUserRuneId(Long userRuneId) {
		this.userRuneId = userRuneId;
	}
	public String getuUID() {
		return uUID;
	}
	public void setuUID(String uUID) {
		this.uUID = uUID;
	}
}
