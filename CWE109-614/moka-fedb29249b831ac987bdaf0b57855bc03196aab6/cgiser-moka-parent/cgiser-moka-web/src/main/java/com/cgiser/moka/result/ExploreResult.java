package com.cgiser.moka.result;

import java.util.List;

import com.cgiser.moka.model.Salary;

public class ExploreResult {
	private int energyExplore;
	private List<Salary> salaryList;
	private int extraExp;
	private int extraCoins;
	public int getEnergyExplore() {
		return energyExplore;
	}
	public void setEnergyExplore(int energyExplore) {
		this.energyExplore = energyExplore;
	}
	public List<Salary> getSalaryList() {
		return salaryList;
	}
	public void setSalaryList(List<Salary> salaryList) {
		this.salaryList = salaryList;
	}
	public int getExtraExp() {
		return extraExp;
	}
	public void setExtraExp(int extraExp) {
		this.extraExp = extraExp;
	}
	public int getExtraCoins() {
		return extraCoins;
	}
	public void setExtraCoins(int extraCoins) {
		this.extraCoins = extraCoins;
	}
}
