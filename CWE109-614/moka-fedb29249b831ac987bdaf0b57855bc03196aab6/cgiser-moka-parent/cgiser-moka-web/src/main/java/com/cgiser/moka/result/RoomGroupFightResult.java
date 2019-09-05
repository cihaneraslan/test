package com.cgiser.moka.result;

import java.util.List;

import com.cgiser.moka.model.GroupFight;
import com.cgiser.moka.model.Salary;

public class RoomGroupFightResult {
	private String turnInfo;
	private List<Salary> salaries;
	private int win;
	private int turns;
	private GroupFight groupFight;
	public List<Salary> getSalaries() {
		return salaries;
	}
	public void setSalaries(List<Salary> salaries) {
		this.salaries = salaries;
	}
	public int getWin() {
		return win;
	}
	public void setWin(int win) {
		this.win = win;
	}
	public String getTurnInfo() {
		return turnInfo;
	}
	public void setTurnInfo(String turnInfo) {
		this.turnInfo = turnInfo;
	}
	public int getTurns() {
		return turns;
	}
	public void setTurns(int turns) {
		this.turns = turns;
	}
	public GroupFight getGroupFight() {
		return groupFight;
	}
	public void setGroupFight(GroupFight groupFight) {
		this.groupFight = groupFight;
	}
}
