package com.cgiser.moka.message.netty;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.Channel;

public class SingleMessageHandler extends MessageHandler implements IHandler {
	@Override
	public void excute(Channel channel, ChannelBuffer buffer) {
		String roleName1 = this.readString(buffer, "UTF-8");
		String roleName2 = this.readString(buffer, "UTF-8");
		if (StringUtils.isEmpty(roleName1) || StringUtils.isEmpty(roleName2)) {
			return;
		}
		String content = this.readString(buffer, "UTF-8");
		ChannelBuffer responseBuffer = new DynamicChannelBuffer(200);
		responseBuffer.writeInt(1002);
		this.writeString(responseBuffer, roleName1, "UTF-8");
		this.writeString(responseBuffer, roleName2, "UTF-8");
		responseBuffer.writeInt(1);
		this.writeString(responseBuffer, content, "UTF-8");
		this.getMessageManager().sendMessageToRole(roleName1,responseBuffer.copy());
		this.getMessageManager().sendMessageToRole(roleName2,responseBuffer);
	}
}
