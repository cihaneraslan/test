package com.cgiser.moka.model;

import java.io.Serializable;
import java.util.List;


public class Round implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5792665390632763491L;
	private boolean isAttack;
	private List<Opp> opps;
	private int round;
	public boolean getIsAttack() {
		return isAttack;
	}
	public void setIsAttack(boolean isAttack) {
		this.isAttack = isAttack;
	}
	public List<Opp> getOpps() {
		return opps;
	}
	public void setOpps(List<Opp> opps) {
		this.opps = opps;
	}
	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
	}
}
