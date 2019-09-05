package com.cgiser.moka.map.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.manager.RobLogManager;
import com.cgiser.moka.manager.RobRoleCacheManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.UserStageManager;
import com.cgiser.moka.model.RobLog;
import com.cgiser.moka.model.RobRoleCache;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.MapLastInComeResult;
import com.cgiser.moka.result.ReturnType;
import com.cgiser.moka.result.RobRole;

public class GetMapLastInComeAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<MapLastInComeResult> returnType = new ReturnType<MapLastInComeResult>();
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
				robRoleCacheManager.updateRobRoleCache(role.getRoleId(), robRoleCache);
			}
			UserStageManager userStageManager = (UserStageManager)HttpSpringUtils.getBean("userStageManager");
			int coins = userStageManager.getRoleMapCoins(role.getRoleId());
			int lastCoins = 0;
			if(robRoleCache!=null){
				lastCoins = robRoleCache.getHasBeenRobCoins();
			}
			lastCoins = coins - lastCoins;
			MapLastInComeResult result = new MapLastInComeResult();
			result.setCoins(coins);
			result.setLastCoins(lastCoins);
			RobLogManager robLogManager = (RobLogManager)HttpSpringUtils.getBean("robLogManager");
			Date date = null;
			if(role.getMapLastIncome()==null){
				Calendar curDate = Calendar.getInstance();
				curDate.add(Calendar.DATE, -1);
				date = curDate.getTime();
			}else{
				date = role.getMapLastIncome();
			}
			List<RobLog> robLogs = robLogManager.getLastRobRoleLog(role.getRoleId(),date);
			if(!CollectionUtils.isEmpty(robLogs)){
				RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
				List<RobRole> robRoles = new ArrayList<RobRole>();
				RobRole robRole;
				Role rRole;
				for(RobLog robLog:robLogs){
					robRole = new RobRole();
					rRole = roleManager.getRoleById(robLog.getRoleId());
					robRole.setAvatar(rRole.getAvatar());
					robRole.setEffective(roleManager.getRoleEffective(robLog.getRoleId()));
					robRole.setRobCoins(robLog.getRobRoleCoins());
					robRole.setRoleId(rRole.getRoleId());
					robRole.setRoleName(rRole.getRoleName());
					robRole.setLevel(role.getLevel());
					robRoles.add(robRole);
				}
				result.setRoles(robRoles);
			}
			returnType.setStatus(1);
			returnType.setValue(result);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
