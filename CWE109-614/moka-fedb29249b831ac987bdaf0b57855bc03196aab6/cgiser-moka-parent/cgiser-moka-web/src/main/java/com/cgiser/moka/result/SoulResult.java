package com.cgiser.moka.result;

import java.util.List;

import com.cgiser.moka.model.Soul;

public class SoulResult {
	private Long soulRefreshTime;
	private List<Soul> souls;
	private int refreshCash;
	public Long getSoulRefreshTime() {
		return soulRefreshTime;
	}
	public void setSoulRefreshTime(Long soulRefreshTime) {
		this.soulRefreshTime = soulRefreshTime;
	}
	public List<Soul> getSouls() {
		return souls;
	}
	public void setSouls(List<Soul> souls) {
		this.souls = souls;
	}
	public int getRefreshCash() {
		return refreshCash;
	}
	public void setRefreshCash(int refreshCash) {
		this.refreshCash = refreshCash;
	}
}
