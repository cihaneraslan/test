package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.Card;

public interface CardManager {
	/**
	 * 获取所有卡牌
	 * @return
	 */
	public List<Card> getAllCard();
	/**
	 * 获取改过名字的卡牌
	 * @return
	 */
	public List<Card> getAllMMCard();
	/**
	 * 随机一张卡牌
	 * @param star
	 * @return
	 */
	public Card randomCard(int star);
	/**
	 * 随机一张不等于指定卡牌的卡牌
	 * @param star
	 * @return
	 */
	public Card randomCard(int star,int cardId);
	/**
	 * 随机多张不等于指定卡牌的卡牌
	 * @param star
	 * @return
	 */
	public List<Card> randomCard(int star,int cardId,int num);
	
	/**
	 * 根据ID获取卡牌
	 * @param cardId
	 * @return
	 */
	public Card getCardById(Long cardId);
	/**
	 * 获取拥有这个技能的卡牌
	 * @param skillName
	 * @return
	 */
	public List<Card> getCardBySkill(String skillName);
}
