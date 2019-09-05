package com.cgiser.moka.model;

import java.io.Serializable;
import java.util.List;

import com.cgiser.moka.model.Battle;
import com.cgiser.moka.model.Player;

public class FightResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1546974934575895136L;
	private Player attackPlayer;
	private Battle battle;
	private String battleId;
	private Player defendPlayer;
	private ExtData extData;
	private String prepare;
	private int win;
	private int type;
	private List<Salary> salaries;
	private List<Salary> dSalaries;
	public Player getAttackPlayer() {
		return attackPlayer;
	}
	public void setAttackPlayer(Player attackPlayer) {
		this.attackPlayer = attackPlayer;
	}
	public Battle getBattle() {
		return battle;
	}
	public void setBattle(Battle battle) {
		this.battle = battle;
	}
	public String getBattleId() {
		return battleId;
	}
	public void setBattleId(String battleId) {
		this.battleId = battleId;
	}
	public Player getDefendPlayer() {
		return defendPlayer;
	}
	public void setDefendPlayer(Player defendPlayer) {
		this.defendPlayer = defendPlayer;
	}
	public ExtData getExtData() {
		return extData;
	}
	public void setExtData(ExtData extData) {
		this.extData = extData;
	}
	public String getPrepare() {
		return prepare;
	}
	public void setPrepare(String prepare) {
		this.prepare = prepare;
	}
	public int getWin() {
		return win;
	}
	public void setWin(int win) {
		this.win = win;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public List<Salary> getSalaries() {
		return salaries;
	}
	public void setSalaries(List<Salary> salaries) {
		this.salaries = salaries;
	}
	public List<Salary> getdSalaries() {
		return dSalaries;
	}
	public void setdSalaries(List<Salary> dSalaries) {
		this.dSalaries = dSalaries;
	}

}
