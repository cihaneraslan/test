package com.cgiser.moka;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.cgiser.moka.dao.UserCardDao;
import com.cgiser.moka.dao.UserRuneDao;
import com.cgiser.moka.manager.CardManager;
import com.cgiser.moka.manager.RuneManager;
import com.cgiser.moka.manager.UserCardManager;
import com.cgiser.moka.manager.UserRuneManager;
import com.cgiser.moka.model.Card;
import com.cgiser.moka.model.Rune;
import com.cgiser.moka.model.UserCard;
import com.cgiser.moka.model.UserRune;

@ContextConfiguration(locations = {"classpath:/bean/moka-applicationContext.xml" })
public class UserCardTest extends AbstractJUnit4SpringContextTests {
	@Autowired
	private UserCardManager userCardManager;
	@Autowired
	private CardManager cardManager;
	@Autowired
	private UserCardDao userCardDao;
	@Autowired
	private UserRuneManager userRuneManager;
	@Autowired 
	private RuneManager runeManager;
	@Autowired
	private UserRuneDao userRuneDao;
	
	@Test
	public void testUserCard() {
		List<UserCard> userCards = userCardManager.getUserCard(1000000176L);
		List<Card> cards = cardManager.getAllCard();
		for(Card card:cards){
			Long userCardId = userCardManager.saveUserCard(card.getCardId(), 1000000176L);
			UserCard userCard = userCardManager.getUserCardById(userCardId);
			
			userCardDao.updateUserCard(userCardId, 10, card.getExp(10));
		}
		System.out.println("");
	}
	@Test
	public void testUserRune() {
//		List<UserRune> userRunes = userRuneManager.getUserRune(1000000174L);
//		List<Rune> runes = runeManager.getRunes();
//		for(Rune rune:runes){
//			Long userRuneId = userRuneManager.saveUserRune(rune.getRuneId(), 1000000174L);
//			UserRune userRune = userRuneManager.getUserRuneById(userRuneId);
//			
//			userRuneDao.updateUserRune(userRuneId, 4, rune.getExp(4));
//		}
//		System.out.println("");
	}
}
