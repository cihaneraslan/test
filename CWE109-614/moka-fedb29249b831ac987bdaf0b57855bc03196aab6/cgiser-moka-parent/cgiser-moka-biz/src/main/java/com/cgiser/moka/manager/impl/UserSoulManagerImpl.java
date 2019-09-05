package com.cgiser.moka.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.UserSoulDao;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.SoulManager;
import com.cgiser.moka.manager.UserSoulManager;
import com.cgiser.moka.model.FightCard;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.Soul;
import com.cgiser.moka.model.UserSoul;

public class UserSoulManagerImpl implements UserSoulManager {
	Logger logger = LoggerFactory.getLogger("usercard.streng");
	private UserSoulDao userSoulDao;
	private SoulManager soulManager;
	private RoleManager roleManager;
	@Override
	public UserSoul getUserSoulById(Long userSoulId) {
		// TODO Auto-generated method stub
		return MapToUserSoul(userSoulDao.getUserSoulById(userSoulId));
	}

	@Override
	public List<UserSoul> getUserSoulByRoleId(Long roleId) {
		
		return MapListToUserSoulList(userSoulDao.getSoulsByRoleId(roleId));
	}
	private List<UserSoul> MapListToUserSoulList(List<Map<String, Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<UserSoul> userSouls = new ArrayList<UserSoul>();
		for(int i=0;i<list.size();i++){
			userSouls.add(MapToUserSoul(list.get(i)));
		}
		return  userSouls;
	}
	private UserSoul MapToUserSoul(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		UserSoul userSoul = new UserSoul();
		userSoul.setUserSoulId(new Long(map.get("USERSOULID").toString()));
		userSoul.setRoleId(new Long(map.get("ROLEID").toString()));
		userSoul.setSoulId(Integer.parseInt(map.get("SOULID").toString()));
		userSoul.setLevel(Integer.parseInt(map.get("LEVEL").toString()));
		userSoul.setState(Integer.parseInt(map.get("STATE").toString()));
		return userSoul;
	}
	 
	@Override
	public void handleFightCard(UserSoul userSoul, FightCard fightCard) {
		Soul soul = soulManager.getSoulById(userSoul.getSoulId());
//		if(soul.getSoulType()==1){
//			if(soul.getAddType()==1){
//				fightCard.setAttack(fightCard.getAttack()+soul.getValue()+soul.getValuePro()*(userSoul.getLevel()-1));
//			}else{
//				fightCard.setAttack(fightCard.getAttack()+(soul.getValue()+soul.getValuePro())*(userSoul.getLevel()-1));
//			}
//			fightCard.setSoul1(userSoul);
//		}else{
//			if(soul.getAddType()==1){
//				fightCard.sethP(fightCard.gethP()+soul.getValue()+soul.getValuePro()*(userSoul.getLevel()-1));
//			}else{
//				fightCard.sethP(fightCard.gethP()+(soul.getValue()+soul.getValuePro())*(userSoul.getLevel()-1));
//			}
//			fightCard.setSoul2(userSoul);
//		}
		
		
	}
	public UserSoulDao getUserSoulDao() {
		return userSoulDao;
	}

	public void setUserSoulDao(UserSoulDao userSoulDao) {
		this.userSoulDao = userSoulDao;
	}

	public SoulManager getSoulManager() {
		return soulManager;
	}

	public void setSoulManager(SoulManager soulManager) {
		this.soulManager = soulManager;
	}

	@Override
	public int deleteUserSoul(Long userSoulId) {
		// TODO Auto-generated method stub
		return userSoulDao.deleteUserSoulById(userSoulId);
	}

	@Override
	public Long saveUserSoul(Long roleId, int soulId, int level) {
		// TODO Auto-generated method stub
		return userSoulDao.saveUserSoul(soulId, roleId, level);
	}

	@Override
	public int sellSoul(Long roleId, String userSoulIds) {
		if(StringUtils.isBlank(userSoulIds)){
			return 0;
		}
		
		String[] ids = userSoulIds.split("_");
		UserSoul userSoul;
		Role role = roleManager.getRoleById(roleId);
		logger.debug("用户["+role.getRoleName()+"]卖武器["+userSoulIds+"]开始");
		Soul soul;
		for(int i=0;i<ids.length;i++){
			userSoul = MapToUserSoul(userSoulDao.getUserSoulById(new Long(ids[i])));
			if(userSoul==null){
				continue;
			}
			soul = soulManager.getSoulById(userSoul.getSoulId());
			if(soul==null){
				continue;
			}
			if(roleManager.addCoin(role.getRoleName(), soul.getPrice())){
				logger.debug("用户["+role.getRoleName()+"]增加["+soul.getPrice()+"]铜钱成功");
				if(this.deleteUserSoul(userSoul.getUserSoulId())>0){
					logger.debug("用户["+role.getRoleName()+"]删除武器["+userSoul.getUserSoulId()+"]成功");
				}else{
					logger.error("用户["+role.getRoleName()+"]删除武器["+userSoul.getUserSoulId()+"]失败");
				}
			}else{
				logger.error("用户["+role.getRoleName()+"]增加["+soul.getPrice()+"]铜钱失败");
			}
		}
		logger.debug("用户["+role.getRoleName()+"]卖武器["+userSoulIds+"]结束");
		return 1;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}



}
