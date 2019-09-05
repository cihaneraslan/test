package com.cgiser.moka.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.UserCardDao;
import com.cgiser.moka.dao.UserCardGroupDao;
import com.cgiser.moka.manager.UserCardGroupManager;
import com.cgiser.moka.manager.UserCardManager;
import com.cgiser.moka.manager.UserRuneManager;
import com.cgiser.moka.manager.UserSoulManager;
import com.cgiser.moka.model.UserCard;
import com.cgiser.moka.model.UserCardGroup;
import com.cgiser.moka.model.UserRune;
import com.cgiser.moka.model.UserSoul;

public class UserCardGroupManagerImpl implements UserCardGroupManager {
	private UserCardGroupDao userCardGroupDao;
	private UserCardDao userCardDao;
	private UserRuneManager userRuneManager;
	private UserSoulManager userSoulManager;
	private UserCardManager userCardManager;
	@Override
	public List<UserCardGroup> getUserCardGroup(Long roleId) {
		List<Map<String,Object>> list = userCardGroupDao.getUserCardGroups(roleId);
		return MapListToUserCardGroupList(list);
	}
	@Override
	public int saveUserCardGroup(Long groupId, String cardIds, String runeIds) {
		UserCardGroup userCardGroup = MapToUserCardGroup(userCardGroupDao.getUserCardGroupById(groupId));
		if(userCardGroup==null){
			return 0;
		}
		String[] cardidArr = cardIds.split("_");
		String strUserCardIds = userCardGroup.getUserCardIds();
		if(!StringUtils.isEmpty(strUserCardIds)){
			String[] strs = strUserCardIds.split("_");
			UserCard userCard = new UserCard();
			for(String userId:strs){
				userCard = userCardManager.getUserCardById(new Long(userId));
				if(userCard!=null){
					if(userCard.getUserSoul()!=null){
						if(!containId(cardidArr,String.valueOf(userCard.getUserCardId()))){
							userCardManager.ResetUserCardSoul(userCard.getUserCardId(), 0L);
						}
						
					}
				}
			}
		}
//		String strUserRuneIds = userCardGroup.getUserRuneIds();
		return userCardGroupDao.updateUserGroup(groupId, cardIds, runeIds);
	}
	private boolean containId(String[] strs,String id){
		for(String str:strs){
			if(str.equals(id)){
				return true;
			}
		}
		return false;
	}
	@Override
	public Long createUserCardGroup(Long roleId, String groupName,String cardIds, String runeIds) {
		StringBuffer cards = new StringBuffer();
		StringBuffer runes = new StringBuffer();
		if(!StringUtils.isBlank(cardIds)){
			String[] strCardIds = cardIds.split("_");
			for(int i=0;i<strCardIds.length;i++){
				cards.append(userCardDao.saveUserCard(Integer.parseInt(strCardIds[i]), roleId));
				cards.append("_");
			}
		}
		if(!StringUtils.isBlank(runeIds)){
			String[] strRuneIds = runeIds.split("_");
			for(int i=0;i<strRuneIds.length;i++){
				runes.append(userRuneManager.saveUserRune(Integer.parseInt(strRuneIds[i]), roleId));
				runes.append("_");
			}
		}
		if(StringUtils.isBlank(cardIds)){
			cards.append("_");
		}
		if(StringUtils.isBlank(runeIds)){
			runes.append("_");
		}
		return userCardGroupDao.insertUserGroup(roleId, groupName,cards.substring(0,cards.length()-1), runes.substring(0,runes.length()-1));
	}
	private List<UserCardGroup> MapListToUserCardGroupList(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<UserCardGroup> listUserCardGroup = new ArrayList<UserCardGroup>();
		for(int i=0;i<list.size();i++){
			listUserCardGroup.add(MapToUserCardGroup(list.get(i)));
		}
		return listUserCardGroup;
	}
	private UserCardGroup MapToUserCardGroup(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		UserCardGroup userCardGroup = new UserCardGroup();
		userCardGroup.setGroupId(new Long(map.get("GROUPID").toString()));
		userCardGroup.setRoleId(new Long(map.get("ROLEID").toString()));
		userCardGroup.setGroupName((String)map.get("GROUPNAME"));
		String strCards = map.get("CARDLIST").toString();
		if(!StringUtils.isBlank(strCards)){
			userCardGroup.setUserCardIds(strCards);
			String[] strUserCards = strCards.split("_");
			List<UserCard> cards = new ArrayList<UserCard>();
			for(int i=0;i<strUserCards.length;i++){
				cards.add(MapToUserCard(userCardDao.getUserCardById(new Long(strUserCards[i]))));
			}
			userCardGroup.setUserCardInfo(cards);
		}
		String strRunes = map.get("RUNELIST").toString();
		if(!StringUtils.isBlank(strRunes)){
			userCardGroup.setUserRuneIds(strRunes);
			String[] strUserRunes = strRunes.split("_");
			List<UserRune> runes = new ArrayList<UserRune>();
			for(int i=0;i<strUserRunes.length;i++){
				runes.add(userRuneManager.getUserRuneById(new Long(strUserRunes[i])));
			}
			userCardGroup.setUserRuneInfo(runes);
		}
		return userCardGroup;
	}
	private UserCard MapToUserCard(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		UserCard userCard = new UserCard();
		userCard.setCardId(Integer.parseInt(map.get("CARDID").toString()));
		userCard.setExp(Integer.parseInt(map.get("EXP").toString()));
		userCard.setGroupId(Integer.parseInt(map.get("GROUPID").toString()));
		userCard.setLevel(Integer.parseInt(map.get("LEVEL").toString()));
		userCard.setRoleId(new Long(map.get("ROLEID").toString()));
		userCard.setUserCardId(new Long(map.get("USERCARDID").toString()));
		userCard.setState(Integer.parseInt(map.get("STATE").toString()));
		Long userSoulId = new Long(map.get("SOUL").toString());
		UserSoul userSoul = null;
		if(userSoulId>0){
			userSoul = userSoulManager.getUserSoulById(userSoulId);
			userSoul.setIsEquipment(1);
		}
		userCard.setUserSoul(userSoul);
		return userCard;
	}
	private List<UserCard> MapListToUserCardList(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<UserCard> listUserCard = new ArrayList<UserCard>();
		for(int i=0;i<list.size();i++){
			listUserCard.add(MapToUserCard(list.get(i)));
		}
		return listUserCard;
	}
	public UserCardGroupDao getUserCardGroupDao() {
		return userCardGroupDao;
	}
	public void setUserCardGroupDao(UserCardGroupDao userCardGroupDao) {
		this.userCardGroupDao = userCardGroupDao;
	}
	public UserCardDao getUserCardDao() {
		return userCardDao;
	}
	public void setUserCardDao(UserCardDao userCardDao) {
		this.userCardDao = userCardDao;
	}
	public UserRuneManager getUserRuneManager() {
		return userRuneManager;
	}
	public void setUserRuneManager(UserRuneManager userRuneManager) {
		this.userRuneManager = userRuneManager;
	}
	@Override
	public UserCardGroup getUserCardGroupById(Long groupId) {
		// TODO Auto-generated method stub
		return  MapToUserCardGroup(userCardGroupDao.getUserCardGroupById(groupId));
	}
	@Override
	public int updateUserCardGroupName(Long groupId, String groupName) {
		// TODO Auto-generated method stub
		return userCardGroupDao.updateUserGroupName(groupId, groupName);
	}
	public UserSoulManager getUserSoulManager() {
		return userSoulManager;
	}
	public void setUserSoulManager(UserSoulManager userSoulManager) {
		this.userSoulManager = userSoulManager;
	}
	public UserCardManager getUserCardManager() {
		return userCardManager;
	}
	public void setUserCardManager(UserCardManager userCardManager) {
		this.userCardManager = userCardManager;
	}



}
