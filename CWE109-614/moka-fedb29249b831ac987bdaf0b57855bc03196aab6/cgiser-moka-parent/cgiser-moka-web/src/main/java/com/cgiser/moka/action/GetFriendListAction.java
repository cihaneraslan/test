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

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.FriendsManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Friend;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class GetFriendListAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String roleName = ServletUtil.getDefaultValue(request, "role",
		"");
		ReturnType<List<Friend> > returnType = new ReturnType<List<Friend>>();
		RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
		Role role = roleManager.getRoleByName(roleName);
		try{
			if(StringUtils.isBlank(roleName)){
				logger.info("获取好友列表失败");
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("获取好友列表失败");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(role==null){
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("好友数为0");
				super.printReturnType2Response(response, returnType);
				logger.error("获取"+roleName+"好友列表失败");
				return null;
			}
			FriendsManager friendsManager = (FriendsManager)HttpSpringUtils.getBean("friendsManager");
			List<Friend> friends = friendsManager.getFriendsByRoleName(roleName);
			if(friends==null||friends.size()==0){
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("好友数为0");
				super.printReturnType2Response(response, returnType);
			}else{
				returnType.setValue(friends);
				returnType.setStatus(1);
				super.printReturnType2Response(response, returnType);
			}
			return null;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		try{
			returnType.setValue(null);
			returnType.setStatus(0);
			returnType.setMsg("好友数为0");
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);// TODO: handle exception
		}
		return null;
		
	}
}
