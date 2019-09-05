package com.cgiser.moka.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.FriendsManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class DelFriendAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String roleName = ServletUtil.getDefaultValue(request, "role",
		"");
		ReturnType<Object> returnType = new ReturnType<Object>();
		try{
			Role role = super.getCurrentRole(request);
			if(role==null){
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("您还没登录哦!");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(StringUtils.isBlank(roleName)){
				logger.info("删除好友失败");
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("删除好友失败");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			Role role1 = roleManager.getRoleByName(roleName);
			
			if(role1==null){
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			FriendsManager friendsManager = (FriendsManager)HttpSpringUtils.getBean("friendsManager");
			Long id = friendsManager.delFriendInvite(role.getRoleName(), roleName);
			Long id1 = friendsManager.delFriendInvite(roleName, role.getRoleName());
			if(id >0||id1>0){
				returnType.setValue(null);
				returnType.setStatus(1);
				returnType.setMsg("");
				super.printReturnType2Response(response, returnType);
			}else{
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("");
				super.printReturnType2Response(response, returnType);
			}
			return null;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
