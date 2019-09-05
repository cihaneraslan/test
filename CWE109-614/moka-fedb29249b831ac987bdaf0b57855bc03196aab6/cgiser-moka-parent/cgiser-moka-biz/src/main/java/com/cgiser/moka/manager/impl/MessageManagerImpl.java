package com.cgiser.moka.manager.impl;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;

import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.model.MessageType;
import com.cgiser.moka.model.Role;
import com.cgiser.sso.message.CommandManager;

public class MessageManagerImpl implements MessageManager {
	
	private CommandManager commandManager;
	@Override
	public void sendMessge(MessageType type,Object buffer) {
		// TODO Auto-generated method stub
		commandManager.getxSocketDataHandler().excudeCammand(type.getCode(), buffer);
	}
	public CommandManager getCommandManager() {
		return commandManager;
	}
	public void setCommandManager(CommandManager commandManager) {
		this.commandManager = commandManager;
	}
	@Override
	public void excute(int cmd, Channel channel, ChannelBuffer buffer) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void sendMessageToAll(ChannelBuffer buffer) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void sendMessageToRole(String roleName, ChannelBuffer buffer) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void bindRole(Role role, Channel channel) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void sendMessageToChannel(Channel channel, ChannelBuffer buffer) {
		// TODO Auto-generated method stub
		
	}

}
