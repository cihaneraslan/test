package com.cgiser.moka.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class FindNearRoleAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String roleName = ServletUtil.getDefaultValue(request, "role",
		"");
		ReturnType<List<Role>> returnType = new ReturnType<List<Role>>();
		try{
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			List<Role> roles = roleManager.getRolesByName(roleName);
			
			if(roles==null||roles.size()==0){
				returnType.setValue(null);
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
			}else{
				returnType.setValue(roles);
				returnType.setStatus(1);
				super.printReturnType2Response(response, returnType);
			}
			return null;
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		try{
			returnType.setValue(null);
			returnType.setStatus(0);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);// TODO: handle exception
		}
		return null;
		
	}
}
