package com.cgiser.moka.model;

import java.io.Serializable;
import java.util.Date;

public class RankRoleCache implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7619807483485343491L;
	private Long roleId;
	private Date rankFightTime;
	private int rankHasTimes;
	private int rankStreakTimes;
	private int rank;
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	/**
	 * 上次打排名战时间
	 * @return
	 */
	public Date getRankFightTime() {
		return rankFightTime;
	}
	public void setRankFightTime(Date rankFightTime) {
		this.rankFightTime = rankFightTime;
	}
	/**
	 * 排名战还有多少次
	 * @return
	 */
	public int getRankHasTimes() {
		return rankHasTimes;
	}
	public void setRankHasTimes(int rankHasTimes) {
		this.rankHasTimes = rankHasTimes;
	}
	/**
	 * 排名战连胜次数
	 * @return
	 */
	public int getRankStreakTimes() {
		return rankStreakTimes;
	}
	public void setRankStreakTimes(int rankStreakTimes) {
		this.rankStreakTimes = rankStreakTimes;
	}
	/**
	 * 当前排名
	 * @return
	 */
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}

}
