package com.cgiser.moka.model;

public class Drum {
	private int id;
	private int everyDayTimes;
	private int everyDayFreeTimes;
	private int currencyType;
	private int currencyValue;
	private int honor;
	public int getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(int currencyType) {
		this.currencyType = currencyType;
	}
	public int getCurrencyValue() {
		return currencyValue;
	}
	public void setCurrencyValue(int currencyValue) {
		this.currencyValue = currencyValue;
	}
	public int getHonor() {
		return honor;
	}
	public void setHonor(int honor) {
		this.honor = honor;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEveryDayTimes() {
		return everyDayTimes;
	}
	public int getEveryDayFreeTimes() {
		return everyDayFreeTimes;
	}
	public void setEveryDayFreeTimes(int everyDayFreeTimes) {
		this.everyDayFreeTimes = everyDayFreeTimes;
	}
	public void setEveryDayTimes(int everyDayTimes) {
		this.everyDayTimes = everyDayTimes;
	}
}
