package com.cgiser.moka.action;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.keel.user.CasUserManager;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.dao.util.DigestUtils;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Role;
import com.cgiser.sso.model.User;

public class WebLoginAction extends AbstractAction {
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
			
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			String roleName = ServletUtil.getDefaultValue(request, "roleName",
			"");
			Role role = roleManager.getRoleByName(roleName);
			if(role==null){
				response.sendRedirect("http://passport.moonlightol.com:8080/weblogin.do");
				return null;
			}
			response.addCookie(new Cookie(cookieId, DigestUtils.digest(roleName)));
			request.setAttribute("role", role);
			request.setAttribute("effective", roleManager.getRoleEffective(role.getRoleId()));
			return mapping.findForward("role");
		}catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
		
		
	}
}
