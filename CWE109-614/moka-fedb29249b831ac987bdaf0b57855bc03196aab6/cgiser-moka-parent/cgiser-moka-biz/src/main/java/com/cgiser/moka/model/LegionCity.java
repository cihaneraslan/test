package com.cgiser.moka.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;
/**
 * 军团战数据
 * @author Administrator
 *
 */
public class LegionCity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7282738770039184357L;
	private int id;
	private String name;
	private String legionName;
	private int coins;
	private Date bidTime;
	public int getCoins() {
		return coins;
	}
	public void setCoins(int coins) {
		this.coins = coins;
	}
	public Date getBidTime() {
		return bidTime;
	}
	public void setBidTime(Date bidTime) {
		this.bidTime = bidTime;
	}
	private Date nextStartTime;
	private Legion attackLegion;
	private Legion defendLegion;
	private List<LegionFighter> attackers;
	private List<LegionFighter> defenders;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLegionName() {
		return legionName;
	}
	public void setLegionName(String legionName) {
		this.legionName = legionName;
	}
	public Legion getAttackLegion() {
		return attackLegion;
	}
	public void setAttackLegion(Legion attackLegion) {
		this.attackLegion = attackLegion;
	}
	public Legion getDefendLegion() {
		return defendLegion;
	}
	public void setDefendLegion(Legion defendLegion) {
		this.defendLegion = defendLegion;
	}
	public List<LegionFighter> getAttackers() {
		if(CollectionUtils.isEmpty(attackers)){
			attackers = new ArrayList<LegionFighter>();
		}
		return attackers;
	}
	public void setAttackers(List<LegionFighter> attackers) {
		this.attackers = attackers;
	}
	public List<LegionFighter> getDefenders() {
		if(CollectionUtils.isEmpty(defenders)){
			defenders = new ArrayList<LegionFighter>();
		}
		return defenders;
	}
	public void setDefenders(List<LegionFighter> defenders) {
		this.defenders = defenders;
	}
	public Date getNextStartTime() {
		return nextStartTime;
	}
	public void setNextStartTime(Date nextStartTime) {
		this.nextStartTime = nextStartTime;
	}
}
