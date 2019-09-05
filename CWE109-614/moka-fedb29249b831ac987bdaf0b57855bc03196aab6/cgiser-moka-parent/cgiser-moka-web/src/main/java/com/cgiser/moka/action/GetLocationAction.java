package com.cgiser.moka.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import com.cgiser.moka.manager.LocationManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.message.netty.ReceiverHandler;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class GetLocationAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<List<Role>> returnType = new ReturnType<List<Role>>();
		try {
			Role role = this.getCurrentRole(request);
			if(role==null){
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("您还没登录哦");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			LocationManager locationManager = (LocationManager)HttpSpringUtils.getBean("locationManager");
			if(StringUtils.isEmpty(role.getCity())){
				role.setCity(locationManager.getCityByXY(String.valueOf(role.getX()), String.valueOf(role.getY())));
			}
			Set<String> roleNames = ReceiverHandler.allChannels.getRoleNamesByCity(role.getCity());
			List<Role> roles = new ArrayList<Role>();
			if(CollectionUtils.isEmpty(roleNames)||roleNames.size()<20){
				RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
				roles = roleManager.getRoleByCity(role.getCity(), 20-(roleNames==null?0:roleNames.size()));
			}
			if(CollectionUtils.isEmpty(roleNames)&&CollectionUtils.isEmpty(roles)){
				returnType.setValue(null);
				returnType.setStatus(1);
				returnType.setMsg("附近没有人哦!");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
//			List<Role> roleList = new ArrayList<Role>();
			if(roles==null){
				roles = new ArrayList<Role>();
			}
			if(CollectionUtils.isEmpty(roleNames)){
				roleNames = new HashSet<String>();
			}
			Iterator<String> ite = roleNames.iterator();
			Role dRole;
			String roleName = "";
			while(ite.hasNext()){
				roleName = ite.next();
				if(!this.contains(roles, roleName)){
					dRole = roleManager.getRoleByName(roleName);
					roles.add(dRole);
				}
				if(roles.size()==20){
					break;
				}
			}
			if(CollectionUtils.isEmpty(roles)){
				returnType.setValue(null);
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}else{
				returnType.setValue(null);
				returnType.setStatus(1);
				returnType.setValue(roles);
				super.printReturnType2Response(response, returnType);
				return null;
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		return null;
	}
	private boolean contains(List<Role> roleList,String roleName){
		for(Role role:roleList){
			if(role.getRoleName().equals(roleName)){
				return true;
			}
		}
		return false;
	}
}
