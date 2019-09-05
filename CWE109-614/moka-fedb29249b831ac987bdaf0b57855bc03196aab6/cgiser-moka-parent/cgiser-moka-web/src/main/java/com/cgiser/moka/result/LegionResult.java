package com.cgiser.moka.result;

import java.util.List;

import com.cgiser.moka.model.Drum;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionTech;
import com.cgiser.moka.model.Legioner;

public class LegionResult {
	private int count;
	private List<Legion> legionInfos;
	private Legioner myInfo;
	private Legion myLegion;
	private List<LegionTech> teachs;
	private int cash;
	private int coins;
	private int nextLegionResource;
	private List<Drum> drums;
	private int buyLegionAttackCash;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<Legion> getLegionInfos() {
		return legionInfos;
	}
	public void setLegionInfos(List<Legion> legionInfos) {
		this.legionInfos = legionInfos;
	}
	public Legioner getMyInfo() {
		return myInfo;
	}
	public void setMyInfo(Legioner myInfo) {
		this.myInfo = myInfo;
	}
	public Legion getMyLegion() {
		return myLegion;
	}
	public void setMyLegion(Legion myLegion) {
		this.myLegion = myLegion;
	}
	public List<LegionTech> getTeach() {
		return teachs;
	}
	public void setTeach(List<LegionTech> teachs) {
		this.teachs = teachs;
	}
	public int getCash() {
		return cash;
	}
	public void setCash(int cash) {
		this.cash = cash;
	}
	public int getCoins() {
		return coins;
	}
	public void setCoins(int coins) {
		this.coins = coins;
	}
	public int getNextLegionResource() {
		return nextLegionResource;
	}
	public void setNextLegionResource(int nextLegionResource) {
		this.nextLegionResource = nextLegionResource;
	}
	public List<Drum> getDrums() {
		return drums;
	}
	public void setDrums(List<Drum> drums) {
		this.drums = drums;
	}
	public int getBuyLegionAttackCash() {
		return buyLegionAttackCash;
	}
	public void setBuyLegionAttackCash(int buyLegionAttackCash) {
		this.buyLegionAttackCash = buyLegionAttackCash;
	}
}
