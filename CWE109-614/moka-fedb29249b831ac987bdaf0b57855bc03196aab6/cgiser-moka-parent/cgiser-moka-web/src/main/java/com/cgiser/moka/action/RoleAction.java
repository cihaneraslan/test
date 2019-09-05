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

public class RoleAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String userIden = ServletUtil.getDefaultValue(request, "uniden",
		"");
		String serverId = ServletUtil.getDefaultValue(request, "serverid",
		"");
//		String x = ServletUtil.getDefaultValue(request, "geoX", "0");
//		String y = ServletUtil.getDefaultValue(request, "geoY", "0");
//		LocationManager locationManager = (LocationManager)HttpSpringUtils.getBean("locationManager");
//		String city = locationManager.getCityByXY(x, y);
		ReturnType<List<Role>> returnType = new ReturnType<List<Role>>();
		try{
			if(StringUtils.isBlank(userIden)){
				logger.info("获取角色失败");
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("获取角色失败");
				super.printReturnType2Response(response, returnType);
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
			List<Role> roleList = roleManager.getRolesByUserIden(userIden,new Long(serverId));
			if(CollectionUtils.isEmpty(roleList)){
				logger.info("获取角色失败");
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("获取角色失败");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			returnType.setValue(roleList);
			returnType.setStatus(1);
			returnType.setMsg("获取角色成功");
//			response.addCookie(new Cookie(cookieId, userIden));
			super.printReturnType2Response(response, returnType);
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
