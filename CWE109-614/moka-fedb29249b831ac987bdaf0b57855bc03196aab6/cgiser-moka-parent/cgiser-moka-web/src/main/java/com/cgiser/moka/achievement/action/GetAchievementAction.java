package com.cgiser.moka.achievement.action;

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
import com.cgiser.moka.manager.AchievementManager;
import com.cgiser.moka.model.Achievement;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.UserAchievement;
import com.cgiser.moka.result.ReturnType;

public class GetAchievementAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<List<Achievement>> returnType = new ReturnType<List<Achievement>>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			AchievementManager achievementManager = (AchievementManager)HttpSpringUtils.getBean("achievementManager");
			List<Achievement> achievements = achievementManager.getAllAchievements();

			if(achievements==null){
				returnType.setMsg("获取成就失败！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
			}else{
				for(Achievement achievement :achievements){
					UserAchievement userAchievement = achievementManager.getUserAchievementById(role.getRoleId(), achievement.getAchievementId());
					if(userAchievement!=null){
						if(userAchievement.getFinishState()>=achievement.getFinishNum()){
							achievement.setIsFinish(1);
							achievement.setFinishState(achievement.getFinishNum());
						}else{
							achievement.setFinishState(userAchievement.getFinishState());
						}

					}else{
						achievement.setFinishState(0);
					}
				}
				returnType.setValue(achievements);
				returnType.setStatus(1);
				super.printReturnType2Response(response, returnType);
			}
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
