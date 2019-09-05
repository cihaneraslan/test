package com.cgiser.moka.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.ActivitiesDao;
import com.cgiser.moka.manager.ActivitiesManager;
import com.cgiser.moka.model.Activities;

public class ActivitiesManagerImpl implements ActivitiesManager {
	ActivitiesDao activitiesDao;
	@Override
	public List<Activities> getAllActivities() {
		// TODO Auto-generated method stub
		return MapListToActivitiesList(activitiesDao.getAllActivities());
	}
	
	private List<Activities> MapListToActivitiesList(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<Activities> activitiesList = new ArrayList<Activities>();
		for(int i=0;i<list.size();i++){
			activitiesList.add(MapToActivities(list.get(i)));
		}
		return activitiesList;
	}

	private Activities MapToActivities(Map<String, Object> map) {
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		Activities activities = new Activities();
		activities.setId(Integer.parseInt(map.get("ID").toString()));
		activities.setImgUrl(map.get("IMGURL").toString());
		activities.setHtmlUrl(map.get("HTMLURL").toString());
		activities.setStart(new Date(((Timestamp)map.get("STARTDATE")).getTime()));
		activities.setEnd(new Date(((Timestamp)map.get("ENDDATE")).getTime()));
		activities.setState(Integer.parseInt(map.get("STATE").toString()));
		return activities;
	}

	public ActivitiesDao getActivitiesDao() {
		return activitiesDao;
	}

	public void setActivitiesDao(ActivitiesDao activitiesDao) {
		this.activitiesDao = activitiesDao;
	}

}
