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
import com.cgiser.moka.manager.LocationManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.message.netty.ReceiverHandler;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class SaveLocationAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<String> returnType = new ReturnType<String>();
		try {
			String x = ServletUtil.getDefaultValue(request, "geoX", null); 
			String y = ServletUtil.getDefaultValue(request, "geoY", null); 
			Role role = this.getCurrentRole(request);
			if(role==null){
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("您还没登录哦");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(x==null||y==null){
				returnType.setValue(null);
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(x.equals("0")||y.equals("0")){
				returnType.setValue(null);
				returnType.setStatus(1);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			String city = role.getCity();
			
			LocationManager locationManager = (LocationManager)HttpSpringUtils.getBean("locationManager");
			Long locationid = locationManager.saveRoleLocation(this.getCurrentRole(request), new Double(x), new Double(y));
			String city1 = locationManager.getCityByForBaiDu(x, y);
			net.sf.json.JSONObject obj = net.sf.json.JSONObject.fromObject(city1);
			city1 = obj.getJSONObject("result").getJSONObject("addressComponent").getString("city");
			String address = obj.getJSONObject("result").getString("formatted_address");
			if(!city.equals(address)){
				RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
				role.setCity(address);
				roleManager.updateRoleCity(role.getRoleId(), address);
			}
			
			role.setX(new Double(x));
			role.setY(new Double(y));
			ReceiverHandler.allChannels.updateRoleCity(city, role);
			if(locationid>0){
				returnType.setValue(null);
				returnType.setStatus(1);
				super.printReturnType2Response(response, returnType);
				return null;
			}else{
				returnType.setValue(null);
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		return null;
	}
}
