package com.cgiser.moka.model;

import java.io.Serializable;
import java.util.Date;

public class RobRoleCache implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7432091891099114175L;
	private Date robLastTime;
	private int robHasTimes;
	private int robRefreshTimes;
	private int hasBeenRobCoins;
	private int robStreakTimes;
	private String robRoleName;
	private Date lastRefreshTime;
	public Date getRobLastTime() {
		return robLastTime;
	}
	public void setRobLastTime(Date robLastTime) {
		this.robLastTime = robLastTime;
	}
	public int getRobHasTimes() {
		return robHasTimes;
	}
	public void setRobHasTimes(int robHasTimes) {
		this.robHasTimes = robHasTimes;
	}
	public int getHasBeenRobCoins() {
		return hasBeenRobCoins;
	}
	public void setHasBeenRobCoins(int hasBeenRobCoins) {
		this.hasBeenRobCoins = hasBeenRobCoins;
	}
	/**
	 * 入侵玩家连胜次数
	 * @return
	 */
	public int getRobStreakTimes() {
		return robStreakTimes;
	}
	public void setRobStreakTimes(int robStreakTimes) {
		this.robStreakTimes = robStreakTimes;
	}
	/**
	 * 入侵随机玩家名称
	 * @return
	 */
	public String getRobRoleName() {
		return robRoleName;
	}
	public void setRobRoleName(String robRoleName) {
		this.robRoleName = robRoleName;
	}
	/**
	 * 入侵刷新剩余次数
	 * @return
	 */
	public int getRobRefreshTimes() {
		return robRefreshTimes;
	}
	public void setRobRefreshTimes(int robRefreshTimes) {
		this.robRefreshTimes = robRefreshTimes;
	}
	public Date getLastRefreshTime() {
		return lastRefreshTime;
	}
	public void setLastRefreshTime(Date lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

}
