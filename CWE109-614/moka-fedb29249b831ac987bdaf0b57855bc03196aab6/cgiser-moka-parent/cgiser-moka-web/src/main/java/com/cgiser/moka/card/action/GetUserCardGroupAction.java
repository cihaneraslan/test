package com.cgiser.moka.card.action;

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
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.UserCardGroupManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.UserCardGroup;
import com.cgiser.moka.result.ReturnType;
import com.cgiser.moka.result.UserCardGroupResult;

public class GetUserCardGroupAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<UserCardGroupResult> returnType = new ReturnType<UserCardGroupResult>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			UserCardGroupManager userCardGroupManager = (UserCardGroupManager)HttpSpringUtils.getBean("userCardGroupManager");
			List<UserCardGroup> userCardGroups = userCardGroupManager.getUserCardGroup(role.getRoleId());
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			int effective = roleManager.getRoleEffective(role.getRoleId());
			UserCardGroupResult result =new UserCardGroupResult();
			result.setUserCardGroups(userCardGroups);
			result.setEffective(effective);
			returnType.setStatus(1);
			returnType.setValue(result);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
