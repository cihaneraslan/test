package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.FightCard;
import com.cgiser.moka.model.UserSoul;

public interface UserSoulManager {
	/**
	 * 获取用户拥有的战魂武魂
	 * @param roleId
	 * @return
	 */
	public List<UserSoul> getUserSoulByRoleId(Long roleId);
	/**
	 * 获取用户战魂武魂
	 * @param userSoulId
	 * @return
	 */
	public UserSoul getUserSoulById(Long userSoulId);
	/**
	 * 对卡牌进行加成
	 * @param userSoul
	 * @param fightCard
	 */
	public void handleFightCard(UserSoul userSoul,FightCard fightCard);
	/**
	 * 给用户增加一个武器
	 * @param roleId
	 * @param soulId
	 * @param level
	 * @return
	 */
	public Long saveUserSoul(Long roleId,int soulId,int level);
	/**
	 * 删除用户武器
	 * @param userSoulId
	 * @return
	 */
	public int deleteUserSoul(Long userSoulId);
	/**
	 * 卖掉用户武器
	 * @param roleId
	 * @param userSoulIds
	 * @return
	 */
	public int sellSoul(Long roleId,String userSoulIds);
}
