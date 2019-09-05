package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.UserCardGroup;

public interface UserCardGroupManager {
	/**
	 * 获取用户卡组
	 * @param roleId
	 * @return
	 */
	public List<UserCardGroup> getUserCardGroup(Long roleId);
	/**
	 * 保存用户卡组
	 * @param groupId
	 * @param cardIds
	 * @param runeIds
	 * @return
	 */
	public int saveUserCardGroup(Long groupId,String cardIds,String runeIds);
	/**
	 * 创建用户卡组
	 * @param roleId
	 * @param cardIds
	 * @param runeIds
	 * @return
	 */
	public Long createUserCardGroup(Long roleId,String groupName,String cardIds,String runeIds);
	/***
	 * 获取指定卡组
	 * @param groupId
	 * @return
	 */
	public UserCardGroup getUserCardGroupById(Long groupId);
	/**
	 * 修改指定卡组名称
	 * @param groupId
	 * @param groupName
	 * @return
	 */
	public int updateUserCardGroupName(Long groupId,String groupName);
}
