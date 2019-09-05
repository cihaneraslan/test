package com.cgiser.moka.legion.action;

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
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.UserAchievement;
import com.cgiser.moka.result.ReturnType;

public class CreateLegionAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String name = ServletUtil.getDefaultValue(request, "name", "");
		ReturnType<Long> returnType = new ReturnType<Long>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(StringUtils.isBlank(name)){
				logger.debug("帮派名称不符合要求");
				returnType.setStatus(0);
				returnType.setValue(0L);
				returnType.setMsg("帮派名无效(由2-6个汉字,数字和26个英文字母或者下划线组成的字符串,不能以下划线开头或结尾)");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(!com.cgiser.moka.common.utils.StringUtils.checkUserNameValid(name)){
				logger.info("帮派名称不符合要求");
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("帮派名无效(由2-6个汉字,数字和26个英文字母或者下划线组成的字符串,不能以下划线开头或结尾)");
				super.printReturnType2Response(response, returnType);
				return null;
		    }
			LegionManager legionManager = (LegionManager)HttpSpringUtils.getBean("legionManager");
			if(legionManager.getLegionByName(name)!=null){
				returnType.setMsg("帮派已经存在了");
				returnType.setStatus(0);
				returnType.setValue(0L);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(legionManager.getLegioner(role.getRoleId())!=null){
				returnType.setMsg("您已经是帮派成员了");
				returnType.setStatus(0);
				returnType.setValue(0L);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(role.getLevel()<1){
				returnType.setMsg("您达到30级后才可以创建帮派哦!");
				returnType.setStatus(0);
				returnType.setValue(0L);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(role.getCash()<200){
				returnType.setMsg("您的元宝不够维持帮派哦!");
				returnType.setStatus(0);
				returnType.setValue(0L);
				super.printReturnType2Response(response, returnType);
				return null;
			}                                                                                                                                                                                                                                              
			
			Long legionid = legionManager.saveLegion(role,name, role.getRoleId(), "");
			if(legionid>0){
				AchievementManager achievementManager = (AchievementManager)HttpSpringUtils.getBean("achievementManager");
				UserAchievement userAchievement = achievementManager.getUserAchievementById(role.getRoleId(), 62);
				if(userAchievement==null){
					achievementManager.saveUserAchievement(62, role.getRoleId(),1);
				}
				returnType.setStatus(1);
				returnType.setValue(legionid);
			}else{
				returnType.setStatus(0);
				returnType.setValue(legionid);
			}
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
