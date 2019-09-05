package com.cgiser.moka.fight.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.manager.CardManager;
import com.cgiser.moka.manager.RobRoleCacheManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.UserCardGroupManager;
import com.cgiser.moka.manager.UserStageManager;
import com.cgiser.moka.model.Card;
import com.cgiser.moka.model.RobRoleCache;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.UserCard;
import com.cgiser.moka.model.UserCardGroup;
import com.cgiser.moka.result.ReturnType;
import com.cgiser.moka.result.RobRoleSet;

public class GetRodamRoleAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public static int robTimes = 5;
	public static int robRefreshTimes = 2;
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		ReturnType<RobRoleSet> returnType = new ReturnType<RobRoleSet>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RobRoleCacheManager robRoleCacheManager = (RobRoleCacheManager)HttpSpringUtils.getBean("robRoleCacheManager");
			RobRoleCache robRoleCache = robRoleCacheManager.getRobRoleCache(role.getRoleId());
			if(robRoleCache==null){
				robRoleCache = new RobRoleCache();
				robRoleCache.setHasBeenRobCoins(0);
				robRoleCache.setRobHasTimes(robTimes);
				robRoleCache.setRobRefreshTimes(robRefreshTimes);
				if(role.getVip()==3){
					robRoleCache.setRobRefreshTimes(4);
				}
				robRoleCache.setRobRoleName("");
				robRoleCache.setRobStreakTimes(0);
				robRoleCacheManager.updateRobRoleCache(role.getRoleId(), robRoleCache);
			}
			RobRoleSet robRoleSet = new RobRoleSet();
			Date date = new Date();
			if(robRoleCache.getRobLastTime()==null){
				if(robRoleCache.getRobHasTimes()<5){
					robRoleCache.setRobHasTimes(5);
				}				
			}else if(robRoleCache.getRobLastTime().before(date)&&date.getDate()!=robRoleCache.getRobLastTime().getDate()){
				if(robRoleCache.getRobHasTimes()<5){
					robRoleCache.setRobHasTimes(5);
				}	
			}else if(robRoleCache.getRobLastTime().before(date)&&date.getDate()==robRoleCache.getRobLastTime().getDate()){
				robRoleCache.setRobHasTimes(robRoleCache.getRobHasTimes());
			}else{
				robRoleCache.setRobHasTimes(5);
			}
			if(robRoleCache.getLastRefreshTime()==null){
				robRoleCache.setRobRefreshTimes(robRefreshTimes);
				if(role.getVip()==3){
					robRoleCache.setRobRefreshTimes(4);
				}		
			}else if(robRoleCache.getLastRefreshTime().before(date)&&date.getDate()!=robRoleCache.getLastRefreshTime().getDate()){
				robRoleCache.setRobRefreshTimes(robRefreshTimes);
				if(role.getVip()==3){
					robRoleCache.setRobRefreshTimes(4);
				}
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			robRoleSet.setRobRefreshCash(1);
			Role robRole = null;
			robRoleSet.setRobTimesCash(10);
			if(StringUtils.isEmpty(robRoleCache.getRobRoleName())){
				robRole = roleManager.RandomRoleByLevel(role.getRoleId());
				robRoleSet.setRobRoleName(robRole.getRoleName());
				robRoleCache.setRobRoleName(robRole.getRoleName());
				robRoleCacheManager.updateRobRoleCache(role.getRoleId(), robRoleCache);
				robRoleSet.setLevel(robRole.getLevel());
			}else{
				robRoleSet.setRobRoleName(robRoleCache.getRobRoleName());
				robRole = roleManager.getRoleByName(robRoleCache.getRobRoleName());
				robRoleSet.setLevel(robRole.getLevel());
			}
			robRoleSet.setRobHasTimes(robRoleCache.getRobHasTimes());
			robRoleSet.setRobRefreshTimes(robRoleCache.getRobRefreshTimes());
			if(robRoleCache.getRobLastTime()==null){
				robRoleSet.setRobLastTime(null);
			}else{
				robRoleSet.setRobLastTime(robRoleCache.getRobLastTime().getTime());
			}
			
			robRoleCacheManager.updateRobRoleCache(role.getRoleId(), robRoleCache);
			CardManager cardManager = (CardManager)HttpSpringUtils.getBean("cardManager");
			UserStageManager userStageManager = (UserStageManager)HttpSpringUtils.getBean("userStageManager");
			int coins = userStageManager.getRoleMapCoins(robRole.getRoleId());
			RobRoleCache robRoleCache1 = robRoleCacheManager.getRobRoleCache(robRole.getRoleId());
			int lastCoins = 0;
			if(coins<2000){
				robRoleSet.setCoins(1000);
			}else{
				if(robRoleCache1!=null){
					lastCoins = robRoleCache1.getHasBeenRobCoins();
				}
				lastCoins = coins - lastCoins;

				if((lastCoins-coins/2)<1000){
					robRoleSet.setCoins(1000);
				}else{
					robRoleSet.setCoins(coins/10);
				}
			}

			robRoleSet.setEffective(roleManager.getRoleEffective(robRole.getRoleId()));
			UserCardGroupManager userCardGroupManager = (UserCardGroupManager)HttpSpringUtils.getBean("userCardGroupManager");
			UserCardGroup userCardGroup = userCardGroupManager.getUserCardGroupById(new Long(robRole.getDefaultGroupId()));
			Map<String, Integer> stars = new HashMap<String, Integer>();
			stars.put("star1", 0);
			stars.put("star2", 0);
			stars.put("star3", 0);
			stars.put("star4", 0);
			stars.put("star5", 0);
			Card card = null;
			if(userCardGroup!=null&&!CollectionUtils.isEmpty(userCardGroup.getUserCardInfo())){
				for(UserCard userCard:userCardGroup.getUserCardInfo()){
					if(userCard==null||userCard.getCardId()==0){
						logger.error(role.getRoleName()+"有一张不存在的卡牌");
						continue;
					}
					card = cardManager.getCardById(new Long(userCard.getCardId()));
					if(card==null){
						logger.error(role.getRoleName()+"有一张不存在的卡牌");
						continue;
					}
					stars.put("star"+card.getColor(), stars.get("star"+card.getColor())+1);
				}
			}
			robRoleSet.setStars(stars);
			robRoleSet.setRobTimes(5);
			if(robRole.getAvatar()==0){
				robRoleSet.setRobRoleAvatar(robRole.getSex()==0?10000:10001);
			}else{
				robRoleSet.setRobRoleAvatar(robRole.getAvatar());
			}
			returnType.setStatus(1);
			returnType.setValue(robRoleSet);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	} 
}
