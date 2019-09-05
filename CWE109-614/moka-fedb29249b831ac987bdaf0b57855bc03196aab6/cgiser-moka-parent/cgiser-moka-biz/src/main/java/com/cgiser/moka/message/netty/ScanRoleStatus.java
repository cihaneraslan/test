package com.cgiser.moka.message.netty;

import com.cgiser.moka.manager.FightManager;
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.support.RoleStatusScanThread;

public class ScanRoleStatus {
	private RoleManager roleManager;
	private MessageManager messageManager;
	private FightManager fightManager;
	public void scan(){
		RoleStatusScanThread scan = new RoleStatusScanThread();
		scan.setRoleManager(roleManager);
		scan.setMessageManager(messageManager);
		scan.setFightManager(fightManager);
		scan.start();
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
