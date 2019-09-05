package com.cgiser.moka.vip.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.VipInfoManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.VipInfo;
import com.cgiser.moka.result.ReturnType;

public class GetRoleVipInfoAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			ReturnType<VipInfo> returnType = new ReturnType<VipInfo>();			
			String pass = ServletUtil.getDefaultValue(request, "pass", "");
			String roleName = ServletUtil.getDefaultValue(request, "roleName", "");
			if(!pass.equals("cgiserpass")){
				returnType.setMsg("您没有权限哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(StringUtils.isEmpty(roleName)){
				returnType.setMsg("参数有误哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			Role role = roleManager.getRoleByName(roleName);
			if (null == role) {
				returnType.setMsg("角色不存在哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			VipInfoManager vipInfoManager = (VipInfoManager)HttpSpringUtils.getBean("vipInfoManager");
			VipInfo vipInfo = vipInfoManager.getVipInfoByRoleName(roleName);
			returnType.setValue(vipInfo);
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}

		return null;
	}
}
