package com.cgiser.moka.message.netty;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;

import com.cgiser.moka.manager.MessageManager;

public interface IHandler {
	public void excute(Channel channel,ChannelBuffer buffer);
	void setMessageManager(MessageManager messageManager);
	MessageManager getMessageManager();
	void writeString(ChannelBuffer buffer,String str);
	String readString(ChannelBuffer buffer);
	void writeString(ChannelBuffer buffer,String str,String charset);
	String readString(ChannelBuffer buffer,String charset);
}
