package com.cgiser.moka.result;

import java.util.List;

import com.cgiser.moka.model.Role;

public class RankCompetitorsResult {
	private int cash;
	private List<Role> competitors;
	private Long countdown;
	private int rankHasTimes;
	private int rankSalary;
	private Long rankSalaryTimes;
	public int getCash() {
		return cash;
	}
	public void setCash(int cash) {
		this.cash = cash;
	}
	public List<Role> getCompetitors() {
		return competitors;
	}
	public void setCompetitors(List<Role> competitors) {
		this.competitors = competitors;
	}
	public Long getCountdown() {
		return countdown;
	}
	public void setCountdown(Long countdown) {
		this.countdown = countdown;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	private int rank;
	public int getRankHasTimes() {
		return rankHasTimes;
	}
	public void setRankHasTimes(int rankHasTimes) {
		this.rankHasTimes = rankHasTimes;
	}
	public int getRankSalary() {
		return rankSalary;
	}
	public void setRankSalary(int rankSalary) {
		this.rankSalary = rankSalary;
	}
	public Long getRankSalaryTimes() {
		return rankSalaryTimes;
	}
	public void setRankSalaryTimes(Long rankSalaryTimes) {
		this.rankSalaryTimes = rankSalaryTimes;
	}
}
