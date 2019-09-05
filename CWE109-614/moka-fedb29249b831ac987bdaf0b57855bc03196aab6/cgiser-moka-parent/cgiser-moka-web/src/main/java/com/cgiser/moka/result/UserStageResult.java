package com.cgiser.moka.result;

import java.util.List;

import com.cgiser.moka.model.UserStage;

public class UserStageResult {
	private List<UserStage> userStages;
	private Long MapCoinsTime;
	public List<UserStage> getUserStages() {
		return userStages;
	}
	public void setUserStages(List<UserStage> userStages) {
		this.userStages = userStages;
	}
	public Long getMapCoinsTime() {
		return MapCoinsTime;
	}
	public void setMapCoinsTime(Long mapCoinsTime) {
		MapCoinsTime = mapCoinsTime;
	}
}
