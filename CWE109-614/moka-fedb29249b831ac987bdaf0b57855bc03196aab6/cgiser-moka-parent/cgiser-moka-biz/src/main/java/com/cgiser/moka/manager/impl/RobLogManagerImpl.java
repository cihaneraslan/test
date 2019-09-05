package com.cgiser.moka.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.RobLogDao;
import com.cgiser.moka.manager.RobLogManager;
import com.cgiser.moka.model.RobLog;

public class RobLogManagerImpl implements RobLogManager {
	private RobLogDao robLogDao;
	@Override
	public Long infoRobLog(Long roleId, Long robRoleId, int robRoleCoins) {
		// TODO Auto-generated method stub
		return robLogDao.infoRobLog(roleId, robRoleId, robRoleCoins);
	}
	@Override
	public List<RobLog> getLastRobRoleLog(Long roleId,Date date) {
		// TODO Auto-generated method stub
		return MapListToRobLogList(robLogDao.getLastRobLog(roleId,date));
	}
	private List<RobLog> MapListToRobLogList(List<Map<String, Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<RobLog> robLogs = new ArrayList<RobLog>();
		for(Map map:list){
			robLogs.add(MapToRobLog(map));
		}
		return robLogs;
	}
	private RobLog MapToRobLog(Map<String, Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		RobLog robLog = new RobLog();
		robLog.setRobId(new Long((String)map.get("ROBID")));
		robLog.setRobRoleCoins(Integer.parseInt((String)map.get("ROBROLECOINS")));
		robLog.setRobRoleId(new Long((String)map.get("ROBROLEID")));
		robLog.setRobTime(new Date(((Timestamp)map.get("ROBTIME")).getTime()));
		robLog.setRoleId(new Long((String)map.get("ROLEID")));
		robLog.setState(Integer.parseInt((String)map.get("STATE")));
		return robLog;
	}
	public RobLogDao getRobLogDao() {
		return robLogDao;
	}
	public void setRobLogDao(RobLogDao robLogDao) {
		this.robLogDao = robLogDao;
	}


}
