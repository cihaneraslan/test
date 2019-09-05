package com.cgiser.moka.manager;

import java.util.List;

public interface UserCardFragmentManager {
	/**
	 * 获取角色拥有的碎片
	 * @param roleId
	 * @return
	 */
	public List<UserCardFragment> getUserFragmentByRoleId(Long roleId);
	/**
	 * 兑换指定的卡牌
	 * @param roleId
	 * @param cardId
	 * @param useUniversal 是否优先使用万能碎片
	 * @param num 使用几张万能碎片
	 * @return
	 */
	public int exchangeCard(Long roleId,int cardId,int useUniversal,int num);
	/**
	 * 分解指定的卡牌
	 * @param roleId
	 * @param userCardId
	 * @return
	 */
	public List<UserCardFragment> resolveCard(Long roleId,Long userCardId);
	/**
	 * 给用户增加碎片
	 * @param roleId
	 * @param fragmentId
	 * @param num
	 * @return
	 */
	public Long addUserFragment(Long roleId,int fragmentId,int num);
	/**
	 * 减少用户碎片
	 * @param roleId
	 * @param fragmentId
	 * @param num
	 * @return
	 */
	public int delUserFragment(Long roleId,int fragmentId,int num);
	/**
	 * 获取用户拥有此碎片的数量
	 * @param roleId
	 * @param fragmentId
	 * @return
	 */
	public int getUserFragmentNum(Long roleId,int fragmentId);
	/**
	 * 获取用户拥有此类型碎片的数量
	 * @param roleId
	 * @param type
	 * @return
	 */
	public int getUserFragmentNumByType(Long roleId,int type);
	/**
	 * 获取用户的指定碎片
	 * @param roleId
	 * @param fragmentId
	 * @return
	 */
	public UserCardFragment getUserFragment(Long roleId,int fragmentId);
	/**
	 * 获取用户一个类型的碎片信息
	 * @param roleId
	 * @param type
	 * @return
	 */
	public List<UserCardFragment> getUserFragmentByType(Long roleId,int type);
}
