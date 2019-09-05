package com.cgiser.moka.result;

import com.cgiser.moka.model.FightResult;
import com.cgiser.moka.model.GroupFight;

public class GroupFightResult {
	private GroupFight groupFight;
	private FightResult fightResult;
	private int turns;
	private int win;
	public FightResult getFightResult() {
		return fightResult;
	}
	public void setFightResult(FightResult fightResult) {
		this.fightResult = fightResult;
	}
	public GroupFight getGroupFight() {
		return groupFight;
	}
	public void setGroupFight(GroupFight groupFight) {
		this.groupFight = groupFight;
	}
	public int getWin() {
		return win;
	}
	public void setWin(int win) {
		this.win = win;
	}
	public int getTurns() {
		return turns;
	}
	public void setTurns(int turns) {
		this.turns = turns;
	}
}
