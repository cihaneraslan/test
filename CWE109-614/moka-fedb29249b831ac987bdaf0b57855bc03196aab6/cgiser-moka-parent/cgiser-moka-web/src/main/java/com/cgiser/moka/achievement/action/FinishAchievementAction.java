package com.cgiser.moka.achievement.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.AchievementManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.UserAchievement;
import com.cgiser.moka.result.ReturnType;
import com.cgiser.sso.model.User;

public class FinishAchievementAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<String> returnType = new ReturnType<String>();
		try{
			String type = ServletUtil.getDefaultValue(request, "type", "1");
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(!StringUtils.isNumeric(type)){
				returnType.setMsg("不合法的请求！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(Integer.parseInt(type)==56){
				RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
				User user = roleManager.getUserByUserIden(role.getUserIden());
				if(user.getEmail()!=null&&!user.getEmail().equals("")){
					AchievementManager achievementManager = (AchievementManager)HttpSpringUtils.getBean("achievementManager");
					UserAchievement userAchievement = achievementManager.getUserAchievementById(role.getRoleId(), 56);
					if(userAchievement==null){
						achievementManager.saveUserAchievement(Integer.parseInt(type), role.getRoleId(),1);
					}
				}
			}else if((Integer.parseInt(type)>=59&&Integer.parseInt(type)<=61)||(Integer.parseInt(type)>=65&&Integer.parseInt(type)<=67)){
				AchievementManager achievementManager = (AchievementManager)HttpSpringUtils.getBean("achievementManager");
				UserAchievement userAchievement = achievementManager.getUserAchievementById(role.getRoleId(), Integer.parseInt(type));
				if(userAchievement==null){
					achievementManager.saveUserAchievement(Integer.parseInt(type), role.getRoleId(),1);
				}
			}else if(Integer.parseInt(type)==46){
				AchievementManager achievementManager = (AchievementManager)HttpSpringUtils.getBean("achievementManager");
				UserAchievement userAchievement = achievementManager.getUserAchievementById(role.getRoleId(), 46);
				if(userAchievement==null){
					achievementManager.saveUserAchievement(Integer.parseInt(type), role.getRoleId(),1);
				}
			}else{
				returnType.setMsg("不合法的请求！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			
			returnType.setValue("完成成就成功！");
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
			return null;
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		try{
			returnType.setValue(null);
			returnType.setStatus(0);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);// TODO: handle exception
		}
		return null;
		
	}
}
