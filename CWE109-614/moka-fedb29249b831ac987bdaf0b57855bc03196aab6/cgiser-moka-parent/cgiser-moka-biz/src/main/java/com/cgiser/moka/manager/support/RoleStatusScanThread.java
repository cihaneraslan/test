package com.cgiser.moka.manager.support;

import java.util.concurrent.ConcurrentMap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.moka.manager.FightManager;
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.message.netty.MessageUtil;
import com.cgiser.moka.message.netty.ReceiverHandler;
import com.cgiser.moka.model.FightResult;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.Room;

public class RoleStatusScanThread extends Thread {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private RoleManager roleManager;
	private MessageManager messageManager;
	private FightManager fightManager;
	public static ConcurrentMap<String, Room> freeFightRoom = new ConcurrentHashMap<String, Room>();
	public RoleStatusScanThread(){
	}
	@Override
	public void run() {
		ConcurrentMap<String, Integer> roleChannel = ReceiverHandler.allChannels.getroleChannel();
		ConcurrentMap<Integer, String> channelRole = ReceiverHandler.allChannels.getchannelRole();
		Channel channel;
		Room room;
		String aPlayer;
		String dPlayer;
		Role aRole= null;
		Role dRole = null;
		while(true){
			try{
				for(String roleName:roleChannel.keySet()){
					channel = ReceiverHandler.allChannels.getChannelByRoleName(roleName);
					if(channel!=null){
						if(!channel.isConnected()){
							channel.close();
							if(channelRole.containsKey(roleChannel.get(roleName))){
								channelRole.remove(roleChannel.get(roleName));
							}
							roleChannel.remove(roleName);
							roleManager.updateRoleStatus(roleName, 3);
						}else{
							Role role = (Role)roleManager.getRoleByName(roleName);
							if(role.getStatus()==3){
								roleManager.updateRoleStatus(roleName, 0);
							}
						}
					}else{
						channelRole.remove(roleChannel.get(roleName));
						roleChannel.remove(roleName);
						roleManager.updateRoleStatus(roleName, 3);
					}
				}
				for(String battleId:freeFightRoom.keySet()){
					room = freeFightRoom.get(battleId);
					FightResult result = fightManager.getFight(battleId);
					if(result==null||result.getWin()!=0){
						freeFightRoom.remove(battleId);
					}else{
						if(result.getType()==1||result.getType()==0){
							aPlayer = room.getaPlayer();
							dPlayer = room.getdPlayer();
							aRole = roleManager.getRoleByName(aPlayer);
							dRole = roleManager.getRoleByName(dPlayer);
							if(aRole.getStatus()==3&&dRole.getStatus()!=3){
								ChannelBuffer responseBuffer = new DynamicChannelBuffer(200);
								responseBuffer.writeInt(1004);
								MessageUtil.writeString(responseBuffer, dPlayer, "UTF-8");
								MessageUtil.writeString(responseBuffer, aPlayer, "UTF-8");
								responseBuffer.writeInt(4);
								messageManager.sendMessageToRole(dPlayer, responseBuffer);
							}else if(aRole.getStatus()!=3&&dRole.getStatus()==3){
								ChannelBuffer responseBuffer = new DynamicChannelBuffer(200);
								responseBuffer.writeInt(1004);
								MessageUtil.writeString(responseBuffer, aPlayer, "UTF-8");
								MessageUtil.writeString(responseBuffer, dPlayer, "UTF-8");
								responseBuffer.writeInt(4);
								messageManager.sendMessageToRole(aPlayer, responseBuffer);
							}else if(aRole.getStatus()==3&&dRole.getStatus()==3){
								freeFightRoom.remove(battleId);
							}
						}

					}
					
				}
			}catch (Exception e) {
				logger.error("RoleStatusScanThread",e);
			}
			
		}
		
	}
	public RoleManager getRoleManager() {
		return roleManager;
	}
	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}
	public MessageManager getMessageManager() {
		return messageManager;
	}
	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}
	public FightManager getFightManager() {
		return fightManager;
	}
	public void setFightManager(FightManager fightManager) {
		this.fightManager = fightManager;
	}
}
