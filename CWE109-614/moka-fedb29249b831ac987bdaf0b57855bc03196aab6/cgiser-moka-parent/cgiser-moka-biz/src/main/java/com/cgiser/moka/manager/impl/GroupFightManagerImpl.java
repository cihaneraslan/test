package com.cgiser.moka.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.GroupFightDao;
import com.cgiser.moka.manager.GroupFightManager;
import com.cgiser.moka.model.GroupFight;

public class GroupFightManagerImpl implements GroupFightManager {
	private GroupFightDao groupFightDao;
	@Override
	public Long save(GroupFight groupFight) {
		// TODO Auto-generated method stub
		return groupFightDao.saveGroupFight(
				groupFight.getGroupFightId(), 
				groupFight.getTurn(),
				groupFight.getTurns(),
				groupFight.getTurnInfo1(), 
				groupFight.getTurnInfo2(),
				groupFight.getTurnInfo3(), 
				groupFight.getTurnInfo4(), 
				groupFight.getTurnInfo5(), 
				groupFight.getTurnInfo6(), 
				groupFight.getRoomId(), groupFight.getRoleId(), 1);
	}
	public GroupFightDao getGroupFightDao() {
		return groupFightDao;
	}
	public void setGroupFightDao(GroupFightDao groupFightDao) {
		this.groupFightDao = groupFightDao;
	}
	private GroupFight MapToGroupFight(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		GroupFight groupFight = new GroupFight();
		groupFight.setGroupFightId((String)map.get("GROUPFIGHTID"));
		groupFight.setId(new Long((String)map.get("ID")));
		groupFight.setRoleId(new Long((String)map.get("ROLEID")));
		groupFight.setRoomId(new Long((String)map.get("ROOMID")));
		groupFight.setState(Integer.parseInt((String)map.get("STATE")));
		groupFight.setTurnInfo1((String)map.get("TURNINFO1"));
		groupFight.setTurnInfo2((String)map.get("TURNINFO2"));
		groupFight.setTurnInfo3((String)map.get("TURNINFO3"));
		groupFight.setTurnInfo4((String)map.get("TURNINFO4"));
		groupFight.setTurnInfo5((String)map.get("TURNINFO5"));
		groupFight.setTurnInfo6((String)map.get("TURNINFO6"));
		groupFight.setTurns(Integer.parseInt((String)map.get("TURNS")));
		groupFight.setTurn(Integer.parseInt((String)map.get("TURN")));
		return groupFight;
	}
	private List<GroupFight> MapListToGroupFightList(List<Map<String, Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<GroupFight> groupFights = new ArrayList<GroupFight>();
		for(Map map:list){
			groupFights.add(MapToGroupFight(map));
		}
		return groupFights;
	}
	@Override
	public List<GroupFight> getGroupFight(Long roomId) {
		// TODO Auto-generated method stub
		return MapListToGroupFightList(groupFightDao.getGroupFightById(roomId));
	}
	@Override
	public GroupFight getGroupFightByRoomIdTurn(Long roomId,int turn) {
		// TODO Auto-generated method stub
		return MapToGroupFight(groupFightDao.getGroupFightByRoomIdTurn(roomId, turn));
	}

}
