package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.CardFragment;


public interface CardFragmentManager {
	/**
	 * 获取所有的卡牌碎片
	 * @return
	 */
	public List<CardFragment> getCardFragment();
	/**
	 * 获取指定卡牌的碎片
	 * @param cardId
	 * @return
	 */
	public CardFragment getCardFragmentByCardId(int cardId);
	/**
	 * 获取指定类型的碎片
	 * @param type
	 * @return
	 */
	public List<CardFragment> getCardFragmentByType(int type);
	/**
	 * 获取碎片可以合成的卡牌
	 * @param fragmentId
	 * @return
	 */
	public CardFragment getCardFragmentByfragmentId(int fragmentId);
	
}
