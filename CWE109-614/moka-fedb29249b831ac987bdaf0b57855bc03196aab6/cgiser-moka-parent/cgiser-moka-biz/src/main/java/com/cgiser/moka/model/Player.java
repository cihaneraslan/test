package com.cgiser.moka.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player implements Serializable {
	/**
	 * 
	 */
	Logger logger = LoggerFactory.getLogger("skill");
	private static final long serialVersionUID = 8336437975282680561L;
	private int avatar;
	private List<FightCard> cards;
	private int HP;
	private int level;
	private String nickName;
	private int remainHP;
	private List<FightRune> runes;
	private int sex;
	private Long roleId;
	private List<FightCard> handsCards;
	private List<FightCard> preCards;
	private Map<String,FightCard> fightCards;
	private List<FightCard> tumbCards;
	private String name;
	private boolean isHand;
	public Player(){
		this.cards = new ArrayList<FightCard>();
		this.runes = new ArrayList<FightRune>();
	}
	public int getAvatar() {
		return avatar;
	}
	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}
	public List<FightCard> getCards() {
		return cards;
	}
	public void setCards(List<FightCard> cards) {
		this.cards = cards;
	}
	public int getHP() {
		return HP;
	}
	public void setHP(int hP) {
		if(hP<=0){
			logger.debug(this.name+"已经死亡");
		}else{
			if(HP-hP<0){
				logger.debug(this.name+"血量增加"+(hP-HP));
			}else{
				logger.debug(this.name+"血量减少"+(HP-hP));
			}
			
		}
		HP = hP;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getRemainHP() {
		return remainHP;
	}
	public void setRemainHP(int remainHP) {
		this.remainHP = remainHP;
	}
	public List<FightRune> getRunes() {
		return runes;
	}
	public void setRunes(List<FightRune> runes) {
		this.runes = runes;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public List<FightCard> getHandsCards() {
		return handsCards;
	}
	public void setHandsCards(List<FightCard> handsCards) {
		this.handsCards = handsCards;
	}
	public List<FightCard> getPreCards() {
		return preCards;
	}
	public void setPreCards(List<FightCard> preCards) {
		this.preCards = preCards;
	}
	public Map<String, FightCard> getFightCards() {
		return fightCards;
	}
	public void setFightCards(Map<String, FightCard> fightCards) {
		this.fightCards = fightCards;
	}
	public List<FightCard> getTumbCards() {
		return tumbCards;
	}
	public void setTumbCards(List<FightCard> tumbCards) {
		this.tumbCards = tumbCards;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isHand() {
		return isHand;
	}
	public void setHand(boolean isHand) {
		this.isHand = isHand;
	}
	
}
