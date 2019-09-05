package com.cgiser.moka.model;

import java.util.List;

public class StageLevel {
	private int id;
	private int stageId;
	private int level;
	private String cardList;
	private String runeList;
	private int heroLevel;
	private int achievementId;
	private String achievementText;
	public String getAchievementText() {
		return achievementText;
	}
	public void setAchievementText(String achievementText) {
		this.achievementText = achievementText;
	}
	private int energyExpEnd;
	private int bonusWinExp;
	private int bonusWinGold;
	private int firstBonusWinCard;
	private int firstBonusWinRune;
	private int bonusLoseExp;
	private int bonusLoseGold;
	private int addedBonus;
	private int energyExplore;
	private int bonusExplore;
	private int hint;
	private String soulList;
	private List<StageLevelColor> stageLevelColors;

	public int getStageId() {
		return stageId;
	}
	public void setStageId(int stageId) {
		this.stageId = stageId;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getCardList() {
		return cardList;
	}
	public void setCardList(String cardList) {
		this.cardList = cardList;
	}
	public String getRuneList() {
		return runeList;
	}
	public void setRuneList(String runeList) {
		this.runeList = runeList;
	}
	public int getHeroLevel() {
		return heroLevel;
	}
	public void setHeroLevel(int heroLevel) {
		this.heroLevel = heroLevel;
	}
	public int getAchievementId() {
		return achievementId;
	}
	public void setAchievementId(int achievementId) {
		this.achievementId = achievementId;
	}
	public int getEnergyExpEnd() {
		return energyExpEnd;
	}
	public void setEnergyExpEnd(int energyExpEnd) {
		this.energyExpEnd = energyExpEnd;
	}
	public int getBonusWinExp() {
		return bonusWinExp;
	}
	public void setBonusWinExp(int bonusWinExp) {
		this.bonusWinExp = bonusWinExp;
	}
	public int getBonusWinGold() {
		return bonusWinGold;
	}
	public void setBonusWinGold(int bonusWinGold) {
		this.bonusWinGold = bonusWinGold;
	}
	public int getFirstBonusWinCard() {
		return firstBonusWinCard;
	}
	public void setFirstBonusWinCard(int firstBonusWinCard) {
		this.firstBonusWinCard = firstBonusWinCard;
	}
	public int getFirstBonusWinRune() {
		return firstBonusWinRune;
	}
	public void setFirstBonusWinRune(int firstBonusWinRune) {
		this.firstBonusWinRune = firstBonusWinRune;
	}
	public int getBonusLoseExp() {
		return bonusLoseExp;
	}
	public void setBonusLoseExp(int bonusLoseExp) {
		this.bonusLoseExp = bonusLoseExp;
	}
	public int getBonusLoseGold() {
		return bonusLoseGold;
	}
	public void setBonusLoseGold(int bonusLoseGold) {
		this.bonusLoseGold = bonusLoseGold;
	}
	public int getAddedBonus() {
		return addedBonus;
	}
	public void setAddedBonus(int addedBonus) {
		this.addedBonus = addedBonus;
	}
	public int getEnergyExplore() {
		return energyExplore;
	}
	public void setEnergyExplore(int energyExplore) {
		this.energyExplore = energyExplore;
	}
	public int getBonusExplore() {
		return bonusExplore;
	}
	public void setBonusExplore(int bonusExplore) {
		this.bonusExplore = bonusExplore;
	}
	public int getHint() {
		return hint;
	}
	public void setHint(int hint) {
		this.hint = hint;
	}
	public List<StageLevelColor> getStageLevelColors() {
		return stageLevelColors;
	}
	public void setStageLevelColors(List<StageLevelColor> stageLevelColors) {
		this.stageLevelColors = stageLevelColors;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSoulList() {
		return soulList;
	}
	public void setSoulList(String soulList) {
		this.soulList = soulList;
	}
}
