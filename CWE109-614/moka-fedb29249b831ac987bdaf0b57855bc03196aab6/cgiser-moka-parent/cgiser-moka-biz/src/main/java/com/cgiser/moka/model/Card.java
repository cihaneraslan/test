package com.cgiser.moka.model;

import java.util.List;

import org.springframework.util.CollectionUtils;

public class Card {
	//卡牌ID
	private int cardId;
	//卡牌攻击力
	private int attack;
	//卡牌攻击数组
	private List<Integer> attackArray;
	//卡牌基础经验
	private int baseExp;
	//是否boss
	private int boss;
	//boss数量
	private int bossCounter;
	//卡牌名称
	private String cardName;
	//卡牌星级
	private int color;
	//卡牌Cost值
	private int cost;
	//卡牌经验数组
	private List<Integer> expArray;
	
	private int factionCounter;
	//
	private int fightMPacket;
	//
	private int fullImageId;
	private int Glory;
	//血量数组
	private List<Integer> hpArray;
	//卡牌对应图片ID
	private int imageId;
	//锁定技能
	private int lockSkill1;
	//锁定技能
	private int lockSkill2;
	//是否魔神卡牌
	private int magicCard;
	private int masterPacket;
	//是否迷宫卡牌
	private int maze;
	//卡牌价格
	private int price;
	//卡牌种族
	private int race;
	//是否盗贼
	private int robber;
	
	private int seniorPacket;
	//技能
	private int skill;
	//卡牌CD时间
	private int wait;
	//卡牌类型
	private int type;
	public int getCardId() {
		return cardId;
	}
	public void setCardId(int cardId) {
		this.cardId = cardId;
	}
	public int getAttack() {
		return attack;
	}
	public void setAttack(int attack) {
		this.attack = attack;
	}
	public List<Integer> getAttackArray() {
		return attackArray;
	}
	public void setAttackArray(List<Integer> attackArray) {
		this.attackArray = attackArray;
	}
	public int getBaseExp() {
		return baseExp;
	}
	public void setBaseExp(int baseExp) {
		this.baseExp = baseExp;
	}
	public int getBoss() {
		return boss;
	}
	public void setBoss(int boss) {
		this.boss = boss;
	}
	public int getBossCounter() {
		return bossCounter;
	}
	public void setBossCounter(int bossCounter) {
		this.bossCounter = bossCounter;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public List<Integer> getExpArray() {
		return expArray;
	}
	public void setExpArray(List<Integer> expArray) {
		this.expArray = expArray;
	}
	public int getFactionCounter() {
		return factionCounter;
	}
	public void setFactionCounter(int factionCounter) {
		this.factionCounter = factionCounter;
	}
	public int getFightMPacket() {
		return fightMPacket;
	}
	public void setFightMPacket(int fightMPacket) {
		this.fightMPacket = fightMPacket;
	}
	public int getFullImageId() {
		return fullImageId;
	}
	public void setFullImageId(int fullImageId) {
		this.fullImageId = fullImageId;
	}
	public int getGlory() {
		return Glory;
	}
	public void setGlory(int glory) {
		Glory = glory;
	}
	public List<Integer> getHpArray() {
		return hpArray;
	}
	public void setHpArray(List<Integer> hpArray) {
		this.hpArray = hpArray;
	}
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	public int getLockSkill1() {
		return lockSkill1;
	}
	public void setLockSkill1(int lockSkill1) {
		this.lockSkill1 = lockSkill1;
	}
	public int getLockSkill2() {
		return lockSkill2;
	}
	public void setLockSkill2(int lockSkill2) {
		this.lockSkill2 = lockSkill2;
	}
	public int getMagicCard() {
		return magicCard;
	}
	public void setMagicCard(int magicCard) {
		this.magicCard = magicCard;
	}
	public int getMasterPacket() {
		return masterPacket;
	}
	public void setMasterPacket(int masterPacket) {
		this.masterPacket = masterPacket;
	}
	public int getMaze() {
		return maze;
	}
	public void setMaze(int maze) {
		this.maze = maze;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getRace() {
		return race;
	}
	public void setRace(int race) {
		this.race = race;
	}
	public int getRobber() {
		return robber;
	}
	public void setRobber(int robber) {
		this.robber = robber;
	}
	public int getSeniorPacket() {
		return seniorPacket;
	}
	public void setSeniorPacket(int seniorPacket) {
		this.seniorPacket = seniorPacket;
	}
	public int getSkill() {
		return skill;
	}
	public void setSkill(int skill) {
		this.skill = skill;
	}
	public int getWait() {
		return wait;
	}
	public void setWait(int wait) {
		this.wait = wait;
	}
	public int getLevel(int exp){
		if(CollectionUtils.isEmpty(this.expArray)){
			return 0;
		}
		int exp1 = 0;
		for(int i=0;i<this.expArray.size();i++){
			exp1 = this.expArray.get(i);
			if(exp<exp1){
				return i-1;
			}
		}
		return 10;
	}
	public int getExp(int level){
		if(level>10){
			level = 10;
		}
		int exp = 0;
		for(int i=0;i<level+1;i++){
			exp = this.expArray.get(i);
		}
		return exp;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
