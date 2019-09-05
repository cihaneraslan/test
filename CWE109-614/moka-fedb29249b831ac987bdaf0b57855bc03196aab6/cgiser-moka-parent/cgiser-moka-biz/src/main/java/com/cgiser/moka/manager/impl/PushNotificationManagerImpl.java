package com.cgiser.moka.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.feedback.FeedbackServiceManager;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.TokenDao;
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.PushNotificationManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.message.netty.MessageUtil;
import com.cgiser.moka.model.MessageType;
import com.cgiser.moka.model.Role;

public class PushNotificationManagerImpl implements PushNotificationManager {
	Logger logger = LoggerFactory.getLogger(PushNotificationManagerImpl.class);
	TokenDao tokenDao;
	private String certificatePath;
	private String certificatePassword;
	private int production;
	private String sound = "default";//铃音
	javapns.notification.PushNotificationManager pushManager;
	private RoleManager roleManager;
	private MessageManager messageManager;
	@Override
	public Long updateRoleTokenId(Long roleId, String tokenId) {
		this.pushNotification(roleId, "", "亲，欢迎登陆月光宝盒，赶紧与你的小伙伴们去战斗吧！");
		return tokenDao.addTokenId(roleId, tokenId);
	}
	@Override
	public int pushNotification(Long roleId, String title, String content) {
		List<Map<String, Object>> list = tokenDao.getRoleToken(roleId);
		
		List<String> tokens = new ArrayList<String>();
		if(CollectionUtils.isEmpty(list)){
			return 0;
		}
		for(Map map:list){
			tokens.add(map.get("TOKENID").toString());
		}
		boolean sendCount = true;
		if(tokens.size()>1){
			sendCount = false;
		}
		 try
	        {
	            PushNotificationPayload payLoad = new PushNotificationPayload();
	            payLoad.addAlert(content); // 消息内容
	            payLoad.addBadge(1); // iphone应用图标上小红圈上的数值
	            if (!StringUtils.isBlank(sound))
	            {
	                payLoad.addSound(sound);//铃音
	            }
//	            javapns.notification.PushNotificationManager pushManager = new javapns.notification.PushNotificationManager();
	            //true：表示的是产品发布推送服务 false：表示的是产品测试推送服务
	            pushManager.initializeConnection(new AppleNotificationServerBasicImpl(certificatePath, certificatePassword, true));
	            List<PushedNotification> notifications = new ArrayList<PushedNotification>();
	            // 发送push消息
	            if (sendCount)
	            {
	                Device device = new BasicDevice();
	                device.setToken(tokens.get(0));
	                PushedNotification notification = pushManager.sendNotification(device, payLoad, true);
	                notifications.add(notification);
	            }
	            else
	            {
	                List<Device> device = new ArrayList<Device>();
	                for (String token : tokens)
	                {
	                    device.add(new BasicDevice(token));
	                }
	                notifications = pushManager.sendNotifications(payLoad, device);
	            }
	            List<PushedNotification> failedNotifications = PushedNotification.findFailedNotifications(notifications);
	            List<PushedNotification> successfulNotifications = PushedNotification.findSuccessfulNotifications(notifications);
	            int failed = failedNotifications.size();
	            int successful = successfulNotifications.size();
	            logger.error("发送消息成功条数"+successful);
	            logger.error("发送消息失败条数"+failed);
	            pushManager.stopConnection();
	        }
	        catch (Exception e)
	        {
	        	logger.error(e.getMessage(),e);
	        }
//        String certificatePath = "C:/OpenSSL-Win64/bin/aps/aps_development.p12";
//        String certificatePassword = "YHlf13739641298";//此处注意导出的证书密码不能为空因为空密码会报错
		
        return 1;
	}
	public TokenDao getTokenDao() {
		return tokenDao;
	}
	public void setTokenDao(TokenDao tokenDao) {
		this.tokenDao = tokenDao;
	}
	public String getCertificatePath() {
		return certificatePath;
	}
	public void setCertificatePath(String certificatePath) {
		this.certificatePath = certificatePath;
	}
	public javapns.notification.PushNotificationManager getPushManager() {
		return pushManager;
	}
	public void setPushManager(
			javapns.notification.PushNotificationManager pushManager) {
		this.pushManager = pushManager;
	}
	public RoleManager getRoleManager() {
		return roleManager;
	}
	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}
	@Override
	public void pushEnergyToAll(){
		List<Role> roles = roleManager.getAllRoles(1);
		if(!CollectionUtils.isEmpty(roles)){
			for(int i=0;i<roles.size();i++){
				if(roles.get(i).getEnergy()<roles.get(i).getEnergyMax()){
					roleManager.addEnergy(roles.get(i).getRoleName(), 1);
				}
			}
				
		}
		
		logger.debug("真实发放体力人数："+roles.size());
		logger.debug("查询出来的角色人数："+roles.size());
		roles.clear();
		roles=null;
		logger.debug("执行了"+Thread.currentThread().getId());
		
	}
	@Override
	public void pushMatchGameMessageToAll() {
		List<Map<String, Object>> list = tokenDao.getAllToken();
		
		List<String> tokens = new ArrayList<String>();
		if(CollectionUtils.isEmpty(list)){
			return;
		}
		for(Map map:list){
			tokens.add(map.get("TOKENID").toString());
		}
		MatchGameManagerImpl.isStart = true;
		ChannelBuffer buffer1 = new DynamicChannelBuffer(200);
		buffer1.writeInt(MessageType.SYSTEM.getCode());
		buffer1.writeInt(MessageType.MATCHSTART.getCode());
		MessageUtil.writeString(buffer1, "匹配战开始了，赶紧去赚取荣誉点吧！", "UTF-8");
		messageManager.sendMessageToAll(buffer1);
		List<String> tokens1 = new ArrayList<String>();
		tokens1.add(tokens.get(0));
		this.pushMessage("匹配战开始了，赶紧去赚取荣誉点吧！",tokens1);
        this.pushMessage("匹配战开始了，赶紧去赚取荣誉点吧！", tokens);
	}
	@Override
	public void pushEnertyMessageToAll() {
		List<Map<String, Object>> list = tokenDao.getAllToken();
		
		List<String> tokens = new ArrayList<String>();
		if(CollectionUtils.isEmpty(list)){
			return;
		}
		for(Map map:list){
			tokens.add(map.get("TOKENID").toString());
		}
		List<String> tokens1 = new ArrayList<String>();
		tokens1.add(tokens.get(0));
		this.pushMessage("领取体力的时间到了，赶紧去补充体力吧！",tokens1);
        this.pushMessage("领取体力的时间到了，赶紧去补充体力吧！", tokens);
	}
	private int pushMessage(String content,List<String> tokens){


		boolean sendCount = true;
		if(tokens.size()>1){
			sendCount = false;
		}
		 try
	        {
	            PushNotificationPayload payLoad = new PushNotificationPayload();
	            payLoad.addAlert(content); // 消息内容
	            payLoad.addBadge(1); // iphone应用图标上小红圈上的数值
	            if (!StringUtils.isBlank(sound))
	            {
	                payLoad.addSound(sound);//铃音
	            }
//	            javapns.notification.PushNotificationManager pushManager = new javapns.notification.PushNotificationManager();
	            //true：表示的是产品发布推送服务 false：表示的是产品测试推送服务
	            pushManager.initializeConnection(new AppleNotificationServerBasicImpl(certificatePath, certificatePassword, true));
	            List<PushedNotification> notifications = new ArrayList<PushedNotification>();
	            // 发送push消息
	            if (sendCount)
	            {
	                Device device = new BasicDevice();
	                device.setToken(tokens.get(0));
	                PushedNotification notification = pushManager.sendNotification(device, payLoad, true);
	                notifications.add(notification);
	            }
	            else
	            {
	                List<Device> device = new ArrayList<Device>();
	                for (String token : tokens)
	                {
	                    device.add(new BasicDevice(token));
	                }
	                notifications = pushManager.sendNotifications(payLoad, device);
	            }
	            List<PushedNotification> failedNotifications = PushedNotification.findFailedNotifications(notifications);
	            List<PushedNotification> successfulNotifications = PushedNotification.findSuccessfulNotifications(notifications);
	            int failed = failedNotifications.size();
	            int successful = successfulNotifications.size();
	            logger.error("发送消息成功条数"+successful);
	            logger.error("发送消息失败条数"+failed);
	            pushManager.stopConnection();
	        }
	        catch (Exception e)
	        {
	        	logger.error(e.getMessage(),e);
	        	return 0;
	        }
	        return 1;
	}
	public String getCertificatePassword() {
		return certificatePassword;
	}
	public void setCertificatePassword(String certificatePassword) {
		this.certificatePassword = certificatePassword;
	}
	public MessageManager getMessageManager() {
		return messageManager;
	}
	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}
	public int getProduction() {
		return production;
	}
	public void setProduction(int production) {
		this.production = production;
	}


}
