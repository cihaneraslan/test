package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.Activities;

public interface ActivitiesManager {
	/**
	 *获取所有的已经生效的活动
	 * @return
	 */
	public List<Activities> getAllActivities();
}
