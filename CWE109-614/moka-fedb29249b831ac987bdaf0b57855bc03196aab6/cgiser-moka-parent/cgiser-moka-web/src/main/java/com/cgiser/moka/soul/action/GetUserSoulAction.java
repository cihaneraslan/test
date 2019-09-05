package com.cgiser.moka.soul.action;

import java.util.List;

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
import com.cgiser.moka.manager.UserCardManager;
import com.cgiser.moka.manager.UserSoulManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.UserSoul;
import com.cgiser.moka.result.ReturnType;

public class GetUserSoulAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<List<UserSoul>> returnType = new ReturnType<List<UserSoul>>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			UserSoulManager userSoulManager = (UserSoulManager)HttpSpringUtils.getBean("userSoulManager");
			List<UserSoul> souls= userSoulManager.getUserSoulByRoleId(role.getRoleId());
			if(!CollectionUtils.isEmpty(souls)){
				UserCardManager userCardManager = (UserCardManager)HttpSpringUtils.getBean("userCardManager");
				for(UserSoul userSoul:souls){
					if(userCardManager.GetUserCardByUserSoulId(role.getRoleId(), userSoul.getUserSoulId())!=null){
						userSoul.setIsEquipment(1);
					}
				}

			}
			returnType.setStatus(1);
			returnType.setValue(souls);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
