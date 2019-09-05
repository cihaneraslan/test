package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.StrengResult;
import com.cgiser.moka.model.UserRune;

public interface UserRuneManager {
	/**
	 * 获取用户星辰
	 * @param roleId
	 * @return
	 */
	public List<UserRune> getUserRune(Long roleId);
	/**
	 * 获取用户拥有过的和现有的星辰
	 * @param roleId
	 * @return
	 */
	public List<UserRune> getUserOwnRune(Long roleId);
	/**
	 * 增加用户星辰
	 * @param runeId
	 * @param roleId
	 * @return
	 */
	public Long saveUserRune(int runeId,Long roleId);
	/**
	 * 获取用户星辰
	 * @param roleId
	 * @param userRuneId
	 * @return
	 */
	public UserRune getUserRuneById(Long userRuneId);
	/**
	 * 获取用户卡组星辰
	 * @param roleId
	 * @param groupId
	 * @return
	 */
	public List<UserRune> getUserGroupRunes(Long roleId,Long groupId);
	/**
	 * 删除用户卡牌
	 * @param userRuneId
	 * @return
	 */
	public int delUserRuneById(Long userRuneId);
	/**
	 * 强化星辰
	 * @param userRuneId
	 * @param userRuneIds
	 * @return
	 */
	public StrengResult StrengRune(int userRuneId,String userRuneIds);
	/**
	 * 强化预览
	 * @param userRuneId
	 * @param userRuneIds
	 * @return
	 */
	public StrengResult StrengRunePreView(int userRuneId,String userRuneIds);
	/**
	 * 卖掉星辰
	 * @param roleId
	 * @param userRuneIds
	 * @return
	 */
	public int SellRune(Long roleId,String userRuneIds);
}
