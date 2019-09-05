package com.cgiser.moka.mail.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.EmailManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class ReadMailAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String emailId = ServletUtil.getDefaultValue(request, "id", null);
		ReturnType<String> returnType = new ReturnType<String>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(null==emailId){
				returnType.setMsg("您要读的邮件不存在或者已删除哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			EmailManager emailManager = (EmailManager)HttpSpringUtils.getBean("emailManager");
			if(emailManager.updateEmailStatue(new Long(emailId), 2)>0){
				int count = emailManager.getEmailsCountByToRoleStatus(role.getRoleId(), 1);
				roleManager.updateRoleNewEmail(role.getRoleId(), count);
				super.printReturnType2Response(response, returnType);
			}else{
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
			}

		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
