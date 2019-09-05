package com.cgiser.moka.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.keel.user.CasUserManager;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;
import com.cgiser.sso.model.User;

public class GetWebRoleAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String serverId = ServletUtil.getDefaultValue(request, "serverid",
		"");
		String callback = ServletUtil.getDefaultValue(request, "callback",
		"");
		ReturnType<List<Role>> returnType = new ReturnType<List<Role>>();
		try{
			CasUserManager casUserManager = CasUserManager.getUserManagerInstance();
			User user = casUserManager.getUser();
			if(user==null){
				response.sendRedirect("http://passport.moonlightol.com:8080/weblogin.do");
				return null;
			}
			if(StringUtils.isBlank(serverId)){
				logger.info("获取角色失败");
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("获取角色失败");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			List<Role> roleList = roleManager.getRolesByUserIden(user.getUserIden(),new Long(serverId));
			if(CollectionUtils.isEmpty(roleList)){
				logger.info("获取角色失败");
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("获取角色失败");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			StringBuffer result = new StringBuffer();
			if(StringUtils.isNotBlank(callback)){
				result.append(callback).append("(");
			}
			result.append(JSONArray.fromObject(roleList));
			if(StringUtils.isNotBlank(callback)){
				result.append(")");
			}
			response.getWriter().print(result);
			return null;
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		try{
			returnType.setValue(null);
			returnType.setStatus(0);
			returnType.setMsg("获取角色失败");
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}

		return null;
		
		
	}
}
