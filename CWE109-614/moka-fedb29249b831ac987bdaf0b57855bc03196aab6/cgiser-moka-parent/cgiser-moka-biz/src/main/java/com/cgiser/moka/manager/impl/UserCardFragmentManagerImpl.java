package com.cgiser.moka.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.UserCardFragmentDao;
import com.cgiser.moka.manager.CardFragmentManager;
import com.cgiser.moka.manager.CardManager;
import com.cgiser.moka.manager.UserCardFragment;
import com.cgiser.moka.manager.UserCardFragmentManager;
import com.cgiser.moka.manager.UserCardManager;
import com.cgiser.moka.model.Card;
import com.cgiser.moka.model.CardFragment;
import com.cgiser.moka.model.UserCard;

public class UserCardFragmentManagerImpl implements UserCardFragmentManager {
	private UserCardFragmentDao userCardFragmentDao;
	private CardFragmentManager cardFragmentManager;
	private UserCardManager userCardManager;
	private CardManager cardManager;
	@Override
	public int exchangeCard(Long roleId, int cardId, int useUniversal, int num) {
		CardFragment cardFragment = cardFragmentManager.getCardFragmentByCardId(cardId);
		if(cardFragment==null){
			return 0;
		}
		UserCardFragment userCardFragment = this.getUserFragment(roleId, cardFragment.getId());
		if(userCardFragment==null){
			return 0;
		}
		if(num>0){
			if(num>this.getUserFragmentNumByType(roleId, 2)){
				return 0;
			}
			if(num>cardFragment.getNum()/2){
				return 0;
			}
		}
		if((num + userCardFragment.getNum())<cardFragment.getNum()){
			return 0;
		}
		//实际消耗的碎片
		int useNum = cardFragment.getNum() - num;
		int result1 = this.delUserFragment(roleId, cardFragment.getId(), useNum);
		if(num>0){
			List<CardFragment> cardFragments = cardFragmentManager.getCardFragmentByType(2);
			int fragmentId = cardFragments.get(0).getId();
			result1 = this.delUserFragment(roleId, fragmentId, num);
		}
		if(result1<1){
			return 0;
		}	
		if(userCardManager.saveUserCard(cardId, roleId)>0){
			return 1;
		}
		return 0;
	}

	@Override
	public List<UserCardFragment> getUserFragmentByRoleId(Long roleId) {
		// TODO Auto-generated method stub
		return MapListToCardFragmentList(userCardFragmentDao.getUserFragmentByRoleId(roleId));
	}

	@Override
	public Long addUserFragment(Long roleId, int fragmentId, int num) {
		// TODO Auto-generated method stub
		return userCardFragmentDao.addUserFragment(roleId, fragmentId, num);
	}

	@Override
	public int delUserFragment(Long roleId, int fragmentId, int num) {
		// TODO Auto-generated method stub
		return userCardFragmentDao.delUserFragment(roleId, fragmentId, num);
	}
	private UserCardFragment MapToUserCardFragment(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		UserCardFragment cardFragment = new UserCardFragment();
		cardFragment.setId(Integer.parseInt((String)map.get("ID")));
		cardFragment.setNum(Integer.parseInt((String)map.get("NUM")));
		cardFragment.setState(Integer.parseInt((String)map.get("STATE")));
		cardFragment.setFragmentId(Integer.parseInt((String)map.get("FRAGMENTID")));
		cardFragment.setRoleId(new Long((String)map.get("ROLEID")));
		return cardFragment;
	}
	
	private List<UserCardFragment> MapListToCardFragmentList(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<UserCardFragment> listFragments = new ArrayList<UserCardFragment>();
		for(int i=0;i<list.size();i++){
			listFragments.add(MapToUserCardFragment(list.get(i)));
		}
		return listFragments;
	}

	public UserCardFragmentDao getUserCardFragmentDao() {
		return userCardFragmentDao;
	}

	public void setUserCardFragmentDao(UserCardFragmentDao userCardFragmentDao) {
		this.userCardFragmentDao = userCardFragmentDao;
	}

	@Override
	public int getUserFragmentNum(Long roleId, int fragmentId) {
		UserCardFragment userCardFragment = this.getUserFragment(roleId, fragmentId);
		if(userCardFragment==null){
			return 0;
		}
		return userCardFragment.getNum();
	}

