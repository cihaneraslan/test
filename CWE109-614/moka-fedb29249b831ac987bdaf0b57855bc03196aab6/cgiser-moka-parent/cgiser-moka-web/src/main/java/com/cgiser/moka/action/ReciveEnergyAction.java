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

import com.cgiser.core.common.cache.mem.MemCachedManager;
import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.support.DateUtils;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class ReciveEnergyAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<String> returnType = new ReturnType<String>();
		MemCachedManager energyCachedManager = (MemCachedManager)HttpSpringUtils.getBean("energyCachedManager");
		Role role = this.getCurrentRole(request);
		try{
			if(role==null){
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("您还没登录哦");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			Date lastRecive = role.getLastReceiveEnergy();
			Date date = new Date();
			int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			boolean flag = false;
			if((hour>=12&&hour<14)||(hour>=18&&hour<20)){
				if(!energyCachedManager.add("energy_"+String.valueOf(role.getRoleId()), 2*60*60, date)){
					returnType.setValue(null);
					returnType.setStatus(0);
					returnType.setMsg("您已经领取过了哦");
					super.printReturnType2Response(response, returnType);
					return null;
				}
				if(lastRecive==null){
					flag =true;
				}else if(DateUtils.isSameDay(lastRecive, date)){
					if(hour>=18&&hour<20){
						if(lastRecive.getHours()>=18&&lastRecive.getHours()<20){
							flag =false;
							returnType.setValue(null);
							returnType.setStatus(0);
							returnType.setMsg("您已经领取过了哦");
							super.printReturnType2Response(response, returnType);
							return null;
						}else{
							flag =true;
						}
					}
					if(hour>=12&&hour<14){
						if(lastRecive.getHours()>=12&&lastRecive.getHours()<14){
							flag =false;
							returnType.setValue(null);
							returnType.setStatus(0);
							returnType.setMsg("您已经领取过了哦");
							super.printReturnType2Response(response, returnType);
							return null;
						}else{
							flag =true;
						}
					}
				}else{
					flag = true;
				}
			}else{
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("领取体力的时间已经过了哦");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(flag){
				int max = role.getEnergyMax();
				int energy = role.getEnergy();
				int recive = 20;
				if(role.getVip()==1){
					recive = 25;
				}
				if(role.getVip()==2){
					recive = 30;
				}
				if(role.getVip()==3){
					recive = 35;
				}
				if(max<=energy){
					energyCachedManager.delete("energy_"+String.valueOf(role.getRoleId()));
					returnType.setValue(null);
					returnType.setStatus(0);
					returnType.setMsg("您的体力已满");
					super.printReturnType2Response(response, returnType);
					return null;
				}else{
					if((max - energy)<recive){
						recive = max - energy;
					}
					RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
					roleManager.addEnergy(role.getRoleName(), recive);
					role = roleManager.getRoleByName(role.getRoleName());
					role.setLastReceiveEnergy(date);
					roleManager.updateRoleLastReceiveEnergy(role.getRoleId(), date);
					returnType.setValue(""+recive);
					returnType.setStatus(1);
					returnType.setMsg("领取体力成功");
					super.printReturnType2Response(response, returnType);
					return null;
				}
			}else{
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("领取体力的时间已经过了哦");
				super.printReturnType2Response(response, returnType);
				return null;
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			energyCachedManager.delete("energy_"+String.valueOf(role.getRoleId()));
		}
		return null;
		
	}
}
