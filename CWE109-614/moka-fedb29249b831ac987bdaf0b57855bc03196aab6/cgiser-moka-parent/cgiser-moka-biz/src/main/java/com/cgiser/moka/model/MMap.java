package com.cgiser.moka.model;

import java.util.List;

public class MMap {
	private int mapId;
	private int mazeCount;
	private String name;
	private int needStar;
	private int count;
	private int everyDayReward;
	private int next;
	private int prev;
	private double x;
	private double y;
	private List<Stage> stages;
	public int getMapId() {
		return mapId;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	public List<Stage> getStages() {
		return stages;
	}
	public void setStages(List<Stage> stages) {
		this.stages = stages;
	}

	public int getMazeCount() {
		return mazeCount;
	}
	public void setMazeCount(int mazeCount) {
		this.mazeCount = mazeCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNeedStar() {
		return needStar;
	}
	public void setNeedStar(int needStar) {
		this.needStar = needStar;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getEveryDayReward() {
		return everyDayReward;
	}
	public void setEveryDayReward(int everyDayReward) {
		this.everyDayReward = everyDayReward;
	}
	public int getNext() {
		return next;
	}
	public void setNext(int next) {
		this.next = next;
	}
	public int getPrev() {
		return prev;
	}
	public void setPrev(int prev) {
		this.prev = prev;
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
