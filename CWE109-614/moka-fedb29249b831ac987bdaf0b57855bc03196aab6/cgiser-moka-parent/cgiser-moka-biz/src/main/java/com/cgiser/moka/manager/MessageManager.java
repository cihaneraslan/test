package com.cgiser.moka.manager;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;

import com.cgiser.moka.model.MessageType;
import com.cgiser.moka.model.Role;

public interface MessageManager {
	public void sendMessge(MessageType type,Object buffer);
	public void excute(int cmd,Channel channel,ChannelBuffer buffer);
	public void sendMessageToAll(ChannelBuffer buffer);
	public void sendMessageToRole(String roleName,ChannelBuffer buffer);
	public void sendMessageToChannel(Channel channel,ChannelBuffer buffer);
	public void bindRole(Role role,Channel chnnel);
}
