package com.cgiser.moka.model;

public class LegionLevel {
	private Long id;
	private int level;
	private int legionerNum;
	private int resource;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getLegionerNum() {
		return legionerNum;
	}
	public void setLegionerNum(int legionerNum) {
		this.legionerNum = legionerNum;
	}
	public int getResource() {
		return resource;
	}
	public void setResource(int resource) {
		this.resource = resource;
	}
}
