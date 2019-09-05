package com.cgiser.moka.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.keel.user.CasUserManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Role;
import com.cgiser.sso.model.User;

public class GetWebLoginRoleAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		try{
			CasUserManager casUserManager = CasUserManager.getUserManagerInstance();
			User user = casUserManager.getUser();
			if(user==null){
				response.sendRedirect("http://passport.moonlightol.com:8080/weblogin.do");
				return null;
			}
			Role role = super.getCurrentRole(request);
			if(role==null){
				response.sendRedirect("http://passport.moonlightol.com:8080/weblogin.do");
				return null;
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			request.setAttribute("role", role);
			request.setAttribute("effective", roleManager.getRoleEffective(role.getRoleId()));
			return mapping.findForward("role");
		}catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
		
		
	}
}
