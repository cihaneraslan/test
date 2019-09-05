package com.cgiser.moka.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

public class InvitFriendAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String roleName1 = ServletUtil.getDefaultValue(request, "role1",
		"");
		String roleName2 = ServletUtil.getDefaultValue(request, "role2",
		"");
		ReturnType<Object> returnType = new ReturnType<Object>();
		try{
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			Role role1 = roleManager.getRoleByName(roleName1);
			Role role2 = roleManager.getRoleByName(roleName2);
			if(role1==null||role2==null){
				logger.error(roleName1+"加好友"+roleName2+"失败");
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("好友不存在");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			FriendsManager friendsManager = (FriendsManager)HttpSpringUtils.getBean("friendsManager");
			if(friendsManager.isFriend(roleName1, roleName2)){
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("你们已经是好友了，再加就是好基友了！");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			List<Friend> friends1 = friendsManager.getFriendsByRoleName(roleName1);
			if(!CollectionUtils.isEmpty(friends1)&&friends1.size()>=role1.getFriendNumMax()){
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("你的好友数量已经到达上限，赶快去升级吧！");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(friendsManager.isInvitFriend(roleName2, roleName1)){
/*				Long id = friendsManager.applyFriendInvite(roleName2, roleName1);
				if(id >0){
					returnType.setValue(null);
					returnType.setStatus(1);
					returnType.setMsg("给"+roleName2+"发好友邀请成功");
					super.printReturnType2Response(response, returnType);
					logger.debug(roleName1+"给"+roleName2+"发好友邀请成功");
					return null;
				}else{
					returnType.setValue(null);
					returnType.setStatus(0);
					returnType.setMsg("邀请失败！");
					super.printReturnType2Response(response, returnType);
					return null;
				}*/
				returnType.setValue(null);
				returnType.setStatus(2);
				returnType.setMsg(roleName2+"已经邀请您成为他的好友,是否直接加为好友.");
				super.printReturnType2Response(response, returnType);
				logger.debug(roleName1+"给"+roleName2+"发好友邀请成功");
				return null;
			}
			if(friendsManager.isInvitFriend(roleName1, roleName2)){
				List<Friend> friends = friendsManager.getFriendsInviteByRoleName(roleName2);
				if(!CollectionUtils.isEmpty(friends)){
					roleManager.updateRoleFriendApply(role2.getRoleId(), friends.size());
				}
				returnType.setValue(null);
				returnType.setStatus(1);
				returnType.setMsg("给"+roleName2+"发好友邀请成功");
				super.printReturnType2Response(response, returnType);
				ChannelBuffer buffer1 = new DynamicChannelBuffer(200);
				buffer1.writeInt(MessageType.SYSTEM.getCode());
				buffer1.writeInt(MessageType.MATCHSTART.getCode());
				MessageManager messageManager = (MessageManager)HttpSpringUtils.getBean("messageManager");
				String sex = role1.getSex()==0?"他":"她";
				MessageUtil.writeString(buffer1, "["+roleName1+"]邀请您成为"+sex+"的好友，赶紧加"+sex+"为好友吧！", "UTF-8");
				messageManager.sendMessageToRole(roleName2, buffer1);
				return null;
			}
			Long id = friendsManager.inviteFriend(roleName1, roleName2);
			if(id >0){
				List<Friend> friends = friendsManager.getFriendsInviteByRoleName(roleName2);
				if(!CollectionUtils.isEmpty(friends)){
					roleManager.updateRoleFriendApply(role2.getRoleId(), friends.size());
				}
				returnType.setValue(null);
				returnType.setStatus(1);
				returnType.setMsg("给"+roleName2+"发好友邀请成功");
				super.printReturnType2Response(response, returnType);
				logger.debug(roleName1+"给"+roleName2+"发好友邀请成功");
				ChannelBuffer buffer1 = new DynamicChannelBuffer(200);
				buffer1.writeInt(MessageType.SYSTEM.getCode());
				buffer1.writeInt(MessageType.MATCHSTART.getCode());
				MessageManager messageManager = (MessageManager)HttpSpringUtils.getBean("messageManager");
				String sex = role1.getSex()==0?"他":"她";
				MessageUtil.writeString(buffer1, "["+roleName1+"]邀请您成为"+sex+"的好友，赶紧加"+sex+"为好友吧！", "UTF-8");
				messageManager.sendMessageToRole(roleName2, buffer1);
				return null;
			}else{
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("邀请失败！");
				super.printReturnType2Response(response, returnType);
				return null;
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
		
	}
}
