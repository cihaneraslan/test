package com.cgiser.moka.soul.action;

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
import com.cgiser.moka.manager.SoulManager;
import com.cgiser.moka.manager.UserSoulManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.Soul;
import com.cgiser.moka.result.ReturnType;

public class ExChangeSoulAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<Integer> returnType = new ReturnType<Integer>();
		try{
			String strSoulId = ServletUtil.getDefaultValue(request, "soulId", null); 
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(null==strSoulId){
				returnType.setMsg("参数不正确哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(!StringUtils.isNumeric(strSoulId)){
				returnType.setMsg("参数不正确哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			int soulId = Integer.parseInt(strSoulId);
			SoulManager soulManager = (SoulManager)HttpSpringUtils.getBean("soulManager");
			Soul soul = soulManager.getSoulById(soulId);
			if(role.getHonor()<soul.getHonor()){
				returnType.setMsg("您的荣誉点不够哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			if(roleManager.updateHonor(role.getRoleId(), soul.getHonor())){
				UserSoulManager userSoulManager = (UserSoulManager)HttpSpringUtils.getBean("userSoulManager");
				if(userSoulManager.saveUserSoul(role.getRoleId(), soulId, 1)<1){
					logger.error("扣除用户荣誉点成功，给用户增加武器失败，武器ID为："+soul.getSoulId());
				}
			}else{
				logger.error("扣除用户荣誉点失败");
			}
			returnType.setStatus(1);
			returnType.setValue(soul.getHonor());
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
