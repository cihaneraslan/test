package com.cgiser.moka.card.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.UserCardGroupManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.UserCardGroup;
import com.cgiser.moka.result.ReturnType;

public class AddCardGroupAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String groupName = ServletUtil.getDefaultValue(request, "groupName", null);
		ReturnType<UserCardGroup> returnType = new ReturnType<UserCardGroup>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(null==groupName){
				returnType.setMsg("名称不能为空");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(role.getLevel()<36){
				returnType.setMsg("36级以后才能增加新的卡组");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(role.getCash()<100){
				returnType.setMsg("增加新的卡组所需要的元宝不够");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			if(roleManager.updateCash(role.getRoleName(), 100)){
				UserCardGroupManager userCardGroupManager = (UserCardGroupManager)HttpSpringUtils.getBean("userCardGroupManager");
				Long userCardGroupId = userCardGroupManager.createUserCardGroup(role.getRoleId(), groupName, "", "");
				if(userCardGroupId>0){
					returnType.setStatus(1);
					returnType.setValue(userCardGroupManager.getUserCardGroupById(userCardGroupId));
					super.printReturnType2Response(response, returnType);
					return null;
				}else{
					returnType.setMsg("增加新的卡组失败");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
			}else{
				returnType.setMsg("增加新的卡组失败");
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
