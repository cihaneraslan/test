package com.cgiser.moka.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FightCard implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9061551689856122311L;
	private int attack;
	private int cardId;
	private int evolution;
	private int hP;
	private int level;
	private List<Skill> skillBuff; 
	private List<Skill> skillNew;
	private Long userCardId;
	private String UUID;
	private int wait;
	private int isAttack;
	private UserSoul soul;
	private int soulRound;
	/**
	 * 获取武器技能持续回合数
	 * @return
	 */
	public int getSoulRound() {
		return soulRound;
	}
	public void setSoulRound(int soulRound) {
		this.soulRound = soulRound;
	}
	public int getAttack() {
		return attack;
	}
	public void setAttack(int attack) {
		this.attack = attack;
	}
	public int getCardId() {
		return cardId;
	}
	public void setCardId(int cardId) {
		this.cardId = cardId;
	}
	public int getEvolution() {
		return evolution;
	}
	public void setEvolution(int evolution) {
		this.evolution = evolution;
	}
	public int gethP() {
		return hP;
	}
	public void sethP(int hP) {
		this.hP = hP;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public List<Skill> getSkillNew() {
		if(this.skillNew==null){
			skillNew = new ArrayList<Skill>();
		}
		return skillNew;
	}
	public void setSkillNew(List<Skill> skillNew) {
		this.skillNew = skillNew;
	}
	public Long getUserCardId() {
		return userCardId;
	}
	public void setUserCardId(Long userCardId) {
		this.userCardId = userCardId;
	}
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public int getWait() {
		return wait;
	}
	public void setWait(int wait) {
		this.wait = wait;
	}
	public int getIsAttack() {
		return isAttack;
	}
	public void setIsAttack(int isAttack) {
		this.isAttack = isAttack;
	}
	public FightCard clone(){
		FightCard fightCard = new FightCard();
		fightCard.setAttack(attack);
		fightCard.setCardId(cardId);
		fightCard.setEvolution(evolution);
		fightCard.sethP(hP);
		fightCard.setIsAttack(isAttack);
		fightCard.setLevel(level);
		fightCard.setSkillNew(skillNew);
		fightCard.setUserCardId(userCardId);
		fightCard.setUUID(UUID);
		fightCard.setWait(wait);
		fightCard.setSkillBuff(skillBuff);
		fightCard.setSoul(soul);
		fightCard.setSoulRound(soulRound);
		return fightCard;
	}
	public List<Skill> getSkillBuff() {
		if(this.skillBuff==null){
			skillBuff = new ArrayList<Skill>();
		}
		return skillBuff;
	}
	public void setSkillBuff(List<Skill> skillBuff) {
		this.skillBuff = skillBuff;
	}
	public UserSoul getSoul() {
		return soul;
	}
	public void setSoul(UserSoul soul) {
		this.soul = soul;
	}

}
