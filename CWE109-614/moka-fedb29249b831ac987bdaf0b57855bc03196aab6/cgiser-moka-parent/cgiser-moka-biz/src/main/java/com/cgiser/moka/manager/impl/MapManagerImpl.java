package com.cgiser.moka.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.MapDao;
import com.cgiser.moka.manager.MapManager;
import com.cgiser.moka.manager.StageManager;
import com.cgiser.moka.model.MMap;
import com.cgiser.moka.model.RoomInfo;
import com.cgiser.moka.model.Stage;
import com.cgiser.moka.model.VirtualRole;

public class MapManagerImpl implements MapManager {
	private MapDao mapDao;
	private StageManager stageManager;
	@Override
	public List<MMap> getAllMap() {
		List<Map<String,Object>> list = mapDao.getMaps();
		return MapListToMapStageList(list);
	}
	@Override
	public RoomInfo getMapRoomInfo(int mapId) {
		// TODO Auto-generated method stub
		return MapToRoomInfo(mapDao.getMapRoomInfo(mapId));
	}
	@Override
	public List<RoomInfo> getAllMapRoomInfo() {
		// TODO Auto-generated method stub
		return MapListToRoomInfoList(mapDao.getAllMapRoomInfo());
	}
	private List<RoomInfo> MapListToRoomInfoList(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<RoomInfo> roomInfos = new ArrayList<RoomInfo>();
		for(Map map:list){
			roomInfos.add(MapToRoomInfo(map));
		}
		return roomInfos;
	}
	private RoomInfo MapToRoomInfo(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		RoomInfo room = new RoomInfo();
		room.setId(new Long((String)map.get("ID")));
		room.setMapId(Integer.parseInt((String)map.get("MAPID")));
		room.setRoleNumMax(Integer.parseInt((String)map.get("ROLENUMMAX")));
		room.setRoleNumMin(Integer.parseInt((String)map.get("ROLENUMMIN")));
		room.setState(Integer.parseInt((String)map.get("STATE")));
		Long roleId1 = new Long((String)map.get("VIRTUALROLEID1"));
		if(roleId1>0){
			room.setVirtualRole1(MapToVirtualRole(mapDao.getVirtualRoleById(roleId1)));
		}
		roleId1 = new Long((String)map.get("VIRTUALROLEID2"));
		if(roleId1>0){
			room.setVirtualRole2(MapToVirtualRole(mapDao.getVirtualRoleById(roleId1)));
		}
		roleId1 = new Long((String)map.get("VIRTUALROLEID3"));
		if(roleId1>0){
			room.setVirtualRole3(MapToVirtualRole(mapDao.getVirtualRoleById(roleId1)));
		}
		roleId1 = new Long((String)map.get("VIRTUALROLEID4"));
		if(roleId1>0){
			room.setVirtualRole4(MapToVirtualRole(mapDao.getVirtualRoleById(roleId1)));
		}
		roleId1 = new Long((String)map.get("VIRTUALROLEID5"));
		if(roleId1>0){
			room.setVirtualRole5(MapToVirtualRole(mapDao.getVirtualRoleById(roleId1)));
		}
		roleId1 = new Long((String)map.get("VIRTUALROLEID6"));
		if(roleId1>0){
			room.setVirtualRole6(MapToVirtualRole(mapDao.getVirtualRoleById(roleId1)));
		}
		room.setEnergy(Integer.parseInt((String)map.get("ENERGY")));
		room.setStar3((String)map.get("STAR3"));
		room.setStar4((String)map.get("STAR4"));
		room.setStar5((String)map.get("STAR5"));
		room.setWinExp(Integer.parseInt((String)map.get("WINEXP")));
		room.setWinCoins(Integer.parseInt((String)map.get("WINCOINS")));
		room.setLoseExp(Integer.parseInt((String)map.get("LOSEEXP")));
		room.setLoseCoins(Integer.parseInt((String)map.get("LOSECOINS")));
		room.setWinCard(new Double((String)map.get("WINCARDS")));
		room.setStar4Day(Integer.parseInt((String)map.get("STAR4DAY")));
		room.setStar4Times(Integer.parseInt((String)map.get("STAR4TIMES")));
		room.setStar5Day(Integer.parseInt((String)map.get("STAR5DAY")));
		room.setStar5Times(Integer.parseInt((String)map.get("STAR5TIMES")));
		room.setUniversialDay(Integer.parseInt((String)map.get("UNIVERSALDAY")));
		room.setUniversialTimes(Integer.parseInt((String)map.get("UNIVERSALTIMES")));
		room.setFightTimes(Integer.parseInt((String)map.get("FIGHTTIMES")));
		room.setFightDayTimes(Integer.parseInt((String)map.get("FIGHTDAYTIMES")));
		return room;
	}
	private List<MMap> MapListToMapStageList(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<MMap> listMapStage = new ArrayList<MMap>();
		for(int i=0;i<list.size();i++){
			listMapStage.add(MapToMapStage(list.get(i)));
		}
		return listMapStage;
	}
	private MMap MapToMapStage(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		MMap mapStage = new MMap();
		mapStage.setMapId(Integer.parseInt(map.get("MAPID").toString()));
		mapStage.setCount(Integer.parseInt(map.get("COUNT").toString()));
		mapStage.setEveryDayReward(Integer.parseInt(map.get("EVERYDAYREWARD").toString()));
		mapStage.setMazeCount(Integer.parseInt(map.get("MAZECOUNT").toString()));
		mapStage.setName(map.get("NAME").toString());
		mapStage.setNeedStar(Integer.parseInt(map.get("NEEDSTAR").toString()));
		mapStage.setNext(Integer.parseInt(map.get("NEXT").toString()));
		mapStage.setPrev(Integer.parseInt(map.get("PREV").toString()));
		mapStage.setX(new Double(map.get("X").toString()));
		mapStage.setY(new Double(map.get("Y").toString()));
//		mapStage.setRank(Integer.parseInt(map.get("RANK").toString()));
		List<Stage> stages =  stageManager.getStagesByMapId(mapStage.getMapId());
		mapStage.setStages(stages);
		return mapStage;
	}
	private VirtualRole MapToVirtualRole(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		VirtualRole role = new VirtualRole();
		role.setAvatar(Integer.parseInt((String)map.get("AVATAR")));
		role.setCards((String)map.get("CARDS"));
		role.setLevel(Integer.parseInt((String)map.get("LEVEL")));
		role.setRoleId(new Long((String)map.get("ROLEID")));
		role.setRunes((String)map.get("RUNES"));
		role.setSouls((String)map.get("SOULS"));
		role.setState(Integer.parseInt((String)map.get("STATE")));
		return role;
	}
	public MapDao getMapDao() {
		return mapDao;
	}
	public void setMapDao(MapDao mapDao) {
		this.mapDao = mapDao;
	}
	public StageManager getStageManager() {
		return stageManager;
	}
	public void setStageManager(StageManager stageManager) {
		this.stageManager = stageManager;
	}
	@Override
	public int updateStar4Day(int mapId) {
		// TODO Auto-generated method stub
		return mapDao.updateStar4Day(mapId);
	}
	@Override
	public int updateStar5Day(int mapId) {
		// TODO Auto-generated method stub
		return mapDao.updateStar5Day(mapId);
	}
	@Override
	public int updateUniversalDay(int mapId) {
		// TODO Auto-generated method stub
		return mapDao.updateUniversalDay(mapId);
	}
	@Override
	public int addDayFightTimes(int mapId) {
		// TODO Auto-generated method stub
		return mapDao.addFightDayTimes(mapId);
	}
	@Override
	public int addFightTimes(int mapId) {
		// TODO Auto-generated method stub
		return mapDao.addFightTimes(mapId);
	}
	@Override
	public int resetStarDay() {
		// TODO Auto-generated method stub
		return mapDao.resetStarDay();
	}

}
