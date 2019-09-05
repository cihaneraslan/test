package com.cgiser.moka.buy.action;

import java.util.List;

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
import com.cgiser.moka.model.LoginAward;
import com.cgiser.moka.model.RobRoleCache;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class BuyRobTimesAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<List<LoginAward>> returnType = new ReturnType<List<LoginAward>>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
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
			if(role.getCash()<10){
				returnType.setMsg("您的元宝不足哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(roleManager.updateCash(role.getRoleName(), 10)){
				robRoleCache.setRobHasTimes(robRoleCache.getRobHasTimes()+1);
				robRoleCacheManager.updateRobRoleCache(role.getRoleId(), robRoleCache);
			}else{
				logger.error("["+role.getRoleName()+"]购买匹配次数失败");
			}
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
