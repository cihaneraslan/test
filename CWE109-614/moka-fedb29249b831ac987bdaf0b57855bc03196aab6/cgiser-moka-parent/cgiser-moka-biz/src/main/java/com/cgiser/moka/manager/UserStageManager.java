package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.UserStage;

public interface UserStageManager {
	/**
	 * 获取用户关卡数据
	 * @return
	 */
	public List<UserStage> getUserStageByRoleId(Long roleId);
	/**
	 * 获取用户关卡数据
	 * @param roleId
	 * @param stageId
	 * @return
	 */
	public UserStage getUserStageByRoleIdStageId(Long roleId,int stageId);
	/**
	 * 获取用户关卡数据
	 * @param roleId
	 * @param mapId
	 * @return
	 */
	public List<UserStage>  getUserStageByRoleIdMapId(Long roleId,int mapId);
	/**
	 * 保存用户关卡数据
	 * @param userStage
	 * @return
	 */
	public Long saveUserStage(UserStage userStage);
	/**
	 * 更新用户关卡数据
	 * @param finishedstage
	 * @param userstageid
	 * @return
	 */
	public int updateUserStage(int finishedstage,Long userstageid);
	/**
	 * 获取玩家当前获得的星星数
	 * @param roleId
	 * @return
	 */
	public int getStarNum(Long roleId);
	/**
	 * 获取玩家当前地图获得的星星数
	 * @param roleId
	 * @return
	 */
	public int getStarNum(Long roleId,int mapId);
	/**
	 * 获取当前玩家的收益
	 */
	public int getRoleMapCoins(Long roleId);
}
