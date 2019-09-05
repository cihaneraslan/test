package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.UserChip;

public interface ChipManager {
	/**
	 * 获取用户拥有的碎片
	 * @param roleId
	 * @return
	 */
	public List<UserChip> getUserChip(Long roleId);
	/**
	 * 给用户增加碎片
	 * @param roleId
	 * @param type
	 * @param num
	 * @return
	 */
	public int addChip(Long roleId,int type,int num);
	/**
	 * 兑换神龙券
	 * @param roleId
	 * @return
	 */
	public int exChange(Long roleId);
}
