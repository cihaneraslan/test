package com.cgiser.moka.result;

import java.util.List;

public class MapLastInComeResult {
	private List<RobRole> roles;
	private int lastCoins;
	private int coins;
	public List<RobRole> getRoles() {
		return roles;
	}
	public void setRoles(List<RobRole> roles) {
		this.roles = roles;
	}
	public int getLastCoins() {
		return lastCoins;
	}
	public void setLastCoins(int lastCoins) {
		this.lastCoins = lastCoins;
	}
	public int getCoins() {
		return coins;
	}
	public void setCoins(int coins) {
		this.coins = coins;
	}
}
