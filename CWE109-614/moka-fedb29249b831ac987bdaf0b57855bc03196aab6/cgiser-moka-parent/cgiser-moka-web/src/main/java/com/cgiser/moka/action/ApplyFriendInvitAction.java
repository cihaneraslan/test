package com.cgiser.moka.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.FriendsManager;
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.message.netty.MessageUtil;
import com.cgiser.moka.model.Friend;
import com.cgiser.moka.model.MessageType;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class ApplyFriendInvitAction extends AbstractAction {
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
				logger.info("加好友失败");
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("加好友失败");
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
			List<Friend> friends1 = friendsManager.getFriendsByRoleName(roleName2);
			if(!CollectionUtils.isEmpty(friends1)&&friends1.size()>=role2.getFriendNumMax()){
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("你的好友数量已经到达上限，赶快去升级吧！");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			Long id = friendsManager.applyFriendInvite(roleName1, roleName2);
			if(id >0){
				List<Friend> friends = friendsManager.getFriendsInviteByRoleName(roleName2);
				if(!CollectionUtils.isEmpty(friends)){
					roleManager.updateRoleFriendApply(role2.getRoleId(), friends.size());
				}else{
					roleManager.updateRoleFriendApply(role2.getRoleId(), 0);
				}
				ChannelBuffer buffer1 = new DynamicChannelBuffer(200);
				buffer1.writeInt(MessageType.SYSTEM.getCode());
				buffer1.writeInt(MessageType.MATCHSTART.getCode());
				MessageManager messageManager = (MessageManager)HttpSpringUtils.getBean("messageManager");
				String sex = role2.getSex()==0?"他":"她";
				MessageUtil.writeString(buffer1, "["+roleName2+"]同意了您的好友申请，赶紧与"+sex+"切磋赢取体力吧！", "UTF-8");
				messageManager.sendMessageToRole(roleName1, buffer1);
				
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
