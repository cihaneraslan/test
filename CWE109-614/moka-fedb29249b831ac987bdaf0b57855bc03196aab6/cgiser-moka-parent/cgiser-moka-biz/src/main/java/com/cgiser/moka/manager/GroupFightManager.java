package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.GroupFight;

public interface GroupFightManager {
	/**
	 * 保存组队战斗过程
	 * @param groupFight
	 * @return
	 */
	public Long save(GroupFight groupFight);
	/**
	 * 获取组队战斗过程
	 * @param roomId
	 * @return
	 */
	public List<GroupFight> getGroupFight(Long roomId);
	/**
	 * 获取某一轮组队战斗过程
	 * @param roomId
	 * @return
	 */
	public GroupFight getGroupFightByRoomIdTurn(Long roomId,int turn);
}
