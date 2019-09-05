package com.cgiser.moka.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.UserStageDao;
import com.cgiser.moka.data.UserStageDo;
import com.cgiser.moka.manager.MapManager;
import com.cgiser.moka.manager.UserStageManager;
import com.cgiser.moka.model.MMap;
import com.cgiser.moka.model.UserStage;

public class UserStageManagerImpl implements UserStageManager {
	private UserStageDao userStageDao;
	private MapManager mapManager;
	@Override
	public List<UserStage> getUserStageByRoleId(Long roleId) {
		return MapListToUserStageList(userStageDao.getUserStagesByRoleId(roleId));
	}
	@Override
	public UserStage getUserStageByRoleIdStageId(Long roleId, int stageId) {
		return MapToUserStage(userStageDao.getUserStagesByRoleIdStageId(roleId, stageId));
	}
	private UserStage MapToUserStage(Map<String, Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		UserStage userStage = new UserStage();
		userStage.setId(new Long(map.get("ID").toString()));
		userStage.setCounterAttackTime(new Long(map.get("COUNTERATTACKTIME").toString()));
		userStage.setFinishedStage(Integer.parseInt(map.get("FINISHEDSTAGE").toString()));
		userStage.setLastFinishedTime(new Date(((Timestamp)(map.get("LASTFINISHEDTIME"))).getTime()));
		userStage.setMapId(Integer.parseInt(map.get("MAPID").toString()));
		userStage.setRoleId(new Long(map.get("ROLEID").toString()));
		userStage.setStageId(Integer.parseInt(map.get("STAGEID").toString()));
		userStage.setType(Integer.parseInt(map.get("TYPE").toString()));
		return userStage;
	}
	private UserStageDo fillDo(UserStage userStage){
		if(userStage!=null){
			UserStageDo userStageDo = new UserStageDo();
			userStageDo.setCounterAttackTime(userStage.getCounterAttackTime());
			userStageDo.setFinishedStage(userStage.getFinishedStage());
			userStageDo.setLastFinishedTime(userStage.getLastFinishedTime());
			userStageDo.setMapId(userStage.getMapId());
			userStageDo.setRoleId(userStage.getRoleId());
			userStageDo.setStageId(userStage.getStageId());
			userStageDo.setType(userStage.getType());
			return userStageDo;
		}
		return null;
	}
	private List<UserStage> MapListToUserStageList(List<Map<String, Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<UserStage> userStages = new ArrayList<UserStage>();
		for(int i=0;i<list.size();i++){
			userStages.add(MapToUserStage(list.get(i)));
		}
		return userStages;
	}
	public UserStageDao getUserStageDao() {
		return userStageDao;
	}
	public void setUserStageDao(UserStageDao userStageDao) {
		this.userStageDao = userStageDao;
	}
	@Override
	public Long saveUserStage(UserStage userStage) {
		// TODO Auto-generated method stub
		return userStageDao.saveUserStage(this.fillDo(userStage));
	}
	@Override
	public int updateUserStage(int finishedstage, Long userstageid) {
		// TODO Auto-generated method stub
		return userStageDao.updateUserStage(finishedstage, userstageid);
	}
	@Override
	public List<UserStage> getUserStageByRoleIdMapId(Long roleId, int mapId) {
		// TODO Auto-generated method stub
		return MapListToUserStageList(userStageDao.getUserStagesByRoleIdMapId(roleId, mapId));
	}
	@Override
	public int getRoleMapCoins(Long roleId) {
		int coins = 0;
		List<MMap> maps = mapManager.getAllMap();
		for(MMap map:maps){
			coins+=map.getEveryDayReward()*this.getStarNum(roleId, map.getMapId());
		}
		return coins;
	}
	@Override
	public int getStarNum(Long roleId) {
		int star = 0;
		List<UserStage> userStages = this.getUserStageByRoleId(roleId);
		
		if(!CollectionUtils.isEmpty(userStages)){
			for(UserStage userStage2:userStages){
				star = star+userStage2.getFinishedStage();
			}
		}
		return star;
	}
	public MapManager getMapManager() {
		return mapManager;
	}
	public void setMapManager(MapManager mapManager) {
		this.mapManager = mapManager;
	}
	@Override
	public int getStarNum(Long roleId, int mapId) {
		// TODO Auto-generated method stub
		int star = 0;
		List<UserStage> userStages = this.getUserStageByRoleIdMapId(roleId, mapId);
		if(!CollectionUtils.isEmpty(userStages)){
			for(UserStage userStage2:userStages){
				star = star+userStage2.getFinishedStage();
			}
		}
		return star;
	}

}
