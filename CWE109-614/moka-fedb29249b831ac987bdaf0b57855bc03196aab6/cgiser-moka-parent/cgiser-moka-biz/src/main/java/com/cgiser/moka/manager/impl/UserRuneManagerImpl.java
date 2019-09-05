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

import com.cgiser.moka.dao.UserRuneDao;
import com.cgiser.moka.manager.AchievementManager;
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.RuneManager;
import com.cgiser.moka.manager.UserRuneManager;
import com.cgiser.moka.message.netty.MessageUtil;
import com.cgiser.moka.model.MessageType;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.Rune;
import com.cgiser.moka.model.StrengResult;
import com.cgiser.moka.model.UserAchievement;
import com.cgiser.moka.model.UserRune;

public class UserRuneManagerImpl implements UserRuneManager {
	Logger logger = LoggerFactory.getLogger("usercard.streng");
	private UserRuneDao userRuneDao;
	private RoleManager roleManager;
	private RuneManager runeManager;
	private AchievementManager achievementManager;
	private MessageManager messageManager;
	private double[] RUNE_EXP_RATIO = {0.6, 0.7, 0.8, 0.9, 1};//卡牌星级所对应的经验比例
	private int[] RUNE_COINS ={10, 12, 20, 30, 40};//星辰强化所对应的的消耗比例
	@Override
	public int SellRune(Long roleId,String userRuneIds) {
		if(StringUtils.isBlank(userRuneIds)){
			return 0;
		}
		
		String[] ids = userRuneIds.split("_");
		UserRune userRune;
		Role role = roleManager.getRoleById(roleId);
		logger.debug("用户["+role.getRoleName()+"]卖星辰["+userRuneIds+"]开始");
		Rune rune;
		for(int i=0;i<ids.length;i++){
			userRune = MapToUserRune(userRuneDao.getUserRuneById(new Long(ids[i])));
			if(userRune==null){
				continue;
			}
			rune = runeManager.getRuneById(userRune.getRuneId());
			if(rune==null){
				continue;
			}
			if(roleManager.addCoin(role.getRoleName(), rune.getPrice())){
				logger.debug("用户["+role.getRoleName()+"]增加["+rune.getPrice()+"]铜钱成功");
				if(userRuneDao.delUserRuneById(userRune.getUserRuneId())>0){
					logger.debug("用户["+role.getRoleName()+"]删除星辰["+userRune.getUserRuneId()+"]成功");
				}else{
					logger.debug("用户["+role.getRoleName()+"]删除星辰["+userRune.getUserRuneId()+"]失败");
				}
			}else{
				logger.debug("用户["+role.getRoleName()+"]增加["+rune.getPrice()+"]铜钱失败");
			}
		}
		logger.debug("用户["+role.getRoleName()+"]卖星辰["+userRuneIds+"]结束");
		return 1;
	}

	@Override
	public StrengResult StrengRunePreView(int userRuneId, String userRuneIds) {
		if(StringUtils.isBlank(userRuneIds)){
			return null;
		}
		String[] ids = userRuneIds.split("_");
		UserRune baseUserRune = MapToUserRune(userRuneDao.getUserRuneById(new Long(userRuneId)));
		Rune baseRune = runeManager.getRuneById(baseUserRune.getRuneId());
		if(baseUserRune==null||baseRune==null){
			return null;
		}
		UserRune userRune;
		Rune rune;
		int exp = 0;
		int expAdd = 0;
		int coins = 0;
		for(int i=0;i<ids.length;i++){
			userRune = MapToUserRune(userRuneDao.getUserRuneById(new Long(ids[i])));
			if(userRune==null){
				continue;
			}
			if(userRune.getUserRuneId()==baseUserRune.getUserRuneId()){
				continue;
			}
			rune = runeManager.getRuneById(userRune.getRuneId());
			if(userRune.getExp()==0){
				expAdd = rune.getBaseExp();
			}else{
				expAdd = userRune.getExp();
			}
			expAdd = (int)(expAdd*RUNE_EXP_RATIO[rune.getColor()-1]);
			coins += expAdd*RUNE_COINS[rune.getColor()-1];
			exp = baseUserRune.getExp()+expAdd;
			int level = baseRune.getLevel(exp);
			baseUserRune.setLevel(level);
			if(level==4){
				baseUserRune.setExp(baseRune.getExp(4));
			}else{
				baseUserRune.setExp(exp);
			}
			
		}
		StrengResult result = new StrengResult();
		result.setCardLevel(baseUserRune.getLevel());
		result.setCoins(coins);
		result.setExp(baseUserRune.getExp());
		return result;
	}

