package com.cgiser.moka.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.UserCardManager;
import com.cgiser.moka.manager.UserRuneManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.UserCard;
import com.cgiser.moka.model.UserRune;
import com.cgiser.moka.result.ReturnType;

public class SaveFreshStepAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<String> returnType = new ReturnType<String>();
		try{
			String type = ServletUtil.getDefaultValue(request, "type", null); 
			String value = ServletUtil.getDefaultValue(request, "step", null); 
			Role role = this.getCurrentRole(request);
			if(role==null){
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("您还没登录哦");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(type==null||value==null){
				returnType.setValue(null);
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			UserCardManager userCardManager = (UserCardManager)HttpSpringUtils.getBean("userCardManager");
			UserRuneManager userRuneManager = (UserRuneManager)HttpSpringUtils.getBean("userRuneManager");
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			int flag = roleManager.updateFreshStep(Integer.parseInt(type), Integer.parseInt(value), role.getRoleId());
			if(flag>0){
				if(type.equals("2")){
					List<UserCard> userCards = userCardManager.getUserCard(role.getRoleId());
					Long cardId = 0L;
					for(UserCard userCard:userCards){
						if(userCard.getCardId()==3){
							cardId = userCard.getUserCardId();
						}
					}
					returnType.setValue(cardId.toString());
					
				}else if(type.equals("3")&&Integer.parseInt(value)<=5){
					//炼化解锁给卡牌
					Long userCardId = userCardManager.saveUserCard(62, role.getRoleId());
					Long userCardId1 = userCardManager.saveUserCard(1001, role.getRoleId());
					roleManager.addCoin(role.getRoleName(), 10000);
					roleManager.updateFreshStep(Integer.parseInt(type), 5, role.getRoleId());
					returnType.setValue(userCardId+"_"+userCardId1+"_"+10000);
				}else if(type.equals("4")&&Integer.parseInt(value)==5){
					List<UserRune> userRunes = userRuneManager.getUserRune(role.getRoleId());
					Long cardId = 0L;
					for(UserRune userRune:userRunes){
						if(userRune.getRuneId()==1){
							cardId = userRune.getUserRuneId();
						}
					}
					returnType.setValue(cardId.toString());
				}else if(type.equals("5")&&Integer.parseInt(value)==5){
					roleManager.addCoin(role.getRoleName(), 6000);
					roleManager.updateFreshStep(Integer.parseInt(type), 6, role.getRoleId());
					returnType.setValue(""+6000);
				}else{
					returnType.setValue(null);
				}
				returnType.setStatus(1);
				super.printReturnType2Response(response, returnType);
			}else{
				returnType.setValue(null);
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
			}
			return null;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
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
