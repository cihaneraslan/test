package com.cgiser.moka.manager.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.RoomDao;
import com.cgiser.moka.manager.MapManager;
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.RoomManager;
import com.cgiser.moka.manager.support.BeanUtils;
import com.cgiser.moka.message.netty.CgiserChannelGroup;
import com.cgiser.moka.model.MapRoom;
import com.cgiser.moka.model.MessageType;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.RoomInfo;

public class RoomManagerImpl implements RoomManager {
	private final ConcurrentMap<String, Set<Long>> halls = new ConcurrentHashMap<String, Set<Long>>();
	private MapManager mapManager;
	private RoomDao roomDao;
	private RoleManager roleManager;
	private MessageManager messageManager;

	@Override
	public MapRoom createRoom(Long roleId, int mapId) {
		RoomInfo info = mapManager.getMapRoomInfo(mapId);
		if (info == null) {
			return null;
		}
		Long roomId = roomDao.saveRoom(mapId, roleId, info.getRoleNumMax());
		if (roomId > 0) {
			Role role;
			if (halls.get(String.valueOf(mapId)) == null) {
				halls.put(String.valueOf(mapId), new HashSet<Long>());
			}
			ChannelBuffer buffer = new DynamicChannelBuffer(200);
			buffer.writeInt(MessageType.GROUPFIGHT.getCode());
			buffer.writeInt(MessageType.ROLECREATEROOM.getCode());
			for (Long roleId1 : halls.get(String.valueOf(mapId))) {
				if (roleId1 > 0) {
					role = roleManager.getRoleById(roleId1);
					if (CgiserChannelGroup.isOnLine(role.getRoleName())) {
						messageManager.sendMessageToRole(role.getRoleName(),
								buffer);
					}
				}

			}
			return this.getMapRoomByRoomId(roomId);
		}
		return null;
	}

	@Override
	public MapRoom getMapRoomByRoomId(Long roomId) {
		// TODO Auto-generated method stub
		return MapToRoom(roomDao.getRoomById(roomId));
	}

	@Override
	public MapRoom getMapRoomByRoomIdState(Long roomId, int state) {
		// TODO Auto-generated method stub
		return MapToRoom(roomDao.getRoomByIdState(roomId, state));
	}

	@Override
	public List<MapRoom> getMapRoom(int mapId) {
		return MapListToRoomList(roomDao.getRoomByMapId(mapId));
	}

	@Override
	public int joinInHall(int mapId, Long roleId) {
		if (halls.get(String.valueOf(mapId)) == null) {
			halls.put(String.valueOf(mapId), new HashSet<Long>());
		}
		halls.get(String.valueOf(mapId)).add(roleId);
		return 1;
	}

	@Override
	public MapRoom joinInRoom(Long roleId, Long roomId) {
		MapRoom room = this.getMapRoomByRoomId(roomId);
		if (room == null) {
			return null;
		}
		int weizhi = 0;
		int flag = 0;
		for (int i = 1; i <= room.getRoleNum(); i++) {
			Long roleId1 = (Long) BeanUtils.getFieldValueByName("role" + i,
					room);
			if (roleId1.equals(roleId)) {
				flag = 1;
				break;
			}
		}
		if (flag == 1) {
			return room;
		}
		for (int i = 1; i <= room.getRoleNum(); i++) {
			Long roleId1 = (Long) BeanUtils.getFieldValueByName("role" + i,
					room);
			if (roleId1 == 0) {
				weizhi = i;
				break;
			}
		}
		if (weizhi == 0) {
			return null;
		}

		if (roomDao.updateRoomRole(roleId, weizhi, roomId) > 0) {
			ChannelBuffer buffer = new DynamicChannelBuffer(200);
			buffer.writeInt(MessageType.GROUPFIGHT.getCode());
			buffer.writeInt(MessageType.ROLEIN.getCode());
			buffer.writeInt(room.getRoomId().intValue());
			Role role;
			if (halls.get(String.valueOf(room.getMapId())) == null) {
				halls.put(String.valueOf(room.getMapId()), new HashSet<Long>());
			}
			for (Long roleId1 : halls.get(String.valueOf(room.getMapId()))) {
				if (roleId1 > 0) {
					role = roleManager.getRoleById(roleId1);
					if (CgiserChannelGroup.isOnLine(role.getRoleName())) {
						messageManager.sendMessageToRole(role.getRoleName(),
								buffer);
					}
				}

			}
			return this.getMapRoomByRoomId(roomId);
		}
		return null;
	}

	@Override
	public int outHall(int mapId, Long roleId) {
		if (halls.get(String.valueOf(mapId)) == null) {
			halls.put(String.valueOf(mapId), new HashSet<Long>());
		}
		halls.get(String.valueOf(mapId)).remove(roleId);
		return 1;
	}

	private MapRoom MapToRoom(Map<String, Object> map) {
		if (CollectionUtils.isEmpty(map)) {
			return null;
		}
		MapRoom mapRoom = new MapRoom();
		mapRoom.setMapId(Integer.parseInt((String) map.get("MAPID")));
		mapRoom.setRole1(new Long((String) map.get("ROLE1")));
		mapRoom.setRole2(new Long((String) map.get("ROLE2")));
		mapRoom.setRole3(new Long((String) map.get("ROLE3")));
		mapRoom.setRole4(new Long((String) map.get("ROLE4")));
		mapRoom.setRole5(new Long((String) map.get("ROLE5")));
		mapRoom.setRole6(new Long((String) map.get("ROLE6")));
		mapRoom.setWin(Integer.parseInt((String) map.get("WIN")));
		mapRoom.setRoleNum(Integer.parseInt((String) map.get("ROLENUM")));
		mapRoom.setRoomId(new Long((String) map.get("ROOMID")));
		mapRoom.setRoomOwner(new Long((String) map.get("ROOMOWNER")));
		mapRoom.setState(Integer.parseInt((String) map.get("STATE")));
		mapRoom.setTurnInfo((String)map.get("TURNINFO"));
		mapRoom.setSalaryInfo((String)map.get("SALARYINFO"));
		return mapRoom;
	}

