package com.cgiser.moka.mail.action;

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
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.EmailManager;
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.MessageType;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class SendMailAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String title = ServletUtil.getDefaultValue(request, "title", null);
		String content = ServletUtil.getDefaultValue(request, "content", null);
		String type = ServletUtil.getDefaultValue(request, "type", null);
		String to = ServletUtil.getDefaultValue(request, "to", null);
		ReturnType<Long> returnType = new ReturnType<Long>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(title==null||content==null||type==null||to==null){
				returnType.setMsg("参数有误哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			EmailManager emailManager = (EmailManager)HttpSpringUtils.getBean("emailManager");
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			Long emailId = 0L;
			Role toRole = null;
			if(to.equals("0")){
				emailId = emailManager.sendEmail(title, "来自["+role.getRoleName()+"]的邮件:"+content, role.getRoleId(), 1000000155L, Integer.parseInt(type));
			}else{
				toRole = roleManager.getRoleByName(to);
				emailId = emailManager.sendEmail(title, content, role.getRoleId(), toRole.getRoleId(), Integer.parseInt(type));
			}
			if(emailId>0){
				if(toRole!=null){
					int count = emailManager.getEmailsCountByToRoleStatus(toRole.getRoleId(), 1);
					MessageManager messageManager = (MessageManager)HttpSpringUtils.getBean("messageManager");
					ChannelBuffer buffer = new DynamicChannelBuffer(200);
					buffer.writeInt(MessageType.SYSTEM.getCode());
					buffer.writeInt(MessageType.NEWEMAIL.getCode());
					buffer.writeInt(count);
					messageManager.sendMessageToRole(toRole.getRoleName(), buffer);
				}
				returnType.setStatus(1);
				returnType.setValue(emailId);
			}else{
				returnType.setStatus(0);
				returnType.setValue(emailId);
			}

			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