	@Override
	public StrengResult StrengRune(int userRuneId, String userRuneIds) {
		if(StringUtils.isBlank(userRuneIds)){
			return null;
		}
		String[] ids = userRuneIds.split("_");
		UserRune baseUserRune = MapToUserRune(userRuneDao.getUserRuneById(new Long(userRuneId)));
		Rune baseRune = runeManager.getRuneById(baseUserRune.getRuneId());
		if(baseUserRune==null||baseRune==null){
			return null;
		}
		UserRune userRune;
		Rune rune;
		int exp = 0;
		int expAdd = 0;
		int coins = 0;
		for(int i=0;i<ids.length;i++){
			userRune = MapToUserRune(userRuneDao.getUserRuneById(new Long(ids[i])));
			if(userRune==null){
				continue;
			}
			if(userRune.getUserRuneId()==baseUserRune.getUserRuneId()){
				continue;
			}
			rune = runeManager.getRuneById(userRune.getRuneId());
			if(userRune.getExp()==0){
				expAdd = rune.getBaseExp();
			}else{
				expAdd = userRune.getExp();
			}
			expAdd = (int)(expAdd*RUNE_EXP_RATIO[rune.getColor()-1]);
			coins += expAdd*RUNE_COINS[rune.getColor()-1];
			exp = baseUserRune.getExp()+expAdd;
			int level = baseRune.getLevel(exp);
			baseUserRune.setLevel(level);
			if(level==4){
				baseUserRune.setExp(baseRune.getExp(4));
			}else{
				baseUserRune.setExp(exp);
			}
			
		}
		Role role = roleManager.getRoleById(baseUserRune.getRoleId());
		if(role.getCoins()<coins){
			return null;
		}
		logger.debug("["+role.getRoleName()+"]开始强化星辰["+baseRune.getRuneName()+"],用户星辰ID:"+baseUserRune.getRuneId());
		if(roleManager.updateCoin(role.getRoleName(), coins)){
			logger.debug("扣除用户["+role.getRoleName()+"]"+coins+"铜钱");
			for(int i = 0;i<ids.length;i++){
				if(userRuneDao.delUserRuneById(new Long(ids[i]))>0){
					logger.debug("删除用户卡牌["+ids[i]+"]成功");
				}else{
					logger.debug("删除用户卡牌["+ids[i]+"]失败");
				}
			}
			if(userRuneDao.updateUserRune(baseUserRune.getUserRuneId(), baseUserRune.getLevel(), baseUserRune.getExp())>0){
				logger.debug("["+role.getRoleName()+"]强化卡牌["+baseRune.getRuneName()+"],用户卡牌ID:"+baseUserRune.getRuneId()+"成功");
				if(baseUserRune.getLevel()==4){
					UserAchievement userAchievement = achievementManager.getUserAchievementById(role.getRoleId(), 34);
					if(userAchievement==null){
						achievementManager.saveUserAchievement(34, role.getRoleId(),1);
					}
					ChannelBuffer buffer = new DynamicChannelBuffer(200);
					buffer.writeInt(MessageType.SYSTEM.getCode());
					buffer.writeInt(MessageType.RUNESRTRENG.getCode());
					MessageUtil.writeString(buffer, role.getRoleName(), "UTF-8");
					buffer.writeInt(baseRune.getRuneId());
					messageManager.sendMessageToAll(buffer);
				}
			}else{
				logger.debug("["+role.getRoleName()+"]强化卡牌["+baseRune.getRuneName()+"],用户卡牌ID:"+baseUserRune.getRuneId()+"失败 ");
			}
			StrengResult result = new StrengResult();
			result.setCardLevel(baseUserRune.getLevel());
			result.setCoins(coins);
			result.setExp(baseUserRune.getExp());
			return result;
		}else{
			return null;
		}
	}
	@Override
	public List<UserRune> getUserGroupRunes(Long roleId, Long groupId) {
		// TODO Auto-generated method stub
		return MapListToUserRuneList(userRuneDao.getUserGroupRunes(roleId, groupId));
	}
	@Override
	public List<UserRune> getUserOwnRune(Long roleId) {
		// TODO Auto-generated method stub
		return MapListToUserRuneList(userRuneDao.getUserOwnRunes(roleId));
	}
	@Override
	public int delUserRuneById(Long userRuneId) {
		// TODO Auto-generated method stub
		return userRuneDao.delUserRuneById(userRuneId);
	}

