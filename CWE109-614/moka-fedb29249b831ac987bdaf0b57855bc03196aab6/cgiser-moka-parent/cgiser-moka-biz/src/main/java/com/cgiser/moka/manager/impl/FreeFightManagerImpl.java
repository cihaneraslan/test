package com.cgiser.moka.manager.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.moka.manager.FightManager;
import com.cgiser.moka.manager.FreeFightManager;
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.message.netty.MessageUtil;
import com.cgiser.moka.model.FightResult;
import com.cgiser.moka.model.FreeFightModel;
import com.cgiser.moka.model.Role;

public class FreeFightManagerImpl implements FreeFightManager {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private RoleManager roleManager;
	private FightManager fightManager;
	private MessageManager messageManager;
	private final ConcurrentMap<String, FreeFightModel> fightMessageList = new ConcurrentHashMap<String, FreeFightModel>();
	@Override
	public void addFightMessage(String aRole,String dRole,String battleId,int round) {
		String key = UUID.randomUUID().toString();
		FreeFightModel model = new FreeFightModel();
		model.setaRole(aRole);
		model.setBattleId(battleId);
		model.setdRole(dRole);
		model.setDate(new Date());
		ChannelBuffer buffer = new DynamicChannelBuffer(200);
		buffer.writeInt(1004);
		MessageUtil.writeString(buffer, model.getaRole(), "UTF-8");
		MessageUtil.writeString(buffer, model.getdRole(), "UTF-8");
		buffer.writeInt(2);
		MessageUtil.writeString(buffer,model.getBattleId(), "UTF-8");
		buffer.writeInt(round);
		MessageUtil.writeString(buffer,key, "UTF-8");
		messageManager.sendMessageToRole(model.getdRole(), buffer);
		fightMessageList.put(key, model);
	}

	@Override
	public void start() {
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
				if(date.getTime()-model.getDate().getTime()>500000){
					fightMessageList.remove(key);
				}
			} catch (Exception e) {
				logger.error("FreeFightManagerImpl",e);
			}
			
		}
	}
	@Override
	public void matchPlayer() {
		ChannelBuffer buffer;
		Set<String> sets = new HashSet<String>();
		sets.clear();
		for(String key:MatchGameManagerImpl.easyRoom){
			try {
				sets.add(key);
				if(sets.size()>=2){
					buffer = new DynamicChannelBuffer(200);
					buffer.writeInt(1004);
					MessageUtil.writeString(buffer, sets.toArray()[0].toString(), "UTF-8");
					MessageUtil.writeString(buffer, sets.toArray()[1].toString(), "UTF-8");
					buffer.writeInt(5);
					MessageUtil.writeString(buffer,sets.toArray()[1].toString(), "UTF-8");
					messageManager.sendMessageToRole(sets.toArray()[0].toString(), buffer);
					MatchGameManagerImpl.easyRoom.remove(sets.toArray()[0]);
					MatchGameManagerImpl.easyRoom.remove(sets.toArray()[1]);
					sets.clear();
				}
			} catch (Exception e) {
				logger.error("FreeFightManagerImpl",e);
			}
			
		}
		sets.clear();
		for(String key:MatchGameManagerImpl.midRoom){
			try {
				sets.add(key);
				if(sets.size()>=2){
					buffer = new DynamicChannelBuffer(200);
					buffer.writeInt(1004);
					MessageUtil.writeString(buffer, sets.toArray()[0].toString(), "UTF-8");
					MessageUtil.writeString(buffer, sets.toArray()[1].toString(), "UTF-8");
					buffer.writeInt(5);
					MessageUtil.writeString(buffer,sets.toArray()[1].toString(), "UTF-8");
					messageManager.sendMessageToRole(sets.toArray()[0].toString(), buffer);
					MatchGameManagerImpl.midRoom.remove(sets.toArray()[0]);
					MatchGameManagerImpl.midRoom.remove(sets.toArray()[1]);
					sets.clear();
				}
			} catch (Exception e) {
				logger.error("FreeFightManagerImpl",e);
			}
			
		}
		sets.clear();
		for(String key:MatchGameManagerImpl.handRoom){
			try {
				sets.add(key);
				if(sets.size()>=2){
					buffer = new DynamicChannelBuffer(200);
					buffer.writeInt(1004);
					MessageUtil.writeString(buffer, sets.toArray()[0].toString(), "UTF-8");
					MessageUtil.writeString(buffer, sets.toArray()[1].toString(), "UTF-8");
					buffer.writeInt(5);
					MessageUtil.writeString(buffer,sets.toArray()[1].toString(), "UTF-8");
					messageManager.sendMessageToRole(sets.toArray()[0].toString(), buffer);
					MatchGameManagerImpl.handRoom.remove(sets.toArray()[0]);
					MatchGameManagerImpl.handRoom.remove(sets.toArray()[1]);
					sets.clear();
				}
			} catch (Exception e) {
				logger.error("FreeFightManagerImpl",e);
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

	@Override
	public void removeFightMessage(String key) {
		if(fightMessageList.containsKey(key)){
			fightMessageList.remove(key);
		}
		
	}

	@Override
	public ConcurrentMap<String, FreeFightModel> getFreeFightMessageList() {
		// TODO Auto-generated method stub
		return this.fightMessageList;
	}

}
