package com.cgiser.moka.manager;

import com.cgiser.moka.model.MatchRoleCache;
import com.cgiser.moka.model.Role;


public interface MatchGameManager {
	/**
	 * 搜索匹配玩家，搜索到直接加入游戏
	 * @return
	 */
	public String findMatchPlayer(Role role,int type);
	/**
	 * 搜索到玩家加入房间，开始游戏
	 * @return
	 */
	public int joinInRoom();
	/**
	 * 搜索不到玩家，创建游戏，创建房间，等待其他玩家进入
	 */
	public int createRoom(Role role);
	/**
	 * 退出房间
	 * @param role
	 * @return
	 */
	public int outRoom(Role role);
	/**
	 * 开启匹配战
	 * @param role
	 * @return
	 */
	public void startMatchGame();
	/**
	 * 关闭匹配战
	 * @param role
	 * @return
	 */
	public void stopMatchGame();
	/**
	 * 更新角色的次数
	 * @param roleId
	 * @return
	 */
	public int updateRoleMatchCD(Long roleId);
	/**
	 * 更新角色的次数和最后匹配时间
	 * @param roleId
	 * @return
	 */
	public int updateRoleMatchTimeAndCD(Long roleId);
	/**
	 * 用于每半个小时更新玩家的匹配次数
	 * @return
	 */
	public int addAllRoleMatchCD();
	/**
	 * 更新玩家的匹配次数
	 * @return
	 */
	public int addRoleMatchCD(Long roleId);
	/**
	 * 获取匹配玩家信息
	 * @param roleId
	 * @return
	 */
	public MatchRoleCache getMacthRole(Long roleId);
	
}
