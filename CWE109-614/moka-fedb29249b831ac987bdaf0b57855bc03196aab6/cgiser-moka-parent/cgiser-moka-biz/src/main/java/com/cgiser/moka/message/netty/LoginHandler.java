package com.cgiser.moka.message.netty;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.Channel;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.MessageType;
import com.cgiser.moka.model.Role;
import com.cgiser.sso.model.User;

public class LoginHandler extends MessageHandler implements IHandler {
	@Override
	public void excute(Channel channel, ChannelBuffer buffer) {
		String user = this.readString(buffer,"UTF-8");
		if (StringUtils.isEmpty(user)) {
			return;
		}
		RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
		roleManager.updateRoleStatus(user, 0);
		Role role = roleManager.getRoleByName(user);
		User user1 = roleManager.getUserByUserIden(role.getUserIden());
		if(user!=null&&(user1.getEmail()==null||user1.getEmail().equals(""))){
			ChannelBuffer buffer1 = new DynamicChannelBuffer(200);
			buffer1.writeInt(MessageType.SYSTEM.getCode());
			buffer1.writeInt(MessageType.MATCHSTART.getCode());
			MessageUtil.writeString(buffer1, "您还没绑定邮箱，绑定邮箱既送四星卡牌，还可以用邮箱登陆游戏哦！", "UTF-8");
			this.getMessageManager().sendMessageToChannel(channel, buffer1);
		}
		this.getMessageManager().bindRole(role, channel);
	}
}
