package com.cgiser.moka.message.netty;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.Channel;

public class WorldMessageHandler extends MessageHandler implements IHandler {

	@Override
	public void excute(Channel channel, ChannelBuffer buffer) {

		String user = this.readString(buffer,"UTF-8");
		String content = this.readString(buffer,"UTF-8");
		if (StringUtils.isEmpty(user) || StringUtils.isEmpty(content)) {
			return;
		}
		ChannelBuffer responseBuffer = new DynamicChannelBuffer(200);
		responseBuffer.writeInt(1001);
		this.writeString(responseBuffer, user,"UTF-8");
		this.writeString(responseBuffer, content,"UTF-8");
		this.getMessageManager().sendMessageToAll(responseBuffer);
	}

}
