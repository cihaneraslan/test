package com.cgiser.moka.action;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.moka.manager.support.DateUtils;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class GetLoginRoleAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		ReturnType<Role> returnType = new ReturnType<Role>();
		try{
			Role role = super.getCurrentRole(request);
			if(role==null){
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("您还没登录哦!");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			Date lastRecive = role.getLastReceiveEnergy();
			Date date = new Date();
			int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			boolean flag = false;
			if ((hour >= 12 && hour < 14)
					|| (hour >= 18 && hour < 20)) {
				if (lastRecive == null) {
					flag = true;
				}else if (DateUtils.isSameDay(lastRecive, date)) {
					if (hour >= 18 && hour < 20) {
						if (lastRecive.getHours() >= 18
								&& lastRecive.getHours() < 20) {
							flag = false;
						} else {
							flag = true;
						}
					}
					if (hour >= 12 && hour < 14) {
						if (lastRecive.getHours() >= 12
								&& lastRecive.getHours() < 14) {
							flag = false;
						} else {
							flag = true;
						}
					}
				} else {
					flag = true;
				}
			}
			role.setEnergyCanRecive(flag ? 1 : 0);
			returnType.setValue(role);
			returnType.setStatus(1);
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
