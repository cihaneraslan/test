package com.cgiser.moka.model;

import java.io.Serializable;
import java.util.List;

public class ExtData implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7539681640205713327L;
	public List<String> Bonus;
	public List<String> FirstBonusWin;
	public List<String> addBonus;
	public List<String> AwardCards;
	public int UserLevel = -1;
	public int PrevExp = -1;
	public int NextExp = -1;
	public int Exp = -1;
	public int StarUp;
	public Object Award;
	public Object Clear;
	public Object User;
	public List<String> getBonus() {
		return Bonus;
	}
	public void setBonus(List<String> bonus) {
		Bonus = bonus;
	}
	public List<String> getFirstBonusWin() {
		return FirstBonusWin;
	}
	public void setFirstBonusWin(List<String> firstBonusWin) {
		FirstBonusWin = firstBonusWin;
	}
	public List<String> getAddBonus() {
		return addBonus;
	}
	public void setAddBonus(List<String> addBonus) {
		this.addBonus = addBonus;
	}
	public List<String> getAwardCards() {
		return AwardCards;
	}
	public void setAwardCards(List<String> awardCards) {
		AwardCards = awardCards;
	}
	public int getUserLevel() {
		return UserLevel;
	}
	public void setUserLevel(int userLevel) {
		UserLevel = userLevel;
	}
	public int getPrevExp() {
		return PrevExp;
	}
	public void setPrevExp(int prevExp) {
		PrevExp = prevExp;
	}
	public int getNextExp() {
		return NextExp;
	}
	public void setNextExp(int nextExp) {
		NextExp = nextExp;
	}
	public int getExp() {
		return Exp;
	}
	public void setExp(int exp) {
		Exp = exp;
	}
	public int getStarUp() {
		return StarUp;
	}
	public void setStarUp(int starUp) {
		StarUp = starUp;
	}
	public Object getAward() {
		return Award;
	}
	public void setAward(Object award) {
		Award = award;
	}
	public Object getClear() {
		return Clear;
	}
	public void setClear(Object clear) {
		Clear = clear;
	}
	public Object getUser() {
		return User;
	}
	public void setUser(Object user) {
		User = user;
	}
}
