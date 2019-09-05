package com.cgiser.moka.fight.action;

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
import com.cgiser.moka.model.RobRoleCache;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class RefreshRodamRoleAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		ReturnType<String> returnType = new ReturnType<String>();
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
				returnType.setMsg("数据出错请联系客服！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			int refreshTimes = robRoleCache.getRobRefreshTimes();
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			if(refreshTimes>0){
				robRoleCache.setRobRoleName("");
				robRoleCache.setRobRefreshTimes(refreshTimes-1);
				robRoleCache.setLastRefreshTime(new Date());
			}else{
				if(role.getCash()>0){
					roleManager.updateCash(role.getRoleName(), 1);
					robRoleCache.setRobRoleName("");
					robRoleCache.setLastRefreshTime(new Date());
				}else{
					returnType.setMsg("您的元宝不够");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
			}
			returnType.setMsg("刷新成功！");
			returnType.setStatus(1);
			robRoleCacheManager.updateRobRoleCache(role.getRoleId(), robRoleCache);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	} 
}
