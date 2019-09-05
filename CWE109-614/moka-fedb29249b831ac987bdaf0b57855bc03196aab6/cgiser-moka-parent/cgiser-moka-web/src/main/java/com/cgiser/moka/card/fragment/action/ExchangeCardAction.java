package com.cgiser.moka.card.fragment.action;

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
import com.cgiser.moka.manager.CardFragmentManager;
import com.cgiser.moka.manager.CardManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.UserCardFragment;
import com.cgiser.moka.manager.UserCardFragmentManager;
import com.cgiser.moka.model.Card;
import com.cgiser.moka.model.CardFragment;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.UserAchievement;
import com.cgiser.moka.result.ReturnType;

public class ExchangeCardAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<Card> returnType = new ReturnType<Card>();
		try{
			String cardId = ServletUtil.getDefaultValue(request, "cardId", "");
			String useUniversal = ServletUtil.getDefaultValue(request, "useUniversal", "");
			String num = ServletUtil.getDefaultValue(request, "num", "");
			
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(!StringUtils.isNumeric(cardId)){
				returnType.setMsg("您合成的卡牌不存在哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(useUniversal!=null){
				if(useUniversal.equals("1")||useUniversal.equals("2")){
					if(!StringUtils.isNumeric(num)){
						returnType.setMsg("参数不合法哦！");
						returnType.setStatus(0);
						super.printReturnType2Response(response, returnType);
						return null;
					}
				}else{
					returnType.setMsg("参数不合法哦！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			UserCardFragmentManager userCardFragmentManager = (UserCardFragmentManager)HttpSpringUtils.getBean("userCardFragmentManager");
			CardFragmentManager cardFragmentManager = (CardFragmentManager)HttpSpringUtils.getBean("cardFragmentManager");
			CardFragment cardFragment = cardFragmentManager.getCardFragmentByCardId(Integer.parseInt(cardId));
			if(cardFragment==null){
				returnType.setMsg("您兑换的碎片不存在哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			CardManager cardManager = (CardManager)HttpSpringUtils.getBean("cardManager");
			Card card = cardManager.getCardById(new Long(cardFragment.getCardId()));
			if(card==null){
				returnType.setMsg("您合成的卡牌不存在哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			UserCardFragment userCardFragment = userCardFragmentManager.getUserFragment(role.getRoleId(), cardFragment.getId());
			if(userCardFragment==null){
				returnType.setMsg("您的碎片已经用完了！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(userCardFragment.getNum()<cardFragment.getNum()){
				if(useUniversal==null){
					useUniversal = "1";
				}
				if((Integer.parseInt(num)+userCardFragment.getNum())<cardFragment.getNum()){
					returnType.setMsg("您的碎片不够！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
			}else{
				if(useUniversal==null){
					useUniversal = "0";
				}
			}
			if(role.getCoins()<card.getPrice()*2){
				returnType.setMsg("合成卡牌所需的铜钱不够！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(!roleManager.updateCoin(role.getRoleName(), card.getPrice()*2)){
				returnType.setMsg("合成卡牌所需的铜钱不够！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(userCardFragmentManager.exchangeCard(role.getRoleId(), Integer.parseInt(cardId), Integer.parseInt(useUniversal),Integer.parseInt(num))>0){
				AchievementManager achievementManager = (AchievementManager)HttpSpringUtils.getBean("achievementManager");
				UserAchievement userAchievement = achievementManager.getUserAchievementById(role.getRoleId(), 64);
				if(userAchievement==null){
					achievementManager.saveUserAchievement(64, role.getRoleId(),1);
				}
				returnType.setStatus(1);
				returnType.setValue(cardManager.getCardById(new Long(cardId)));
				super.printReturnType2Response(response, returnType);
				return null;
			}
			returnType.setMsg("合成失败！");
			returnType.setStatus(0);
			super.printReturnType2Response(response, returnType);
			return null;
			
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
