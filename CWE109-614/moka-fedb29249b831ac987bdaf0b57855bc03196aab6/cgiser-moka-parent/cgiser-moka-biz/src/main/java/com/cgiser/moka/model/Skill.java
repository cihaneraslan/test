package com.cgiser.moka.model;

import java.io.Serializable;

public class Skill implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2292457738456316209L;
	private int skillId;
	private String name;
	private int type;
	private String desc;
	private int lanchType;
	private int lanchCondition;
	private int lanchConditionValue;
	private int affectType;
	private int affectValue;
	private int affectValue2;
	private int skillCategory;
	public int getSkillId() {
		return skillId;
	}
	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getLanchType() {
		return lanchType;
	}
	public void setLanchType(int lanchType) {
		this.lanchType = lanchType;
	}
	public int getLanchCondition() {
		return lanchCondition;
	}
	public void setLanchCondition(int lanchCondition) {
		this.lanchCondition = lanchCondition;
	}
	public int getLanchConditionValue() {
		return lanchConditionValue;
	}
	public void setLanchConditionValue(int lanchConditionValue) {
		this.lanchConditionValue = lanchConditionValue;
	}
	public int getAffectType() {
		return affectType;
	}
	public void setAffectType(int affectType) {
		this.affectType = affectType;
	}
	public int getAffectValue() {
		return affectValue;
	}
	public void setAffectValue(int affectValue) {
		this.affectValue = affectValue;
	}
	public int getAffectValue2() {
		return affectValue2;
	}
	public void setAffectValue2(int affectValue2) {
		this.affectValue2 = affectValue2;
	}
	public int getSkillCategory() {
		return skillCategory;
	}
	public void setSkillCategory(int skillCategory) {
		this.skillCategory = skillCategory;
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return getSkillId();
	}
	
}
