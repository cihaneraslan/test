package com.cgiser.moka.message.netty;

import java.util.HashMap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.core.common.cache.mem.MemCachedManager;
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.MessageType;
import com.cgiser.moka.model.Role;

public class MessageManagerImpl implements MessageManager {
	private HashMap<Integer, IHandler> map = new HashMap<Integer, IHandler>();
	private Logger logger = LoggerFactory.getLogger(MessageManagerImpl.class);
	private MemCachedManager roleCachedManager;
	private RoleManager roleManager;
	public MessageManagerImpl(){
		try {
			this.registerHandler(1001, WorldMessageHandler.class);
			this.registerHandler(1002, SingleMessageHandler.class);
			this.registerHandler(1003, GroupMessageHandler.class);
			this.registerHandler(1020,HertBeatHandler.class);
			this.registerHandler(1000, LoginHandler.class);
			this.registerHandler(1004, FightMessageHandler.class);
			this.registerHandler(1005,GetCardMessageHandler.class);
		} catch (Exception e) {
			logger.equals("注册消息处理类异常"+e.getMessage());
		}
	}

	public void registerHandler(int cmd, Class HandlerClass) throws Exception {
		IHandler handler = (IHandler) HandlerClass.newInstance();
		map.put(cmd, handler);
		handler.setMessageManager(this);
	}

	public void unregisterHandler(int cmd) {
		map.remove(cmd);
	}

	@Override
	public void sendMessge(MessageType type, Object buffer) {
		if(map.containsKey(type.getCode())){
			map.get(type.getCode()).excute(null, (ChannelBuffer)buffer);
		}else{
			logger.error("消息处理Handler"+type.getCode()+"不存在");
		}
	}

	@Override
	public void excute(int cmd, Channel channel, ChannelBuffer buffer) {
		if(map.containsKey(cmd)){
			map.get(cmd).excute(channel, buffer);
		}else{
			logger.error("消息处理Handler"+cmd+"不存在");
		}
	}

	@Override
	public void sendMessageToAll(ChannelBuffer buffer) {
		byte[] recByte=buffer.copy().toByteBuffer().array();
		ChannelBuffer responseBuffer = new DynamicChannelBuffer(4+recByte.length);
		responseBuffer.writeInt(recByte.length);
		responseBuffer.writeBytes(recByte);
		ReceiverHandler.allChannels.write(responseBuffer);
	}

	@Override
	public void sendMessageToRole(String roleName, ChannelBuffer buffer) {
		byte[] recByte=buffer.copy().toByteBuffer().array();
		ChannelBuffer responseBuffer = new DynamicChannelBuffer(4+recByte.length);
		responseBuffer.writeInt(recByte.length);
		responseBuffer.writeBytes(recByte);
    	Role role = roleManager.getRoleByName(roleName);
		Channel channel = ReceiverHandler.allChannels.getChannelByRole(role);
		if(channel!=null&&channel.isOpen()&&channel.isWritable()){
			channel.write(responseBuffer);
		}
	}
	
	@Override
	public void bindRole(Role role,Channel channel) {
		ReceiverHandler.allChannels.bindChannel(role, channel);
	}

	public MemCachedManager getRoleCachedManager() {
		return roleCachedManager;
	}

	public void setRoleCachedManager(MemCachedManager roleCachedManager) {
		this.roleCachedManager = roleCachedManager;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	@Override
	public void sendMessageToChannel(Channel channel, ChannelBuffer buffer) {
		byte[] recByte=buffer.copy().toByteBuffer().array();
		ChannelBuffer responseBuffer = new DynamicChannelBuffer(4+recByte.length);
		responseBuffer.writeInt(recByte.length);
		responseBuffer.writeBytes(recByte);
		channel.write(responseBuffer);
		
	}
}