	@Override
	public List<UserRune> getUserRune(Long roleId) {
		// TODO Auto-generated method stub
		return MapListToUserRuneList(userRuneDao.getUserRunes(roleId));
	}
	
	@Override
	public UserRune getUserRuneById(Long userRuneId) {
		
		return MapToUserRune(userRuneDao.getUserRuneById(userRuneId));
	}

	@Override
	public Long saveUserRune(int runeId, Long roleId) {
		
		Long userRuneId = userRuneDao.saveUserRune(runeId, roleId);
		if(userRuneId>0){
			List<UserRune> userRunes = this.getUserOwnRune(roleId);
			Set<String> runeIds = new HashSet<String>();
			if(!CollectionUtils.isEmpty(userRunes)){
				for(UserRune userRune : userRunes){
					runeIds.add(String.valueOf(userRune.getRuneId()));
				} 
			}
			if(runeIds!=null){
				UserAchievement userAchievement = achievementManager.getUserAchievementById(roleId, 32);
				if(userAchievement==null){
					achievementManager.saveUserAchievement(32, roleId,runeIds.size());
				}
				if(userAchievement!=null&&userAchievement.getFinishState()<20){
					if(userAchievement.getFinishState()<runeIds.size()){
						achievementManager.saveUserAchievement(32, roleId,runeIds.size());
					}
				}
			}
			if(runeManager.getRuneById(runeId).getColor()==5){
				UserAchievement userAchievement = achievementManager.getUserAchievementById(roleId, 33);
				if(userAchievement==null){
					achievementManager.saveUserAchievement(33, roleId,1);
				}
			}
		}
		return userRuneId;
	}
	private UserRune MapToUserRune(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		UserRune userRune = new UserRune();
		userRune.setExp(Integer.parseInt(map.get("EXP").toString()));
		userRune.setLevel(Integer.parseInt(map.get("LEVEL").toString()));
		userRune.setRoleId(new Long(map.get("ROLEID").toString()));
		userRune.setRuneId(Integer.parseInt(map.get("RUNEID").toString()));
		userRune.setState(Integer.parseInt(map.get("STATE").toString()));
		userRune.setUserRuneId(new Long(map.get("USERRUNEID").toString()));
//		userRune.setGroupId(Integer.parseInt(map.get("GROUPID").toString()));
		return userRune;
	}
	private List<UserRune> MapListToUserRuneList(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<UserRune> userRunes = new ArrayList<UserRune>();
		for(int i=0;i<list.size();i++){
			userRunes.add(MapToUserRune(list.get(i)));
		}
		return userRunes;
		
	}

	public UserRuneDao getUserRuneDao() {
		return userRuneDao;
	}

	public void setUserRuneDao(UserRuneDao userRuneDao) {
		this.userRuneDao = userRuneDao;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public RuneManager getRuneManager() {
		return runeManager;
	}

	public void setRuneManager(RuneManager runeManager) {
		this.runeManager = runeManager;
	}

	public AchievementManager getAchievementManager() {
		return achievementManager;
	}

	public void setAchievementManager(AchievementManager achievementManager) {
		this.achievementManager = achievementManager;
	}

	public MessageManager getMessageManager() {
		return messageManager;
	}

	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}




}
