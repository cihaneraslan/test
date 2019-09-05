package com.cgiser.moka.message.netty;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.Channel;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.manager.RoleManager;

public class HertBeatHandler extends MessageHandler implements IHandler {
	
	@Override
	public void excute(Channel channel, ChannelBuffer buffer) {
		String user = this.readString(buffer,"UTF-8");
		if (StringUtils.isEmpty(user)) {
			return;
		}
		RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
		this.getMessageManager().bindRole(roleManager.getRoleByName(user), channel);
		String str = "heartbeat";
		ChannelBuffer responseBuffer = new DynamicChannelBuffer(4 + 4 + str
				.getBytes().length);
		responseBuffer.writeInt(4 + str.getBytes().length);
		responseBuffer.writeInt(1020);
		responseBuffer.writeBytes(str.getBytes());
		
		channel.write(responseBuffer);
	}

}
