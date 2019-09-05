package com.cgiser.moka.model;

import java.util.List;

import org.springframework.util.CollectionUtils;

public class Rune {
    private int RuneId;
	private String RuneName;
	private int Property;
	private int Color;
	private int LockSkill1;
	private int LockSkill2;
	private int LockSkill3;
	private int LockSkill4;
	private int LockSkill5;
	private int Price;
	private List<Integer> ExpArray;
	private int SkillTimes;
	private String Condition;
	private int SkillConditionSlide;
	private int SkillConditionType;
	private int SkillConditionRace;
	private int SkillConditionColor;
	private int SkillConditionCompare;
	private int SkillConditionValue;
	private int ThinkGet;
	private int Fragment;
	private int BaseExp;
//	private int UserRuneId;
//	private int Level;
	private int Exp;
	public int getRuneId() {
		return RuneId;
	}
	public void setRuneId(int runeId) {
		RuneId = runeId;
	}
	public String getRuneName() {
		return RuneName;
	}
	public void setRuneName(String runeName) {
		RuneName = runeName;
	}
	public int getProperty() {
		return Property;
	}
	public void setProperty(int property) {
		Property = property;
	}
	public int getColor() {
		return Color;
	}
	public void setColor(int color) {
		Color = color;
	}

	public int getLockSkill1() {
		return LockSkill1;
	}
	public void setLockSkill1(int lockSkill1) {
		LockSkill1 = lockSkill1;
	}
	public int getLockSkill2() {
		return LockSkill2;
	}
	public void setLockSkill2(int lockSkill2) {
		LockSkill2 = lockSkill2;
	}
	public int getLockSkill3() {
		return LockSkill3;
	}
	public void setLockSkill3(int lockSkill3) {
		LockSkill3 = lockSkill3;
	}
	public int getLockSkill4() {
		return LockSkill4;
	}
	public void setLockSkill4(int lockSkill4) {
		LockSkill4 = lockSkill4;
	}
	public int getLockSkill5() {
		return LockSkill5;
	}
	public void setLockSkill5(int lockSkill5) {
		LockSkill5 = lockSkill5;
	}
	public int getPrice() {
		return Price;
	}
	public void setPrice(int price) {
		Price = price;
	}
	public List<Integer> getExpArray() {
		return ExpArray;
	}
	public void setExpArray(List<Integer> expArray) {
		ExpArray = expArray;
	}
	public int getSkillTimes() {
		return SkillTimes;
	}
	public void setSkillTimes(int skillTimes) {
		SkillTimes = skillTimes;
	}
	public String getCondition() {
		return Condition;
	}
	public void setCondition(String condition) {
		Condition = condition;
	}
	public int getSkillConditionSlide() {
		return SkillConditionSlide;
	}
	public void setSkillConditionSlide(int skillConditionSlide) {
		SkillConditionSlide = skillConditionSlide;
	}
	public int getSkillConditionType() {
		return SkillConditionType;
	}
	public void setSkillConditionType(int skillConditionType) {
		SkillConditionType = skillConditionType;
	}
	public int getSkillConditionRace() {
		return SkillConditionRace;
	}
	public void setSkillConditionRace(int skillConditionRace) {
		SkillConditionRace = skillConditionRace;
	}
	public int getSkillConditionColor() {
		return SkillConditionColor;
	}
	public void setSkillConditionColor(int skillConditionColor) {
		SkillConditionColor = skillConditionColor;
	}
	public int getSkillConditionCompare() {
		return SkillConditionCompare;
	}
	public void setSkillConditionCompare(int skillConditionCompare) {
		SkillConditionCompare = skillConditionCompare;
	}
	public int getSkillConditionValue() {
		return SkillConditionValue;
	}
	public void setSkillConditionValue(int skillConditionValue) {
		SkillConditionValue = skillConditionValue;
	}
	public int getThinkGet() {
		return ThinkGet;
	}
	public void setThinkGet(int thinkGet) {
		ThinkGet = thinkGet;
	}
	public int getFragment() {
		return Fragment;
	}
	public void setFragment(int fragment) {
		Fragment = fragment;
	}
	public int getBaseExp() {
		return BaseExp;
	}
	public void setBaseExp(int baseExp) {
		BaseExp = baseExp;
	}
//	public int getLevel() {
//		return Level;
//	}
//	public void setLevel(int level) {
//		Level = level;
//	}
	public int getExp() {
		return Exp;
	}
	public void setExp(int exp) {
		Exp = exp;
	}
	public int getSkillIdByLevel(int level){
		if(level==0){
			return this.getLockSkill1();
		}
		if(level==1){
			return this.getLockSkill2();
		}
		if(level==2){
			return this.getLockSkill3();
		}
		if(level==3){
			return this.getLockSkill4();
		}
		if(level==4){
			return this.getLockSkill5();
		}
		return 0;
	}
	public int getLevel(int exp){
		if(CollectionUtils.isEmpty(this.ExpArray)){
			return 0;
		}
		int exp1 = 0;
		for(int i=0;i<this.ExpArray.size();i++){
			exp1 = this.ExpArray.get(i);
			if(exp<exp1){
				return i-1;
			}
		}
		return 4;
	}
	public int getExp(int level){
		if(level>4){
			level = 4;
		}
		int exp = 0;
		for(int i=0;i<level+1;i++){
			exp = this.ExpArray.get(i);
		}
		return exp;
	}
}
