/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cgiser.sso.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.connection.INonBlockingConnection;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.dao.util.DigestUtils;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Role;

/**
 *
 * @author zkpursuit
 */
public class LoginCommand extends Command implements ICommand{
	Logger logger = LoggerFactory.getLogger(LoginCommand.class);
    @Override
    public void execute(INonBlockingConnection nbc, int cmd, Object buffer){
        if(buffer instanceof Packet){
            Packet packet = (Packet)buffer;
            String roleName = packet.readString("UTF-8");
            Packet senddata = new Packet(200);
            //UserManager userManager = (UserManager)HttpSpringUtils.getBean("userManager");
            RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
            //User user = userManager.getUserbyUserIden(userIden);
            Role role = roleManager.getRoleByName(roleName);
            if(role==null){
            	logger.debug("登录的用户不存在");
            	try {
            		nbc.close();
				} catch (Exception e) {
					logger.error("登录的用户不存在");
				}
            }else{
	        	getSocketHandler().bindUserAndNbc(DigestUtils.digest(roleName), nbc.getId());
	            senddata.writeString(roleName, "UTF-8");
	            senddata.writeString("已建立连接", "UTF-8");
	            getSocketHandler().sendMessageToAll(senddata);
            }
            senddata.clear();
            packet.clear();
        }
    }
}
