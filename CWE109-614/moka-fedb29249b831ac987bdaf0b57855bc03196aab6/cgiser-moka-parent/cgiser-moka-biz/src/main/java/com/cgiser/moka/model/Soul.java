package com.cgiser.moka.model;

import java.io.Serializable;

public class Soul implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1613496284305836722L;
	
	private int soulId;
	private String soulName;
	private int attack;
	private int hp;
	private int soulType;
	private int round;
	private String Desc;
	private int color;
	private int random;
	private int honor;
	private int price;
	public int getSoulId() {
		return soulId;
	}
	public void setSoulId(int soulId) {
		this.soulId = soulId;
	}
	public String getSoulName() {
		return soulName;
	}
	public void setSoulName(String soulName) {
		this.soulName = soulName;
	}
	public int getAttack() {
		return attack;
	}
	public void setAttack(int attack) {
		this.attack = attack;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getSoulType() {
		return soulType;
	}
	public void setSoulType(int soulType) {
		this.soulType = soulType;
	}
	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
	}
	public String getDesc() {
		return Desc;
	}
	public void setDesc(String desc) {
		Desc = desc;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getRandom() {
		return random;
	}
	public void setRandom(int random) {
		this.random = random;
	}
	public int getHonor() {
		return honor;
	}
	public void setHonor(int honor) {
		this.honor = honor;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	

	
}
