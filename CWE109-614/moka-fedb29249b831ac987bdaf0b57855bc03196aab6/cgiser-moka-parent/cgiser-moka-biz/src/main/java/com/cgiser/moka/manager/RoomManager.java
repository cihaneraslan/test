package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.MapRoom;

public interface RoomManager {
	/**
	 * 创建房间
	 * @param roleId
	 * @param mapId
	 * @return
	 */
	public MapRoom createRoom(Long roleId,int mapId);
	/**
	 * 获取当前组队页面的房间
	 * @param mapId
	 * @return
	 */
	public List<MapRoom> getMapRoom(int mapId);
	/**
	 * 进入房间
	 * @param roleId
	 * @param roomId
	 * @return
	 */
	public MapRoom joinInRoom(Long roleId,Long roomId);
	/**
	 * 进入组队战的大厅
	 * @param mapId
	 * @return
	 */
	public int joinInHall(int mapId,Long roleId);
	/**
	 * 出组队大厅
	 * @param mapId
	 * @param roleId
	 * @return
	 */
	public int outHall(int mapId,Long roleId);
	/**
	 * 获取当前组队页面的房间
	 * @param roomId
	 * @return
	 */
	public MapRoom getMapRoomByRoomId(Long roomId);
	/**
	 * 获取组队战斗的房间
	 * @param roomId
	 * @return
	 */
	public MapRoom getMapRoomByRoomIdState(Long roomId,int state);
	/**
	 * 获取当前角色是否有房间
	 * @param roomId
	 * @return
	 */
	public MapRoom getMapRoomByRoomOwner(Long roleId);
	/**
	 * 退出房间
	 * @param roleId
	 * @param roomId
	 * @return
	 */
	public int outRoom(Long roleId,Long roomId);
	
	public int deleteRoom(Long roomId);
	
	public void scanAllRoom();
	/**
	 * 修改组队战的战斗结果
	 * @param roomId
	 * @param salaryInfo 奖励信息
	 * @param turnInfo 战斗回合数
	 * @param win
	 * @return
	 */
	public int updateRoomFightResult(Long roomId,String salaryInfo,String turnInfo,int win);
}
