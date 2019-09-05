package com.cgiser.moka.result;

import java.util.List;
import java.util.Map;

public class LegionFightResult {
	private Long legionFightId;
	private String legionName;
	private Long legionId;
	private Long lastTime;
	private int legionLastAttack;
	private int legionerLastAttack;
	private int win;
	private Long resource;
	private List<Map<String,String>> listGuard;

	public List<Map<String, String>> getListGuard() {
		return listGuard;
	}

	public void setListGuard(List<Map<String, String>> listGuard) {
		this.listGuard = listGuard;
	}

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

	public Long getLastTime() {
		return lastTime;
	}

	public void setLastTime(Long lastTime) {
		this.lastTime = lastTime;
	}

	public Long getLegionFightId() {
		return legionFightId;
	}

	public void setLegionFightId(Long legionFightId) {
		this.legionFightId = legionFightId;
	}

	public int getLegionLastAttack() {
		return legionLastAttack;
	}

	public void setLegionLastAttack(int legionLastAttack) {
		this.legionLastAttack = legionLastAttack;
	}

	public int getLegionerLastAttack() {
		return legionerLastAttack;
	}

	public void setLegionerLastAttack(int legionerLastAttack) {
		this.legionerLastAttack = legionerLastAttack;
	}

	public int getWin() {
		return win;
	}

	public void setWin(int win) {
		this.win = win;
	}

	public Long getResource() {
		return resource;
	}

	public void setResource(Long resource) {
		this.resource = resource;
	}
}
