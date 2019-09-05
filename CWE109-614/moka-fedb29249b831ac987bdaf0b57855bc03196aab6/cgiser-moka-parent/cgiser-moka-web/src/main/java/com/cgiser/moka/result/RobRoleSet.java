package com.cgiser.moka.result;

import java.util.Map;

public class RobRoleSet {
	private Long robLastTime;
	private int robHasTimes;
	private int robRefreshTimes;
	private int robRefreshCash;
	private String robRoleName;
	private int robStreakTimes;
	private int robTimesCash;
	private Map<String, Integer> stars;
	private int coins;
	private int effective;
	private int robRoleAvatar;
	private int robTimes;
	private int level;
	/**
	 * 上次入侵时间
	 * @return
	 */
	public Long getRobLastTime() {
		return robLastTime;
	}
	public void setRobLastTime(Long robLastTime) {
		this.robLastTime = robLastTime;
	}
	/**
	 * 今日入侵剩余次数
	 * @return
	 */
	public int getRobHasTimes() {
		return robHasTimes;
	}
	public void setRobHasTimes(int robHasTimes) {
		this.robHasTimes = robHasTimes;
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
	/**
	 * 入侵刷新消耗铜钱数
	 * @return
	 */
	public int getRobRefreshCash() {
		return robRefreshCash;
	}
	public void setRobRefreshCash(int robRefreshCash) {
		this.robRefreshCash = robRefreshCash;
	}
	/**
	 * 入侵玩家
	 * @return
	 */
	public String getRobRoleName() {
		return robRoleName;
	}
	public void setRobRoleName(String robRoleName) {
		this.robRoleName = robRoleName;
	}
	/**
	 * 入侵连胜次数
	 * @return
	 */
	public int getRobStreakTimes() {
		return robStreakTimes;
	}
	public void setRobStreakTimes(int robStreakTimes) {
		this.robStreakTimes = robStreakTimes;
	}
	/**
	 * 增加入侵次数消耗元宝数
	 * @return
	 */
	public int getRobTimesCash() {
		return robTimesCash;
	}
	public void setRobTimesCash(int robTimesCash) {
		this.robTimesCash = robTimesCash;
	}
	public Map<String, Integer> getStars() {
		return stars;
	}
	public void setStars(Map<String, Integer> stars) {
		this.stars = stars;
	}
	public int getCoins() {
		return coins;
	}
	public void setCoins(int coins) {
		this.coins = coins;
	}
	/**
	 * 战斗力
	 * @return
	 */
	public int getEffective() {
		return effective;
	}
	public void setEffective(int effective) {
		this.effective = effective;
	}
	public int getRobRoleAvatar() {
		return robRoleAvatar;
	}
	public void setRobRoleAvatar(int robRoleAvatar) {
		this.robRoleAvatar = robRoleAvatar;
	}
	public int getRobTimes() {
		return robTimes;
	}
	public void setRobTimes(int robTimes) {
		this.robTimes = robTimes;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}
