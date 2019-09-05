package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.StrengResult;
import com.cgiser.moka.model.UserCard;

public interface UserCardManager {
	/**
	 * 获取用户卡牌
	 * @param roleId
	 * @return
	 */
	public List<UserCard> getUserCard(Long roleId);
	/**
	 * 获取用户曾经拥有过 和现在拥有的卡牌
	 * @param roleId
	 * @return
	 */
	public List<UserCard> getUserOwnCard(Long roleId);
	/**
	 * 增加用户卡牌
	 * @param CardId
	 * @param roleId
	 * @return
	 */
	public Long saveUserCard(int CardId,Long roleId);
	/**
	 * 获取用户卡牌
	 * @param roleId
	 * @param userCardId
	 * @return
	 */
	public UserCard getUserCardById(Long userCardId);
	/**
	 * 删除用户卡牌
	 * @param userCardId
	 * @return
	 */
	public int delUserCardById(Long userCardId);
	/**
	 * 强化卡牌
	 * @param usesCardId
	 * @param userCardIds
	 * @return
	 */
	public StrengResult StrengCard(Long usesCardId,String userCardIds);
	/**
	 * 强化预览
	 * @param usesCardId
	 * @param userCardIds
	 * @return
	 */
	public StrengResult StrengCardPreView(Long usesCardId,String userCardIds);
	/**
	 * 卖掉卡牌
	 * @param roleId
	 * @param userCardIds
	 * @return
	 */
	public int SellCard(Long roleId,String userCardIds);
	/**
	 * 修改用户卡牌装备的武器
	 * @param userCardId
	 * @param userSoulId
	 * @return
	 */
	public int ResetUserCardSoul(Long userCardId,Long userSoulId);
	/**
	 * 获取已装备武器的所有卡牌
	 * @param roleId
	 * @return
	 */
	public List<UserCard> GetUserCardBySoul(Long roleId);
	/**
	 * 获取指定的武器所装备的卡牌
	 * @param roleId
	 * @return
	 */
	public UserCard GetUserCardByUserSoulId(Long roleId,Long userSoulId);
}
