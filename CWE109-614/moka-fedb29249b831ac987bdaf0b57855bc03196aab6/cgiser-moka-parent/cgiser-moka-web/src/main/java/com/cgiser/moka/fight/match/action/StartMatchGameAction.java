package com.cgiser.moka.fight.match.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.manager.MatchGameManager;
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.impl.MatchGameManagerImpl;
import com.cgiser.moka.message.netty.MessageUtil;
import com.cgiser.moka.model.MessageType;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class StartMatchGameAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<Integer> returnType = new ReturnType<Integer>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			MessageManager messageManager = (MessageManager)HttpSpringUtils.getBean("messageManager");
			MatchGameManager matchGameManager = (MatchGameManager)HttpSpringUtils.getBean("matchGameManager");
			matchGameManager.startMatchGame();
			ChannelBuffer buffer1 = new DynamicChannelBuffer(200);
			buffer1.writeInt(MessageType.SYSTEM.getCode());
			buffer1.writeInt(MessageType.MATCHSTART.getCode());
			MessageUtil.writeString(buffer1, "匹配战要开始了，赶紧去赚取荣誉点吧！", "UTF-8");
			messageManager.sendMessageToAll(buffer1);
			returnType.setStatus(1);
			returnType.setValue(MatchGameManagerImpl.isStart?1:0);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	} 
}
