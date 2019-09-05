package com.cgiser.moka.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.FriendsManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Friend;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class DelFriendInvitAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String roleName1 = ServletUtil.getDefaultValue(request, "role1",
		"");
		String roleName2 = ServletUtil.getDefaultValue(request, "role2",
		"");
		ReturnType<Object> returnType = new ReturnType<Object>();
		try{
			if(StringUtils.isBlank(roleName1)||StringUtils.isBlank(roleName2)){
				logger.info("删除好友申请失败");
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("删除好友申请失败");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			Role role1 = roleManager.getRoleByName(roleName1);
			Role role2 = roleManager.getRoleByName(roleName2);
			
			if(role1==null||role2==null){
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			FriendsManager friendsManager = (FriendsManager)HttpSpringUtils.getBean("friendsManager");
			Long id = friendsManager.delFriendInvite(roleName1, roleName2);
			if(id >0){
				List<Friend> friends = friendsManager.getFriendsInviteByRoleName(roleName2);
				if(!CollectionUtils.isEmpty(friends)){
					roleManager.updateRoleFriendApply(role2.getRoleId(), friends.size());
				}else{
					roleManager.updateRoleFriendApply(role2.getRoleId(), 0);
				}
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
