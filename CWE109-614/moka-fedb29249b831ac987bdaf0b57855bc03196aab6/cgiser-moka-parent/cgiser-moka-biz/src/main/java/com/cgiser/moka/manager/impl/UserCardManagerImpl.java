package com.cgiser.moka.manager.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.UserCardDao;
import com.cgiser.moka.manager.AchievementManager;
import com.cgiser.moka.manager.CardManager;
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.UserCardManager;
import com.cgiser.moka.manager.UserSoulManager;
import com.cgiser.moka.message.netty.MessageUtil;
import com.cgiser.moka.model.Card;
import com.cgiser.moka.model.MessageType;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.StrengResult;
import com.cgiser.moka.model.UserAchievement;
import com.cgiser.moka.model.UserCard;
import com.cgiser.moka.model.UserSoul;

public class UserCardManagerImpl implements UserCardManager {
	Logger logger = LoggerFactory.getLogger("usercard.streng");
	private UserCardDao userCardDao;
	private CardManager cardManager;
	private RoleManager roleManager;
	private AchievementManager achievementManager;
	private UserSoulManager userSoulManager;
	private MessageManager messageManager;
	public MessageManager getMessageManager() {
		return messageManager;
	}
	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}
	private double[] CARD_EXP_RATIO = {0.6, 0.7, 0.8, 0.9, 1};//卡牌星级所对应的经验比例
    private int[] CARD_COINS = {2, 3, 5, 8, 10};//卡牌星级所对应的铜钱消耗比例
    
	@Override
	public List<UserCard> getUserCard(Long roleId) {
		return MapListToUserCardList(userCardDao.getUserCards(roleId,1));
	}
	@Override
	public List<UserCard> GetUserCardBySoul(Long roleId) {
		// TODO Auto-generated method stub
		return MapListToUserCardList(userCardDao.getUserCardsBySoul(roleId, 1));
	}
	@Override
	public List<UserCard> getUserOwnCard(Long roleId) {
		// TODO Auto-generated method stub
		return MapListToUserCardList(userCardDao.getUserOwnCards(roleId));
	}
	@Override
	public Long saveUserCard(int cardId, Long roleId) {
		Long userCardId = userCardDao.saveUserCard(cardId, roleId);
		if(userCardId>0){
			if(cardManager.getCardById(new Long(cardId)).getColor()==5){
				UserAchievement userAchievement = achievementManager.getUserAchievementById(roleId, 35);
				if(userAchievement==null){
					achievementManager.saveUserAchievement(35, roleId,1);
				}
			}
			List<UserCard> userCards = this.getUserOwnCard(roleId);
			Set<String> cardIds = new HashSet<String>();
			for(UserCard userCard : userCards){
				cardIds.add(String.valueOf(userCard.getCardId()));
			} 
			if(cardIds!=null){
				UserAchievement userAchievement = achievementManager.getUserAchievementById(roleId, 36);
				if(userAchievement==null){
					achievementManager.saveUserAchievement(36, roleId,cardIds.size());
				}
				if(userAchievement!=null&&userAchievement.getFinishState()<50){
					if(userAchievement.getFinishState()<cardIds.size()){
						achievementManager.saveUserAchievement(36, roleId,cardIds.size());
					}

				}
			}
		}
		return userCardId;

	}
	@Override
	public int delUserCardById(Long userCardId) {
		// TODO Auto-generated method stub
		return userCardDao.delUserCardById(userCardId);
	}
	@Override
	public UserCard getUserCardById(Long userCardId) {
		// TODO Auto-generated method stub
		return MapToUserCard(userCardDao.getUserCardById(userCardId));
	}
	@Override
	public StrengResult StrengCard(Long usesCardId,String userCardIds){
		if(StringUtils.isBlank(userCardIds)){
			return null;
		}
		String[] ids = userCardIds.split("_");
		UserCard baseUserCard = MapToUserCard(userCardDao.getUserCardById(usesCardId));
		Card baseCard = cardManager.getCardById(new Long(baseUserCard.getCardId()));
		if(baseUserCard==null||baseCard==null){
			return null;
		}
		UserCard userCard;
		Card card;
		int exp = 0;
		int expAdd = 0;
		int coins = 0;
		for(int i=0;i<ids.length;i++){
			userCard = MapToUserCard(userCardDao.getUserCardById(new Long(ids[i])));
			if(userCard==null){
				continue;
			}
			if(userCard.getUserCardId()==baseUserCard.getUserCardId()){
				continue;
			}
			card = cardManager.getCardById(new Long(userCard.getCardId()));
			if(userCard.getExp()==0){
				expAdd = card.getBaseExp();
			}else{
				expAdd = userCard.getExp();
			}
			expAdd = (int)(expAdd*CARD_EXP_RATIO[card.getColor()-1]);
			coins += expAdd*CARD_COINS[baseCard.getColor()-1];
			exp = baseUserCard.getExp()+expAdd;
			int level = baseCard.getLevel(exp);
			baseUserCard.setLevel(level);
			if(level==10){
				baseUserCard.setExp(baseCard.getExp(10));
			}else{
				baseUserCard.setExp(exp);
			}
			
		}
		Role role = roleManager.getRoleById(baseUserCard.getRoleId());
		if(role.getCoins()<coins){
			return null;
		}
		logger.debug("["+role.getRoleName()+"]开始强化卡牌["+baseCard.getCardName()+"],用户卡牌ID:"+baseUserCard.getUserCardId());
		if(roleManager.updateCoin(role.getRoleName(), coins)){
			logger.debug("扣除用户["+role.getRoleName()+"]"+coins+"铜钱");
			for(int i = 0;i<ids.length;i++){
				if(userCardDao.delUserCardById(new Long(ids[i]))>0){
					logger.debug("删除用户卡牌["+ids[i]+"]成功");
				}else{
					logger.debug("删除用户卡牌["+ids[i]+"]失败");
				}
			}
			if(userCardDao.updateUserCard(baseUserCard.getUserCardId(), baseUserCard.getLevel(), baseUserCard.getExp())>0){
				logger.debug("["+role.getRoleName()+"]强化卡牌["+baseCard.getCardName()+"],用户卡牌ID:"+baseUserCard.getUserCardId()+"成功");
				if(baseUserCard.getLevel()==10){
					UserAchievement userAchievement = achievementManager.getUserAchievementById(role.getRoleId(), 37);
					if(userAchievement==null){
						achievementManager.saveUserAchievement(37, role.getRoleId(),1);
					}
					ChannelBuffer buffer = new DynamicChannelBuffer(200);
					buffer.writeInt(MessageType.SYSTEM.getCode());
					buffer.writeInt(MessageType.CARDSTRENG.getCode());
					MessageUtil.writeString(buffer, role.getRoleName(), "UTF-8");
					buffer.writeInt(baseCard.getCardId());
					messageManager.sendMessageToAll(buffer);
				}
			}else{
				logger.debug("["+role.getRoleName()+"]强化卡牌["+baseCard.getCardName()+"],用户卡牌ID:"+baseUserCard.getUserCardId()+"失败 ");
			}
			StrengResult result = new StrengResult();
			result.setCardLevel(baseUserCard.getLevel());
			result.setCoins(coins);
			result.setExp(baseUserCard.getExp());
			return result;
		}else{
			return null;
		}
		

	}
	@Override
	public StrengResult StrengCardPreView(Long usesCardId, String userCardIds) {
		if(StringUtils.isBlank(userCardIds)){
			return null;
		}
		String[] ids = userCardIds.split("_");
		UserCard baseUserCard = MapToUserCard(userCardDao.getUserCardById(usesCardId));
		Card baseCard = cardManager.getCardById(new Long(baseUserCard.getCardId()));
		if(baseUserCard==null||baseCard==null){
			return null;
		}
		UserCard userCard;
		Card card;
		int exp = 0;
		int expAdd = 0;
		int coins = 0;
		for(int i=0;i<ids.length;i++){
			userCard = MapToUserCard(userCardDao.getUserCardById(new Long(ids[i])));
			if(userCard==null){
				continue;
			}
			if(userCard.getUserCardId()==baseUserCard.getUserCardId()){
				continue;
			}
			card = cardManager.getCardById(new Long(userCard.getCardId()));
			if(userCard.getExp()==0){
				expAdd = card.getBaseExp();
			}else{
				expAdd = userCard.getExp();
			}
			expAdd = (int)(expAdd*CARD_EXP_RATIO[card.getColor()-1]);
			coins += expAdd*CARD_COINS[baseCard.getColor()-1];
			exp = baseUserCard.getExp()+expAdd;
			int level = baseCard.getLevel(exp);
			baseUserCard.setLevel(level);
			if(level==10){
				baseUserCard.setExp(baseCard.getExp(10));
			}else{
				baseUserCard.setExp(exp);
			}
			
		}
		StrengResult result = new StrengResult();
		result.setCardLevel(baseUserCard.getLevel());
		result.setCoins(coins);
		result.setExp(baseUserCard.getExp());
		return result;
	}
	@Override
	public int SellCard(Long roleId,String userCardIds) {
		if(StringUtils.isBlank(userCardIds)){
			return 0;
		}
		
		String[] ids = userCardIds.split("_");
		UserCard userCard;
		Card card;
		Role role = roleManager.getRoleById(roleId);
		logger.debug("用户["+role.getRoleName()+"]卖卡牌["+userCardIds+"]开始");
		for(int i=0;i<ids.length;i++){
			userCard = MapToUserCard(userCardDao.getUserCardById(new Long(ids[i])));
			if(userCard==null){
				continue;
			}
			card = cardManager.getCardById(new Long(userCard.getCardId()));
			if(card==null){
				continue;
			}
			if(roleManager.addCoin(role.getRoleName(), card.getPrice())){
				logger.debug("用户["+role.getRoleName()+"]增加["+card.getPrice()+"]铜钱成功");
				if(userCardDao.delUserCardById(userCard.getUserCardId())>0){
					logger.debug("用户["+role.getRoleName()+"]删除卡牌["+userCard.getUserCardId()+"]成功");
				}else{
					logger.debug("用户["+role.getRoleName()+"]删除卡牌["+userCard.getUserCardId()+"]失败");
				}
			}else{
				logger.debug("用户["+role.getRoleName()+"]增加["+card.getPrice()+"]铜钱失败");
			}
		}
		logger.debug("用户["+role.getRoleName()+"]卖卡牌["+userCardIds+"]结束");
		return 1;
	}
	@Override
	public int ResetUserCardSoul(Long userCardId, Long userSoulId) {
		// TODO Auto-generated method stub
		return userCardDao.updateUserSoul(userCardId, userSoulId);
	}
	private UserCard MapToUserCard(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		UserCard userCard = new UserCard();
		userCard.setCardId(Integer.parseInt(map.get("CARDID").toString()));
		userCard.setExp(Integer.parseInt(map.get("EXP").toString()));
//		userCard.setGroupId(Integer.parseInt(map.get("GROUPID").toString()));
		userCard.setLevel(Integer.parseInt(map.get("LEVEL").toString()));
		userCard.setRoleId(new Long(map.get("ROLEID").toString()));
		userCard.setUserCardId(new Long(map.get("USERCARDID").toString()));
		userCard.setState(Integer.parseInt(map.get("STATE").toString()));
		Long userSoulId = new Long(map.get("SOUL").toString());
		UserSoul userSoul = null;
		if(userSoulId>0){
			userSoul = userSoulManager.getUserSoulById(userSoulId);
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
	public UserCardDao getUserCardDao() {
		return userCardDao;
	}
	public void setUserCardDao(UserCardDao userCardDao) {
		this.userCardDao = userCardDao;
	}
	public CardManager getCardManager() {
		return cardManager;
	}
	public void setCardManager(CardManager cardManager) {
		this.cardManager = cardManager;
	}
	public RoleManager getRoleManager() {
		return roleManager;
	}
	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}
	public AchievementManager getAchievementManager() {
		return achievementManager;
	}
	public void setAchievementManager(AchievementManager achievementManager) {
		this.achievementManager = achievementManager;
	}
	public UserSoulManager getUserSoulManager() {
		return userSoulManager;
	}
	public void setUserSoulManager(UserSoulManager userSoulManager) {
		this.userSoulManager = userSoulManager;
	}
	@Override
	public UserCard GetUserCardByUserSoulId(Long roleId, Long userSoulId) {
		// TODO Auto-generated method stub
		return MapToUserCard(userCardDao.getUserCardsByUserSoulId(roleId, userSoulId, 1));
	}









}
