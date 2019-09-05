package com.cgiser.moka.legion.action;

import java.util.HashMap;
import java.util.Map;

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
import com.cgiser.moka.manager.CardManager;
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.UserCardGroupManager;
import com.cgiser.moka.manager.support.BeanUtils;
import com.cgiser.moka.model.Card;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionContext;
import com.cgiser.moka.model.LegionGuard;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.UserCard;
import com.cgiser.moka.model.UserCardGroup;
import com.cgiser.moka.result.LegionGuardResult;
import com.cgiser.moka.result.ReturnType;

public class GetLegionGuardAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<LegionGuardResult> returnType = new ReturnType<LegionGuardResult>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			LegionManager legionManager = (LegionManager)HttpSpringUtils.getBean("legionManager");
			Legioner legioner = legionManager.getLegioner(role.getRoleId());
			if(legioner==null){
				returnType.setMsg("您还没加入帮派哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			Legion legion  = legionManager.getLegionById(legioner.getLegionId());
			if(legion==null){
				returnType.setMsg("帮派不存在！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			int guardNum = legion.getLegionLevel() > 5 ? 9 : 5;
			Map<String, LegionGuard> map = new HashMap<String, LegionGuard>();
			Role aRole = null;
			LegionGuard legionGuard;
			RoleManager roleManager = (RoleManager) HttpSpringUtils
					.getBean("roleManager");
			CardManager cardManager = (CardManager)HttpSpringUtils.getBean("cardManager");
			UserCardGroupManager userCardGroupManager = (UserCardGroupManager)HttpSpringUtils.getBean("userCardGroupManager");
			Long guardId;
			Legioner aLegioner;
			Map<String, Integer> stars;
			int[] LegionGuardCoins = guardNum>5?LegionContext.LegionGuardCoins69:LegionContext.LegionGuardCoins05;
			for (int i = 1; i <= guardNum; i++) {
				guardId = (Long)BeanUtils.getFieldValueByName("guard" + i,
						legion);
				if (guardId > 0) {
					aRole = roleManager.getRoleById(guardId);
					legionGuard = new LegionGuard();
					legionGuard.setCoins(LegionGuardCoins[i-1]+"%");
					legionGuard.setAvatar(aRole.getAvatar());
					legionGuard.setSex(aRole.getSex());
					legionGuard.setEffective(roleManager.getRoleEffective(aRole.getRoleId()));
					aLegioner = legionManager.getLegioner(aRole.getRoleId());
					legionGuard.setLegionerId(aLegioner.getId());
					legionGuard.setRoleId(aRole.getRoleId());
					legionGuard.setRoleName(aRole.getRoleName());
					stars = new HashMap<String, Integer>();
					stars.put("star1", 0);
					stars.put("star2", 0);
					stars.put("star3", 0);
					stars.put("star4", 0);
					stars.put("star5", 0);
					Card card = null;
					UserCardGroup userCardGroup = userCardGroupManager.getUserCardGroupById(new Long(aRole.getDefaultGroupId()));
					if(!CollectionUtils.isEmpty(userCardGroup.getUserCardInfo())){
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
					legionGuard.setStars(stars);
					map.put("guard"+i, legionGuard);
				} else {
					legionGuard = new LegionGuard();
					legionGuard.setCoins(LegionGuardCoins[i-1]+"%");
					legionGuard.setRoleName("此处无守卫");
					map.put("guard"+i, legionGuard);
				}
			}
			LegionGuardResult result = new LegionGuardResult();
			result.setLegionId(legion.getId());
			result.setLegionName(legion.getName());
			result.setLevel(legion.getLegionLevel());
			result.setGuards(map);
			returnType.setStatus(1);
			returnType.setValue(result);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
