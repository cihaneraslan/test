package com.cgiser.moka.model;

import java.util.List;

public class Stage {
	private int stageId;
	private int mapId;
	private String name;
	private String fightName;
	private int fightImg;
	private List<StageLevel> levels;
	private int next;
	private int nextBranch;
	private int prev;
	private int rank;
	private int type;
	private double x;
	private double y;

	public int getStageId() {
		return stageId;
	}
	public void setStageId(int stageId) {
		this.stageId = stageId;
	}
	public int getMapId() {
		return mapId;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFightName() {
		return fightName;
	}
	public void setFightName(String fightName) {
		this.fightName = fightName;
	}
	public int getFightImg() {
		return fightImg;
	}
	public void setFightImg(int fightImg) {
		this.fightImg = fightImg;
	}
	public List<StageLevel> getLevels() {
		return levels;
	}
	public void setLevels(List<StageLevel> levels) {
		this.levels = levels;
	}
	public int getNext() {
		return next;
	}
	public void setNext(int next) {
		this.next = next;
	}
	public int getNextBranch() {
		return nextBranch;
	}
	public void setNextBranch(int nextBranch) {
		this.nextBranch = nextBranch;
	}
	public int getPrev() {
		return prev;
	}
	public void setPrev(int prev) {
		this.prev = prev;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
}
