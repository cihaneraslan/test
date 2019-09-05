package com.cgiser.moka.model;

import java.util.List;

public class LegionTech {
	private Long id;
	private String techId;
	private String techName;
	private String techEffect;
	private List<Integer> resourceArray;
	private int techLevel;
	private int lock;
	private int legionLevel;
	private Long contribute;
	private Long nextContribute;
	private int race;
	private int type;
	private float and;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTechId() {
		return techId;
	}
	public void setTechId(String techId) {
		this.techId = techId;
	}
	public String getTechName() {
		return techName;
	}
	public void setTechName(String techName) {
		this.techName = techName;
	}
	public String getTechEffect() {
		return techEffect;
	}
	public void setTechEffect(String techEffect) {
		this.techEffect = techEffect;
	}
	public List<Integer> getResourceArray() {
		return resourceArray;
	}
	public void setResourceArray(List<Integer> resourceArray) {
		this.resourceArray = resourceArray;
	}
	public int getTechLevel() {
		return techLevel;
	}
	public void setTechLevel(int techLevel) {
		this.techLevel = techLevel;
	}
	
	public int getLegionTechLevel(Long resource){
		
		for(int i=0;i<resourceArray.size();i++){
			if(resource<resourceArray.get(i)){
				return i -1;
			}
		}
		return 10;
	}
	public int getContributeByLevel(int level){
		return resourceArray.get(level);
	}
	public int getLock() {
		return lock;
	}
	public void setLock(int lock) {
		this.lock = lock;
	}
	public int getLegionLevel() {
		return legionLevel;
	}
	public void setLegionLevel(int legionLevel) {
		this.legionLevel = legionLevel;
	}
	public Long getContribute() {
		return contribute;
	}
	public void setContribute(Long contribute) {
		this.contribute = contribute;
	}
	public Long getNextContribute() {
		return nextContribute;
	}
	public void setNextContribute(Long nextContribute) {
		this.nextContribute = nextContribute;
	}
	public int getRace() {
		return race;
	}
	public void setRace(int race) {
		this.race = race;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public float getAnd() {
		return and;
	}
	public void setAnd(float and) {
		this.and = and;
	}
	
}
