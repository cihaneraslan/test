package com.cgiser.moka.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class RegisterRoleAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String userIden = ServletUtil.getDefaultValue(request, "uniden",
		"");
		String gameIden = ServletUtil.getDefaultValue(request, "gameIden", "");
		String serverId = ServletUtil.getDefaultValue(request, "serverid",
		"");
		String roleName = ServletUtil.getDefaultValue(request, "rolename",
		"");
		String invitCode = ServletUtil.getDefaultValue(request, "invitCode",
		"");
		String sex = ServletUtil.getDefaultValue(request, "Sex",
		"");
		ReturnType<Role> returnType = new ReturnType<Role>();
		RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
		try{
			if(StringUtils.isEmpty(gameIden)||StringUtils.isEmpty(userIden)||StringUtils.isEmpty(serverId)||StringUtils.isEmpty(roleName)){
				logger.info("创建角色失败");
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("参数有误");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(!com.cgiser.moka.common.utils.StringUtils.checkUserNameValid(roleName)){
				logger.info("创建角色失败");
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("角色名无效(由2-6个汉字,数字和26个英文字母或者下划线组成的字符串,不能以下划线开头或结尾)");
				super.printReturnType2Response(response, returnType);
				return null;
		    }
			Role role = roleManager.getRoleByName(roleName);
			if(role!=null){
				logger.info("创建角色失败");
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("角色名被占用");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			
			if(StringUtils.isEmpty(sex)&&!StringUtils.isNumeric(sex)){
				logger.info("创建角色失败");
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("性别参数有误");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			List<Role> roles = roleManager.getRolesByUserIden(userIden, new Long(serverId));
			if(!CollectionUtils.isEmpty(roles)&&roles.size()>=5){
				logger.info("创建角色失败");
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("一个服只能创建5个角色");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			Long roleId = roleManager.createRole(gameIden,userIden, serverId, roleName,invitCode,Integer.parseInt(sex));
			if(roleId>0){
//				List<Role> roles = roleManager.getRolesByUserIden(userIden, new Long(serverId));
				returnType.setValue(roleManager.getRoleById(roleId));
				returnType.setStatus(1);
				returnType.setMsg("创建角色成功");
//				response.addCookie(new Cookie(cookieId, userIden));
				super.printReturnType2Response(response, returnType);
				return null;
			}else{
				logger.info("创建角色失败");
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("创建角色失败");
				super.printReturnType2Response(response, returnType);
				return null;
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		try{
			returnType.setValue(null);
			returnType.setStatus(0);
			returnType.setMsg("创建角色失败");
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
