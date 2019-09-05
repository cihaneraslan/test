package com.cgiser.moka.energy.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.message.netty.MessageUtil;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class SendEnergyAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String roleName = ServletUtil.getDefaultValue(request, "roleId", "");
		ReturnType<String> returnType = new ReturnType<String>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			
			Role role1 = roleManager.getRoleByName(roleName);
			if(StringUtils.isEmpty(roleName)||null==role1){
				returnType.setMsg("您赠送的用户不存在");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			
			if(role1.getStatus()!=0){
				
			}
			roleManager.addEnergy(roleName, 1);
			//初始生成战斗模型后通知挑战者开始战斗
			ChannelBuffer buffer = new DynamicChannelBuffer(200);
			buffer.writeInt(1006);
			MessageUtil.writeString(buffer, role.getRoleName(), "UTF-8");
			MessageUtil.writeString(buffer, roleName, "UTF-8");
			buffer.writeInt(2);
			MessageManager messageManager = (MessageManager)HttpSpringUtils.getBean("messageManager");
			messageManager.sendMessageToRole(roleName, buffer);
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
