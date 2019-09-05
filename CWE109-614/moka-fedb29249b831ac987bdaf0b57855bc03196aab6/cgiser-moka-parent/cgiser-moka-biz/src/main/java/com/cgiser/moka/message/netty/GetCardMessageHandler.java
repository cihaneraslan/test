package com.cgiser.moka.message.netty;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;

public class GetCardMessageHandler extends MessageHandler implements IHandler {
	@Override
	public void excute(Channel channel, ChannelBuffer buffer) {
		this.getMessageManager().sendMessageToAll(buffer);
	}
}
