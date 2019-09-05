package com.cgiser.moka.card.fragment.action;

import java.util.List;

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
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.AchievementManager;
import com.cgiser.moka.manager.UserCardFragment;
import com.cgiser.moka.manager.UserCardFragmentManager;
import com.cgiser.moka.manager.UserCardManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.UserAchievement;
import com.cgiser.moka.model.UserCard;
import com.cgiser.moka.result.ReturnType;

public class ResolveCardAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<List<UserCardFragment>> returnType = new ReturnType<List<UserCardFragment>>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			String userCardId = ServletUtil.getDefaultValue(request, "userCardId", "");
			if(!StringUtils.isNumeric(userCardId)){
				returnType.setMsg("参数有误哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			UserCardManager userCardManager = (UserCardManager)HttpSpringUtils.getBean("userCardManager");
			UserCard userCard = userCardManager.getUserCardById(new Long(userCardId));
			if(userCard==null){
				returnType.setMsg("您分解的卡牌不存在哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			UserCardFragmentManager userCardFragmentManager = (UserCardFragmentManager)HttpSpringUtils.getBean("userCardFragmentManager");
			List<UserCardFragment> userCardFragments = userCardFragmentManager.resolveCard(role.getRoleId(), new Long(userCardId));
			if(!CollectionUtils.isEmpty(userCardFragments)){
				AchievementManager achievementManager = (AchievementManager)HttpSpringUtils.getBean("achievementManager");
				UserAchievement userAchievement = achievementManager.getUserAchievementById(role.getRoleId(), 63);
				if(userAchievement==null){
					achievementManager.saveUserAchievement(63, role.getRoleId(),1);
				}
			}
			returnType.setStatus(1);
			returnType.setValue(userCardFragments);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
