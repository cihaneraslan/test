package com.cgiser.moka.model;

import java.io.Serializable;

public class BidLegion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7095257613888420073L;
	private int id;
	private String cityName;
	private String legionName;
	private int coins;
	private int state;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getLegionName() {
		return legionName;
	}
	public void setLegionName(String legionName) {
		this.legionName = legionName;
	}
	public int getCoins() {
		return coins;
	}
	public void setCoins(int coins) {
		this.coins = coins;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}
