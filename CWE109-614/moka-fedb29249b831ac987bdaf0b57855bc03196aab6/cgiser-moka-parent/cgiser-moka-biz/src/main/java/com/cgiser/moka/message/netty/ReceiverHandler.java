package com.cgiser.moka.message.netty;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.ThirdManager;
import com.cgiser.moka.manager.impl.MatchGameManagerImpl;
import com.cgiser.moka.model.MessageType;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.ThirdRecord;

public class ReceiverHandler extends SimpleChannelHandler {
	private Logger logger = LoggerFactory.getLogger(ReceiverHandler.class
			.getName());
	public static final CgiserChannelGroup allChannels = new CgiserChannelGroup();
	private MessageManager messageManager;
	private RoleManager roleManager;
	private ThirdManager thirdManager;
	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		ChannelBuffer responseBuffer;
		ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
		byte[] recByte = buffer.copy().toByteBuffer().array();
		ChannelBuffer requestBuffer = new DynamicChannelBuffer(recByte.length);
		requestBuffer.writeBytes(recByte);
		String recMsg = new String(recByte);
		if (recMsg.indexOf("<policy-file-request/>") != -1) {
			String xml = "<cross-domain-policy>";
			xml = xml
					+ "<allow-access-from domain=\"*\" to-ports=\"5000,8080,8088,8090\" />";
			xml = xml + "</cross-domain-policy>";
			xml = xml + "\0";
			responseBuffer = new DynamicChannelBuffer(xml.getBytes().length);
			responseBuffer.writeBytes(xml.getBytes());
			e.getChannel().write(responseBuffer);
			return;
		}
		logger.info("server received:" + recMsg.trim());
		int dataLength = requestBuffer.readInt();// dataLength
		int cmd = requestBuffer.readShort();
		messageManager.excute(cmd, e.getChannel(), requestBuffer);
		// String str = "heartbeat";
		// responseBuffer=new DynamicChannelBuffer(4+4+str.getBytes().length);
		// responseBuffer.writeInt(4+str.getBytes().length);
		// responseBuffer.writeInt(1020);
		// responseBuffer.writeBytes(str.getBytes());
		// e.getChannel().write(responseBuffer);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.channelConnected(ctx, e);
		allChannels.add(e.getChannel());
		String str = "heartbeat";
		ChannelBuffer responseBuffer = new DynamicChannelBuffer(4 + 4 + str
				.getBytes().length);
		responseBuffer.writeInt(4 + str.getBytes().length);
		responseBuffer.writeInt(1020);
		responseBuffer.writeBytes(str.getBytes());
		e.getChannel().write(responseBuffer);
		ChannelBuffer buffer = new DynamicChannelBuffer(200);
		buffer.writeInt(MessageType.SYSTEM.getCode());
		buffer.writeInt(MessageType.MATCHSTART.getCode());
		MessageUtil.writeString(buffer, "欢迎进入月光宝盒！每天的12:00和18:00都可以领取20点体力！", "UTF-8");
		messageManager.sendMessageToChannel(e.getChannel(), buffer);
		if(MatchGameManagerImpl.isStart){
			buffer = new DynamicChannelBuffer(200);
			buffer.writeInt(MessageType.SYSTEM.getCode());
			buffer.writeInt(MessageType.MATCHSTART.getCode());
			MessageUtil.writeString(buffer, "匹配战开始了，赶紧去赚取荣誉点吧！", "UTF-8");
			messageManager.sendMessageToChannel(e.getChannel(), buffer);
		}else{
			buffer = new DynamicChannelBuffer(200);
			buffer.writeInt(MessageType.SYSTEM.getCode());
			buffer.writeInt(MessageType.MATCHSTART.getCode());
			MessageUtil.writeString(buffer, "匹配战开始时间：每天的12:00到14:00和20:00到22:00，您可以在匹配战中赚取荣誉点，荣誉点可以在龙宫中兑换您心仪的武器！", "UTF-8");
			messageManager.sendMessageToChannel(e.getChannel(), buffer);
		}
		buffer = new DynamicChannelBuffer(200);
		buffer.writeInt(MessageType.SYSTEM.getCode());
		buffer.writeInt(MessageType.MATCHSTART.getCode());
		MessageUtil.writeString(buffer, "亲，排名战前1000名有大量的铜钱奖励哦！", "UTF-8");
		messageManager.sendMessageToChannel(e.getChannel(), buffer);
		buffer = new DynamicChannelBuffer(200);
		buffer.writeInt(MessageType.SYSTEM.getCode());
		buffer.writeInt(MessageType.MATCHSTART.getCode());
		MessageUtil.writeString(buffer, "亲，好友切磋可以赚取奖励哦(切磋一次奖励一点体力值，体力值收益次数在首页战书中查看)！", "UTF-8");
		messageManager.sendMessageToChannel(e.getChannel(), buffer);
		buffer = new DynamicChannelBuffer(200);
		buffer.writeInt(MessageType.SYSTEM.getCode());
		buffer.writeInt(MessageType.MATCHSTART.getCode());
		MessageUtil.writeString(buffer, "亲，分享到微信，微博可以赚取元宝哦！", "UTF-8");
		messageManager.sendMessageToChannel(e.getChannel(), buffer);
		buffer = new DynamicChannelBuffer(200);
		buffer.writeInt(MessageType.SYSTEM.getCode());
		buffer.writeInt(MessageType.MATCHSTART.getCode());
		MessageUtil.writeString(buffer, "亲，有任何疑问请加入我们的官方QQ群:368881376咨询！", "UTF-8");
		messageManager.sendMessageToChannel(e.getChannel(), buffer);
		try{
			List<ThirdRecord> thirdRecords = thirdManager.getWinner();
			if(!CollectionUtils.isEmpty(thirdRecords)){
				ThirdRecord r = thirdRecords.get(thirdRecords.size()-1);
				int num = 0;
				Role role;
				int issueNum = thirdManager.getNumByIssue(String.valueOf(r.getDate()));
				Map<Integer,Integer> mapCash = thirdManager.getCash(issueNum);
				for(ThirdRecord record:thirdRecords){
					role = roleManager.getRoleById(record.getRoleId());
					if(role!=null&&record.getDate()==r.getDate()){
						num = record.getFirst()*100+record.getSecond()*10+record.getThird();
						buffer = new DynamicChannelBuffer(200);
						buffer.writeInt(MessageType.SYSTEM.getCode());
						buffer.writeInt(MessageType.MATCHSTART.getCode());
						MessageUtil.writeString(buffer, "恭喜<a href=\"event:user_" +role.getRoleName()+ "\"><u>[" + role.getRoleName() + "]</u></a>" +
								"在长乐坊第"+record.getDate()+"期中用"+record.getCount()*20+"元宝博得了" +
									""+mapCash.get(num)*record.getCount()+"元宝", "UTF-8");
						messageManager.sendMessageToChannel(e.getChannel(), buffer);
					}

				}
			}
		}catch (Exception e1) {
			logger.error(e1.getMessage(),e1);
		}


	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.channelClosed(ctx, e);
		try {
			allChannels.removeChannel(e.getChannel());

		} catch (Exception ex) {
			logger.error(ex.getMessage(),ex);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.error(e.getCause().getMessage());
		if (e.getChannel() != null) {
			e.getChannel().close().addListener(ChannelFutureListener.CLOSE);
		}
	}

	public MessageManager getMessageManager() {
		return messageManager;
	}

	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}

	public ThirdManager getThirdManager() {
		return thirdManager;
	}

	public void setThirdManager(ThirdManager thirdManager) {
		this.thirdManager = thirdManager;
	}
}
