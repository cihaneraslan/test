package com.cgiser.moka.soul.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.SoulManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.Soul;
import com.cgiser.moka.result.ReturnType;
import com.cgiser.moka.result.SoulResult;

public class RefreshRandomSoulAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<SoulResult> returnType = new ReturnType<SoulResult>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			Long time = role.getSoulRefreshTime();
			Date date = new Date();
			List<Soul> souls = new ArrayList<Soul>();
			if(time==null||(date.getTime() - time>=12*60*60*1000)){
				SoulManager soulManager = (SoulManager)HttpSpringUtils.getBean("soulManager");
				Soul soul1 = soulManager.randomSoul(0,0);
				Soul soul2 = soulManager.randomSoul(soul1.getSoulId(),0);
				souls.add(soul1);
				souls.add(soul2);
				if(role.getVip()>=2){
					Soul soul3 = soulManager.randomSoul(soul1.getSoulId(),soul2.getSoulId());
					souls.add(soul3);
				}
				RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
				role.setSoulRefreshTime(date.getTime());
				roleManager.updateRoleSoulRefreshTime(role.getRoleId(), date);
				role.setSouls(souls);
				roleManager.updateRoleCache(role);
			}else{
				if(role.getCash()<100){
					returnType.setMsg("您的元宝不够哦！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}else{
					RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
					if(!roleManager.updateCash(role.getRoleName(), 100)){
						logger.error("用户刷新武器减元宝失败");
						returnType.setMsg("对不起，暂时不能刷新哦！");
						returnType.setStatus(0);
						super.printReturnType2Response(response, returnType);
						return null;
					}
					SoulManager soulManager = (SoulManager)HttpSpringUtils.getBean("soulManager");
					Soul soul1 = soulManager.randomSoul(0,0);
					Soul soul2 = soulManager.randomSoul(soul1.getSoulId(),0);
					souls.add(soul1);
					souls.add(soul2);
					if(role.getVip()>=2){
						Soul soul3 = soulManager.randomSoul(soul1.getSoulId(),soul2.getSoulId());
						souls.add(soul3);
					}
					role.setSoulRefreshTime(date.getTime());
					roleManager.updateRoleSoulRefreshTime(role.getRoleId(), date);
					role.setSouls(souls);
					roleManager.updateRoleCache(role);
				}
			}
			SoulResult result = new SoulResult();
			result.setRefreshCash(100);
			result.setSouls(souls);
			result.setSoulRefreshTime((12*60*60*1000-(date.getTime() - role.getSoulRefreshTime()))/1000);
			returnType.setStatus(1);
			returnType.setValue(result);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
