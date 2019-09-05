package com.cgiser.moka.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.CardFragmentDao;
import com.cgiser.moka.manager.CardFragmentManager;
import com.cgiser.moka.manager.CardManager;
import com.cgiser.moka.model.Card;
import com.cgiser.moka.model.CardFragment;

public class CardFragmentManagerImpl implements CardFragmentManager {
	private CardFragmentDao cardFragmentDao;
	private CardManager cardManager;
	@Override
	public List<CardFragment> getCardFragment() {
		// TODO Auto-generated method stub
		return MapListToCardFragmentList(cardFragmentDao.getAllCardFragment());
	}

	@Override
	public CardFragment getCardFragmentByCardId(int cardId) {
		// TODO Auto-generated method stub
		return MapToCardFragment(cardFragmentDao.getCardFragmentByCardId(cardId));
	}

	@Override
	public CardFragment getCardFragmentByfragmentId(int fragmentId) {
		// TODO Auto-generated method stub
		return MapToCardFragment(cardFragmentDao.getCardFragmentByfragmentId(fragmentId));
	}
	private CardFragment MapToCardFragment(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		CardFragment cardFragment = new CardFragment();
		cardFragment.setCardId(Integer.parseInt((String)map.get("CARDID")));
		cardFragment.setId(Integer.parseInt((String)map.get("ID")));
		cardFragment.setNum(Integer.parseInt((String)map.get("NUM")));
		cardFragment.setState(Integer.parseInt((String)map.get("STATE")));
		cardFragment.setType(Integer.parseInt((String)map.get("TYPE")));
		cardFragment.setResolveNum(Integer.parseInt((String)map.get("RESOLVENUM")));
		cardFragment.setUniversal(Integer.parseInt((String)map.get("UNIVERSAL")));
		cardFragment.setResolveType(Integer.parseInt((String)map.get("RESOLVETYPE")));
		Card card = cardManager.getCardById(new Long(cardFragment.getCardId()));
		if(card!=null){
			cardFragment.setCardName(card.getCardName());
		}else{
			if(cardFragment.getType()==2){
				cardFragment.setCardName("万能碎片");
			}
		}
		return cardFragment;
	}
	
	private List<CardFragment> MapListToCardFragmentList(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<CardFragment> listFragments = new ArrayList<CardFragment>();
		for(int i=0;i<list.size();i++){
			listFragments.add(MapToCardFragment(list.get(i)));
		}
		return listFragments;
	}
	
	public CardFragmentDao getCardFragmentDao() {
		return cardFragmentDao;
	}

	public void setCardFragmentDao(CardFragmentDao cardFragmentDao) {
		this.cardFragmentDao = cardFragmentDao;
	}

	public CardManager getCardManager() {
		return cardManager;
	}

	public void setCardManager(CardManager cardManager) {
		this.cardManager = cardManager;
	}

	@Override
	public List<CardFragment> getCardFragmentByType(int type) {
		// TODO Auto-generated method stub
		return MapListToCardFragmentList(cardFragmentDao.getCardFragmentByType(type));
	}

}
