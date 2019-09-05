package com.cgiser.moka.result;

import java.util.ArrayList;
import java.util.List;

import com.cgiser.moka.model.RewardItem;

public class DealResult {
	private int coins;
	private List<RewardItem> rewards;
	private List<RewardItem> sells;
	public DealResult(){
		rewards = new ArrayList<RewardItem>();
		sells = new ArrayList<RewardItem>();
	}
	public int getCoins() {
		return coins;
	}
	public void setCoins(int coins) {
		this.coins = coins;
	}
	public List<RewardItem> getRewards() {
		return rewards;
	}
	public void setRewards(List<RewardItem> rewards) {
		this.rewards = rewards;
	}
	public List<RewardItem> getSells() {
		return sells;
	}
	public void setSells(List<RewardItem> sells) {
		this.sells = sells;
	}
}
