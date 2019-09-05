/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cgiser.sso.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.connection.INonBlockingConnection;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Role;

/**
 *
 * @author zkpursuit
 */
public class Command implements ICommand{
	Logger logger = LoggerFactory.getLogger(Command.class);
    protected XSocketDataHandler socket;
    protected RoleManager roleManager;
    protected LegionManager legionManager;
    public LegionManager getLegionManager() {
		if(legionManager==null){
			legionManager = (LegionManager)HttpSpringUtils.getBean("legionManager");
		}
		return legionManager;
	}
	public void setLegionManager(LegionManager legionManager) {
		this.legionManager = legionManager;
	}
	public Command(){}
    public void setSocketHandler(XSocketDataHandler handler){
        this.socket = handler;
    }
    public XSocketDataHandler getSocketHandler() {
        return this.socket;
    }
    public void execute(INonBlockingConnection nbc, int cmd, Object buffer){
        
    }
    public Role isLogin(INonBlockingConnection nbc, int cmd, Object buffer){
    	 Packet packet = (Packet)buffer;
         String roleName = packet.readString("UTF-8");         
         Role role = roleManager.getRoleByName(roleName);
         if(role==null){
         	logger.error("登录的用户不存在");
         	return null;
         }else{
	        return role;
         }
    }
	public RoleManager getRoleManager() {
		if(roleManager==null){
			roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
		}
		return roleManager;
	}
	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}
}
