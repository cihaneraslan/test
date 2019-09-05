package com.cgiser.moka.action;

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

public class UpdateRoleStatusAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String status = ServletUtil.getDefaultValue(request, "status",
		null);
		ReturnType<String> returnType = new ReturnType<String>();
		try{
			Role role = this.getCurrentRole(request);
			if(role==null){
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("您还没登录哦");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(status==null){
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("参数有误");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			roleManager.updateRoleStatus(role.getRoleName(), Integer.parseInt(status));
			returnType.setValue(status);
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
			return null;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
		
	}
}
