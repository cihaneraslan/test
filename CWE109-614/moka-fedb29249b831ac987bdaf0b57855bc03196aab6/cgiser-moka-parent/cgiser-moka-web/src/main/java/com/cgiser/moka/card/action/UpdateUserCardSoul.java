package com.cgiser.moka.card.action;

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
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.UserCardManager;
import com.cgiser.moka.manager.UserSoulManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.StrengResult;
import com.cgiser.moka.model.UserCard;
import com.cgiser.moka.model.UserSoul;
import com.cgiser.moka.result.ReturnType;

public class UpdateUserCardSoul extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String userSoulId = ServletUtil.getDefaultValue(request, "userSoulId", null);
		String userCardId = ServletUtil.getDefaultValue(request, "userCardId", null);
		ReturnType<StrengResult> returnType = new ReturnType<StrengResult>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦,亲！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(userCardId==null||userSoulId==null||!StringUtils.isNumeric(userCardId)||!StringUtils.isNumeric(userSoulId)){
				returnType.setMsg("参数有误哦,亲！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			UserCardManager userCardManager = (UserCardManager)HttpSpringUtils.getBean("userCardManager");
			UserCard userCard= userCardManager.getUserCardById(new Long(userCardId));
			if(userCard==null){
				returnType.setMsg("您装备的卡牌不存在哦,亲！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			
			if(!userSoulId.equals("0")){
				UserSoulManager userSoulManager = (UserSoulManager)HttpSpringUtils.getBean("userSoulManager");
				UserSoul userSoul = userSoulManager.getUserSoulById(new Long(userSoulId));
				if(userSoul==null){
					returnType.setMsg("您装备的武器不存在哦,亲！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
			}
			if(!userSoulId.equals("0")){
				UserCard userCard2= userCardManager.GetUserCardByUserSoulId(role.getRoleId(),new Long(userSoulId));
				if(userCard2!=null){
					returnType.setMsg("该武器已被装备到其他的卡牌了哦,亲！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
			}
			if(userCardManager.ResetUserCardSoul(new Long(userCardId), new Long(userSoulId))>0){
				String[] freshStep = role.getFreshStep();
				if(Integer.parseInt(freshStep[0])<2){
					RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
					roleManager.updateFreshStep(0, 2, role.getRoleId());
				}
				returnType.setMsg("装备成功！");
				returnType.setStatus(1);
				super.printReturnType2Response(response, returnType);
				return null;
			}else{
				returnType.setMsg("装备失败！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
