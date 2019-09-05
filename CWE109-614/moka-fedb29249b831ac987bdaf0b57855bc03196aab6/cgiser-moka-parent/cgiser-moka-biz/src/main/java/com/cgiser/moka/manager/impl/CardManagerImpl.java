package com.cgiser.moka.manager.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.CardDao;
import com.cgiser.moka.manager.CardManager;
import com.cgiser.moka.manager.SkillManager;
import com.cgiser.moka.model.Card;
import com.cgiser.moka.model.Skill;

public class CardManagerImpl implements CardManager {
	private SkillManager skillManager;
	private CardDao cardDao;
	private int star1 = -1;
	private int star2 = -1;
	private int star3 = -1;
	private int star4 = -1;
	private int star5 = -1;
	private Random rnd;
	@Override
	public List<Card> getAllCard() {
		
		return MapListToCardList(cardDao.getCards());
	}
	@Override
	public Card getCardById(Long cardId) {
		return MapToCard(cardDao.getCardById(cardId));
	}
	@Override
	public Card randomCard(int star) {
		if(star1==-1||star2==-1||star3==-1||star4==-1||star5==-1){
			this.initCardCount();
		}
		if(rnd==null){
			rnd = new Random();
		}
		int cardIndex = 0;
		if(star==1){
			cardIndex = rnd.nextInt(star1);
		}
		if(star==2){
			cardIndex = rnd.nextInt(star2);
		}
		if(star==3){
			cardIndex = rnd.nextInt(star3);
		}
		if(star==4){
			cardIndex = rnd.nextInt(star4);
		}
		if(star==5){
			cardIndex = rnd.nextInt(star5);
		}
		
		return MapToCard(cardDao.getCardByCardIndex(star, cardIndex).get(0));
	}
	@Override
	public Card randomCard(int star, int cardId) {
		// TODO Auto-generated method stub
		return this.randomCard(star, cardId,1).get(0);
	}
	@Override
	public List<Card> randomCard(int star, int cardId,int num) {
		if(star1==-1||star2==-1||star3==-1||star4==-1||star5==-1){
			this.initCardCount();
		}
		if(rnd==null){
			rnd = new Random();
		}
		int cardIndex = 0;
		List<Card> cardList = new ArrayList<Card>();
		Card card = null;
		for(int i=0;i<num;i++){
			if(star==1){
				cardIndex = rnd.nextInt(star1-1);
			}
			if(star==2){
				cardIndex = rnd.nextInt(star2-1);
			}
			if(star==3){
				cardIndex = rnd.nextInt(star3-1);
			}
			if(star==4){
				cardIndex = rnd.nextInt(star4-1);
			}
			if(star==5){
				cardIndex = rnd.nextInt(star5-1);
			}
			card = MapToCard(cardDao.getCardByCardIndex(star, cardIndex, cardId).get(0));
			cardList.add(card);
		}
		return cardList;
	}
	private void initCardCount(){
		star1 = cardDao.getCardCountByStar(1);
		star2 = cardDao.getCardCountByStar(2);
		star3 = cardDao.getCardCountByStar(3);
		star4 = cardDao.getCardCountByStar(4);
		star5 = cardDao.getCardCountByStar(5);
	}
	private Card MapToCard(Map<String, Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		Card card = new Card();
		card.setAttack(Integer.parseInt(map.get("ATTACK").toString()));
		card.setBaseExp(Integer.parseInt(map.get("BASEEXP").toString()));
		int attackPro = Integer.parseInt(map.get("ATTACKPRO").toString());
		List<Integer> attackArray = new ArrayList<Integer>();
		for(int i=0;i<11;i++){
			attackArray.add(card.getAttack()+i*attackPro);
		}
		card.setAttackArray(attackArray);
		card.setBoss(Integer.parseInt(map.get("BOSS").toString()));
		card.setBossCounter(Integer.parseInt(map.get("BOSSCOUNTER").toString()));
		card.setCardId(Integer.parseInt(map.get("CARDID").toString()));
		card.setCardName(map.get("CARDNAME").toString());
		card.setColor(Integer.parseInt(map.get("COLOR").toString()));
		card.setCost(Integer.parseInt(map.get("COST").toString()));
		String strExp = map.get("EXPARRAY").toString();
		List<String> listStrExp = Arrays.asList(strExp.split("_"));
		List<Integer> expArray = new ArrayList<Integer>();
		for(int i = 0;i<listStrExp.size();i++){
			expArray.add(Integer.parseInt(listStrExp.get(i)));
		}
		card.setExpArray(expArray);
		card.setFactionCounter(Integer.parseInt(map.get("FACTIONCOUNTER").toString()));
		card.setFightMPacket(Integer.parseInt(map.get("FIGHTMPACKET").toString()));
		card.setFullImageId(Integer.parseInt(map.get("FULLIMAGEID").toString()));
		card.setGlory(Integer.parseInt(map.get("GLORY").toString()));
		int hp = Integer.parseInt(map.get("HP").toString());
		int hpPro = Integer.parseInt(map.get("HPPRO").toString());
		List<Integer> hpArray = new ArrayList<Integer>();
		for(int i=0;i<11;i++){
			hpArray.add(hp+i*hpPro);
		}
		card.setHpArray(hpArray);
		card.setImageId(Integer.parseInt(map.get("IMAGEID").toString()));
		card.setLockSkill1(Integer.parseInt(map.get("LOCKSKILL1").toString()));
		card.setLockSkill2(Integer.parseInt(map.get("LOCKSKILL2").toString()));
		card.setMagicCard(Integer.parseInt(map.get("MAGICCARD").toString()));
		card.setMasterPacket(Integer.parseInt(map.get("MASTERPACKET").toString()));
		card.setMaze(Integer.parseInt(map.get("MAZE").toString()));
		card.setPrice(Integer.parseInt(map.get("PRICE").toString()));
		card.setRace(Integer.parseInt(map.get("RACE").toString()));
		card.setRobber(Integer.parseInt(map.get("ROBBER").toString()));
		card.setSeniorPacket(Integer.parseInt(map.get("SENIORPACKET").toString()));
		card.setSkill(Integer.parseInt(map.get("SKILL").toString()));
		card.setWait(Integer.parseInt(map.get("WAIT").toString()));
		card.setType(Integer.parseInt(map.get("TYPE").toString()));
		return card;
	}
	private List<Card> MapListToCardList(List<Map<String, Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<Card> listCard = new ArrayList<Card>();
		for(int i=0;i<list.size();i++){
			listCard.add(MapToCard(list.get(i)));
		}
		return listCard;
	}

	public CardDao getCardDao() {
		return cardDao;
	}

	public void setCardDao(CardDao cardDao) {
		this.cardDao = cardDao;
	}
	@Override
	public List<Card> getCardBySkill(String skillName) {
		Skill skill = skillManager.getSkillByName(skillName);
		if(skill==null){
			return null;
		}
		return MapListToCardList(cardDao.getCardBySkill(skill.getSkillId()));
	}
	public SkillManager getSkillManager() {
		return skillManager;
	}
	public void setSkillManager(SkillManager skillManager) {
		this.skillManager = skillManager;
	}
	@Override
	public List<Card> getAllMMCard() {
		// TODO Auto-generated method stub
		return MapListToCardList(cardDao.getMMCards());
	}
}
