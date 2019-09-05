package com.cgiser.moka.model;

import java.io.Serializable;
import java.util.List;

public class Battle implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 705177795817814791L;
	private List<Round> rounds;
	public List<Round> getRounds() {
		return rounds;
	}
	public void setRounds(List<Round> rounds) {
		this.rounds = rounds;
	}
}
