package com.cgiser.moka.fight.match.action;

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
import com.cgiser.moka.manager.MatchGameManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class FindMatchPlayerAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<String> returnType = new ReturnType<String>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
//			String strType = ServletUtil.getDefaultValue(request, "type", "");
//			if(StringUtils.isEmpty(strType)||!StringUtils.isNumeric(strType)){
//				returnType.setMsg("参数有误哦！");
//				returnType.setStatus(0);
//				super.printReturnType2Response(response, returnType);
//				return null;
//			}
//			int type = Integer.parseInt(strType);
//			if(type>3){
//				returnType.setMsg("参数有误哦！");
//				returnType.setStatus(0);
//				super.printReturnType2Response(response, returnType);
//				return null;
//			}
			int type = 1;
			if(role.getLevel()<15){
				returnType.setMsg("您的级别不够！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(role.getLevel()>=15&&role.getLevel()<30){
				type = 1;
			}else if(role.getLevel()>=30&&role.getLevel()<45){
				type = 2;
			}else{
				type = 3;
			}
			MatchGameManager matchGameManager = (MatchGameManager)HttpSpringUtils.getBean("matchGameManager");
			String roleName = matchGameManager.findMatchPlayer(role,type);
			if(StringUtils.isEmpty(roleName)){
				returnType.setStatus(0);
			}else{
				returnType.setStatus(1);
				returnType.setValue(roleName);
			}

			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	} 
}
