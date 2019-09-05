package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.MMap;
import com.cgiser.moka.model.RoomInfo;

public interface MapManager {
	/**
	 * 获取所有的地图信息
	 * @return
	 */
	public List<MMap> getAllMap();
	/**
	 * 获取该地图的组队战房间信息
	 * @return
	 */
	public RoomInfo getMapRoomInfo(int mapId);
	/**
	 * 获取所有的组队战房间信息
	 * @return
	 */
	public List<RoomInfo> getAllMapRoomInfo();
	/**
	 * 更新4星碎片得到的次数
	 * @param mapId
	 * @return
	 */
	public int updateStar4Day(int mapId);
	/**
	 * 更新5星碎片的次数
	 * @param mapId
	 * @return
	 */
	public int updateStar5Day(int mapId);
	
	public int updateUniversalDay(int mapId);
	public int addFightTimes(int mapId);
	public int addDayFightTimes(int mapId);
	
	public int resetStarDay();
}
