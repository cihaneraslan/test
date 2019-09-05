package com.cgiser.moka.result;

import java.io.Serializable;

public class MatchGameSet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1308466124540312527L;
	private Long matchLastTime;
	private int matchHasTimes;
	private Long matchCutDown;
	private int matchTimesCash;
	private int isStart;
	public Long getMatchLastTime() {
		return matchLastTime;
	}
	public void setMatchLastTime(Long matchLastTime) {
		this.matchLastTime = matchLastTime;
	}
	public int getMatchHasTimes() {
		return matchHasTimes;
	}
	public void setMatchHasTimes(int matchHasTimes) {
		this.matchHasTimes = matchHasTimes;
	}
	public Long getMatchCutDown() {
		return matchCutDown;
	}
	public void setMatchCutDown(Long matchCutDown) {
		this.matchCutDown = matchCutDown;
	}
	public int getMatchTimesCash() {
		return matchTimesCash;
	}
	public void setMatchTimesCash(int matchTimesCash) {
		this.matchTimesCash = matchTimesCash;
	}
	public int getIsStart() {
		return isStart;
	}
	public void setIsStart(int isStart) {
		this.isStart = isStart;
	}

}