	private List<MapRoom> MapListToRoomList(List<Map<String, Object>> list) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		List<MapRoom> mapRooms = new ArrayList<MapRoom>();
		for (Map<String, Object> map : list) {
			mapRooms.add(MapToRoom(map));
		}
		return mapRooms;
	}

	public MapManager getMapManager() {
		return mapManager;
	}

	public void setMapManager(MapManager mapManager) {
		this.mapManager = mapManager;
	}

	@Override
	public MapRoom getMapRoomByRoomOwner(Long roleId) {
		// TODO Auto-generated method stub
		return MapToRoom(roomDao.getRoomByOwner(roleId));
	}

	@Override
	public int outRoom(Long roleId, Long roomId) {
		MapRoom room = this.getMapRoomByRoomId(roomId);
		if (room == null) {
			return 1;
		}
		Role role;
		
		if (room.getRoomOwner().equals(roleId)) {
			int result = roomDao.updateRoomRoleState(roomId);
			if(result>0){
				ChannelBuffer buffer = new DynamicChannelBuffer(200);
				buffer.writeInt(MessageType.GROUPFIGHT.getCode());
				buffer.writeInt(MessageType.DISSOLUTION.getCode());
				buffer.writeInt(room.getRoomId().intValue());
				if (halls.get(String.valueOf(room.getMapId())) == null) {
					halls.put(String.valueOf(room.getMapId()),
							new HashSet<Long>());
				}
				for (Long roleId1 : halls.get(String.valueOf(room.getMapId()))) {
					if (roleId1 > 0) {
						role = roleManager.getRoleById(roleId1);
						if (CgiserChannelGroup.isOnLine(role.getRoleName())) {
							messageManager.sendMessageToRole(
									role.getRoleName(), buffer);
						}
					}

				}
				return 1;
			}else{
				return 0;
			}
		}
		int weizhi = 0;
		for (int i = 1; i <= room.getRoleNum(); i++) {
			Long roleId1 = (Long) BeanUtils.getFieldValueByName("role" + i,
					room);
			if (roleId1.equals(roleId)) {
				weizhi = i;
				break;
			}
		}
		if (weizhi > 0) {
			ChannelBuffer buffer = new DynamicChannelBuffer(200);
			buffer.writeInt(MessageType.GROUPFIGHT.getCode());
			buffer.writeInt(MessageType.ROLELEAVE.getCode());
			buffer.writeInt(room.getRoomId().intValue());
			if (halls.get(String.valueOf(room.getMapId())) == null) {
				halls.put(String.valueOf(room.getMapId()),
						new HashSet<Long>());
			}
			for (Long roleId1 : halls.get(String.valueOf(room.getMapId()))) {
				if (roleId1 > 0) {
					role = roleManager.getRoleById(roleId1);
					if (CgiserChannelGroup.isOnLine(role.getRoleName())) {
						messageManager.sendMessageToRole(
								role.getRoleName(), buffer);
					}
				}

			}
			return roomDao.updateRoomRoleToNull(weizhi, roomId);
		}
		return 1;
	}

	public RoomDao getRoomDao() {
		return roomDao;
	}

	public void setRoomDao(RoomDao roomDao) {
		this.roomDao = roomDao;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	@Override
	public void scanAllRoom() {
		List<MapRoom> rooms = MapListToRoomList(roomDao.getAllRoom());
		if (CollectionUtils.isEmpty(rooms)) {
			return;
		}
		Role role;
		// boolean online = false;
		for (MapRoom room : rooms) {
			Long roleId = room.getRoomOwner();
			role = roleManager.getRoleById(roleId);
			Set<Long> roleList = this.halls
					.get(String.valueOf(room.getMapId()));
			if (CollectionUtils.isEmpty(roleList)) {
				this.halls.put(String.valueOf(room.getMapId()),
						new HashSet<Long>());
			}
			if (!CgiserChannelGroup.isOnLine(role.getRoleName())) {
				this.outRoom(roleId, room.getRoomId());
				roleList.remove(roleId);
				continue;
			}

			if (!roleList.contains(roleId)) {
				this.outRoom(roleId, room.getRoomId());
				continue;
			}
			for (int i = 1; i <= room.getRoleNum(); i++) {
				Long roleId1 = (Long) BeanUtils.getFieldValueByName("role" + i,
						room);
				if (roleId1 > 0) {
					role = roleManager.getRoleById(roleId1);
					if (!CgiserChannelGroup.isOnLine(role.getRoleName())) {
						this.outRoom(roleId1, room.getRoomId());
						roleList.remove(roleId1);
					} else {
						if (!roleList.contains(roleId1)) {
							this.outRoom(roleId1, room.getRoomId());
						}
					}

				}
			}
		}
	}

	public MessageManager getMessageManager() {
		return messageManager;
	}

	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}

	@Override
	public int deleteRoom(Long roomId) {
		// TODO Auto-generated method stub
		return roomDao.updateRoomRoleState(roomId);
	}

	@Override
	public int updateRoomFightResult(Long roomId,String salaryInfo,String turnInfo,int win) {
		// TODO Auto-generated method stub
		return roomDao.updateRoomFightResult(roomId,salaryInfo,turnInfo,win);
	}

}
