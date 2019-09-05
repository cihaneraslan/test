package com.cgiser.moka.result;

import java.util.Map;

import com.cgiser.moka.model.LegionGuard;

public class LegionGuardResult {

	private String legionName;
	private Long legionId;
	private int level;
	private Map<String,LegionGuard> guards;
	public String getLegionName() {
		return legionName;
	}
	public void setLegionName(String legionName) {
		this.legionName = legionName;
	}
	public Long getLegionId() {
		return legionId;
	}
	public void setLegionId(Long legionId) {
		this.legionId = legionId;
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Map<String,LegionGuard> getGuards() {
		return guards;
	}
	public void setGuards(Map<String,LegionGuard> guards) {
		this.guards = guards;
	}
}
