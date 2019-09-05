package com.cgiser.moka.message.netty;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.Channel;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Role;

public class FightMessageHandler extends MessageHandler implements IHandler {
	@Override
	public void excute(Channel channel, ChannelBuffer buffer) {
		String roleName1 = this.readString(buffer, "UTF-8");
		String roleName2 = this.readString(buffer, "UTF-8");
		int num = buffer.readInt();
		if (StringUtils.isEmpty(roleName1) || StringUtils.isEmpty(roleName2)) {
			return;
		}
		ChannelBuffer responseBuffer = new DynamicChannelBuffer(200);
		responseBuffer.writeInt(1004);
		this.writeString(responseBuffer, roleName1, "UTF-8");
		this.writeString(responseBuffer, roleName2, "UTF-8");
		responseBuffer.writeInt(num);
		if(num==1){
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			Role role = roleManager.getRoleByName(roleName1);
			int avatar = role.getAvatar();
			if(avatar==0){
				avatar = role.getSex()==0?10000:10001;
			}
			String info = avatar+"_"+role.getHP()+"_"+role.getLevel();
			this.writeString(responseBuffer, info, "UTF-8");
		}
		this.getMessageManager().sendMessageToRole(roleName2,responseBuffer);
	}
}
