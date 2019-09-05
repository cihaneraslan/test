package com.cgiser.moka.result;

import java.io.Serializable;
import java.util.Set;

import com.cgiser.moka.model.AwardItem;

public class AwardResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2495423342176027572L;
	private AwardItem awardItem;
	private int coins;
	private Set<Integer> npcList;
	public AwardItem getAwardItem() {
		return awardItem;
	}
	public void setAwardItem(AwardItem awardItem) {
		this.awardItem = awardItem;
	}
	public Set<Integer> getNpcList() {
		return npcList;
	}
	public void setNpcList(Set<Integer> npcList) {
		this.npcList = npcList;
	}
	public int getCoins() {
		return coins;
	}
	public void setCoins(int coins) {
		this.coins = coins;
	}

}
