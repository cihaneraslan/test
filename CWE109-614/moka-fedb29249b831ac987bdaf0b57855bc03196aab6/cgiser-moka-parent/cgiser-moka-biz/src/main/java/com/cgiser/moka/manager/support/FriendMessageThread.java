package com.cgiser.moka.manager.support;

import java.util.Date;
import java.util.concurrent.ConcurrentMap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.moka.manager.FightManager;
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.message.netty.MessageUtil;
import com.cgiser.moka.model.FightResult;
import com.cgiser.moka.model.FreeFightModel;
import com.cgiser.moka.model.Role;

public class FriendMessageThread extends Thread {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private final ConcurrentMap<String, FreeFightModel> fightMessageList;
	private RoleManager roleManager;
	private FightManager fightManager;
	private MessageManager messageManager;
	public FriendMessageThread(ConcurrentMap<String, FreeFightModel> frightMessageList){
		fightMessageList = frightMessageList;
	}
	@Override
	public void run() {
		Date date;
		FreeFightModel model;
		Role dRole;
		FightResult fightResult;
		ChannelBuffer buffer;
		date = new Date();
		for(String key:fightMessageList.keySet()){
			try {
				model = fightMessageList.get(key);
				dRole = roleManager.getRoleByName(model.getdRole());
				if(dRole.getStatus()==3||dRole.getStatus()==2){
					fightResult = fightManager.getFight(model.getBattleId());
					fightResult.getDefendPlayer().setHand(false);
					fightManager.saveFight(fightResult);
					fightMessageList.remove(key);
				}else{
					buffer = new DynamicChannelBuffer(200);
					buffer.writeInt(1004);
					MessageUtil.writeString(buffer, model.getaRole(), "UTF-8");
					MessageUtil.writeString(buffer, model.getdRole(), "UTF-8");
					buffer.writeInt(2);
					MessageUtil.writeString(buffer,model.getBattleId(), "UTF-8");
					buffer.writeInt(1);
					MessageUtil.writeString(buffer,key, "UTF-8");
					messageManager.sendMessageToRole(model.getdRole(), buffer);
				}
				Thread.sleep(5000);
				if(date.getTime()-model.getDate().getTime()>500000){
					fightMessageList.remove(key);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				logger.error("MessageKeyThread",e);
			}
			
		}
	}
	public RoleManager getRoleManager() {
		return roleManager;
	}
	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}
	public FightManager getFightManager() {
		return fightManager;
	}
	public void setFightManager(FightManager fightManager) {
		this.fightManager = fightManager;
	}
	public MessageManager getMessageManager() {
		return messageManager;
	}
	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}

}