	@Override
	public int getUserFragmentNumByType(Long roleId, int type) {
		List<UserCardFragment> list = getUserFragmentByType(roleId, type);
		if(CollectionUtils.isEmpty(list)){
			return 0;
		}
		int num = 0;
		for(int i=0;i<list.size();i++){
			num = num + list.get(i).getNum();
		}
		return num;
	}

	@Override
	public UserCardFragment getUserFragment(Long roleId, int fragmentId) {
		// TODO Auto-generated method stub
		return MapToUserCardFragment(userCardFragmentDao.getUserFragmentByRoleIdFragmentId(roleId, fragmentId));
	}

	public CardManager getCardManager() {
		return cardManager;
	}

	public void setCardManager(CardManager cardManager) {
		this.cardManager = cardManager;
	}

	@Override
	public List<UserCardFragment> getUserFragmentByType(Long roleId, int type) {
		List<CardFragment> cardFragments = cardFragmentManager.getCardFragmentByType(type);
		if(CollectionUtils.isEmpty(cardFragments)){
			return null;
		}
		List<UserCardFragment> userCardFragments = new ArrayList<UserCardFragment>();
		for(CardFragment cardFragment:cardFragments){
			userCardFragments.add(MapToUserCardFragment(userCardFragmentDao.getUserFragmentByRoleIdFragmentId(roleId, cardFragment.getId())));
		}
		return userCardFragments;
	}

	public CardFragmentManager getCardFragmentManager() {
		return cardFragmentManager;
	}

	public void setCardFragmentManager(CardFragmentManager cardFragmentManager) {
		this.cardFragmentManager = cardFragmentManager;
	}

	public UserCardManager getUserCardManager() {
		return userCardManager;
	}

	public void setUserCardManager(UserCardManager userCardManager) {
		this.userCardManager = userCardManager;
	}

	@Override
	public List<UserCardFragment> resolveCard(Long roleId, Long userCardId) {
		UserCard userCard = userCardManager.getUserCardById(userCardId);
		if(userCard==null){
			return null;
		}
		Card card = cardManager.getCardById(new Long(userCard.getCardId()));
		if(card==null){
			return null;
		}
		CardFragment cardFragment = cardFragmentManager.getCardFragmentByCardId(card.getCardId());
		if(cardFragment==null){
			return null;
		}
		if(userCardManager.delUserCardById(userCardId)>0){
			List<UserCardFragment> userCardFragments = new ArrayList<UserCardFragment>();
			int num = cardFragment.getResolveNum();
			UserCardFragment userCardFragment = null;
			CardFragment cardFragment1 = null;
			Long id = 0L;
			int resolveType = cardFragment.getResolveType();
			List<Card> cardList = cardManager.randomCard(card.getColor(), card.getCardId(),resolveType);
			Map<Integer,Integer> cardIds = new HashMap<Integer, Integer>();
			for(Card ca:cardList){
				if(cardIds.containsKey(ca.getCardId())){
					cardIds.put(ca.getCardId(), cardIds.get(ca.getCardId())+1) ;
				}else{
					cardIds.put(ca.getCardId(), 1) ;
				}
			}
			for(Map.Entry<Integer, Integer> entry: cardIds.entrySet()) {
				cardIds.put(entry.getKey(), num/cardIds.size());
			}
			for(Map.Entry<Integer, Integer> entry: cardIds.entrySet()) {
				cardFragment1 = cardFragmentManager.getCardFragmentByCardId(entry.getKey());
				id = this.addUserFragment(roleId, cardFragment1.getId(), entry.getValue());
				userCardFragment = new UserCardFragment();
				userCardFragment.setFragmentId(cardFragment1.getId());
				userCardFragment.setId(id.intValue());
				userCardFragment.setNum(entry.getValue());
				userCardFragment.setRoleId(roleId);
				userCardFragment.setState(1);
				userCardFragments.add(userCardFragment);
			}
			int universalNum = cardFragment.getUniversal();
			
			if(universalNum>0){
				List<CardFragment> cardFragments = cardFragmentManager.getCardFragmentByType(2);
				int fragmentId = cardFragments.get(0).getId();
				id = this.addUserFragment(roleId, fragmentId, universalNum);
				userCardFragment = new UserCardFragment();
				userCardFragment.setFragmentId(fragmentId);
				userCardFragment.setId(id.intValue());
				userCardFragment.setNum(universalNum);
				userCardFragment.setRoleId(roleId);
				userCardFragment.setState(1);
				userCardFragments.add(userCardFragment);
			}
			return userCardFragments;
		}
		return null;
		
	}

}
