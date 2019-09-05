package com.cgiser.moka.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cgiser.moka.model.AwardItem;

public class FaneResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4130338089629190471L;
	private List<AwardItem> awardList;
	private int coins;
	private Set<Integer> npcList;
	public FaneResult(){
		awardList = new ArrayList<AwardItem>();
		npcList = new HashSet<Integer>();
	}
	public List<AwardItem> getAwardList() {
		return awardList;
	}
	public void setAwardList(List<AwardItem> awardList) {
		this.awardList = awardList;
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
