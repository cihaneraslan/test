package com.cgiser.moka.model;

public class Story {
	private int storyId;
	private int cardId;
	private String storyInfo;
	private int stageId;
	private int left;
	private int state;
	public int getStoryId() {
		return storyId;
	}
	public void setStoryId(int storyId) {
		this.storyId = storyId;
	}
	public int getCardId() {
		return cardId;
	}
	public void setCardId(int cardId) {
		this.cardId = cardId;
	}
	public String getStoryInfo() {
		return storyInfo;
	}
	public void setStoryInfo(String storyInfo) {
		this.storyInfo = storyInfo;
	}
	public int getStageId() {
		return stageId;
	}
	public void setStageId(int stageId) {
		this.stageId = stageId;
	}
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}
