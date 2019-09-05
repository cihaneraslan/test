package com.cgiser.moka.map.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.manager.RobRoleCacheManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.UserStageManager;
import com.cgiser.moka.model.RobRoleCache;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class CollectMapInComeAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<Long> returnType = new ReturnType<Long>();
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
				robRoleCache.setRobHasTimes(5);
				robRoleCache.setRobRefreshTimes(2);
				robRoleCache.setRobRoleName("");
				robRoleCache.setRobStreakTimes(0);
			}
			UserStageManager userStageManager = (UserStageManager)HttpSpringUtils.getBean("userStageManager");
			int coins = userStageManager.getRoleMapCoins(role.getRoleId());
			int lastCoins = 0;
			if(robRoleCache!=null){
				lastCoins = robRoleCache.getHasBeenRobCoins();
			}
			lastCoins = coins - lastCoins;
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			roleManager.addCoin(role.getRoleName(), lastCoins);
			robRoleCache.setHasBeenRobCoins(0);
			robRoleCacheManager.updateRobRoleCache(role.getRoleId(), robRoleCache);
			Date date = new Date();
			role.setMapLastIncome(date);
			roleManager.updateRoleMapLastInCome(role.getRoleId(), date);
			returnType.setStatus(1);
			returnType.setValue(24*60*60*1000L);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
