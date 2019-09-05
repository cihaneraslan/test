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
import org.springframework.util.CollectionUtils;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.SoulManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.Soul;
import com.cgiser.moka.result.ReturnType;
import com.cgiser.moka.result.SoulResult;

public class GetRandomSoulAction extends AbstractAction {
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
			if(CollectionUtils.isEmpty(role.getSouls())||time==null||(date.getTime() - time>=12*60*60*1000)){
				SoulManager soulManager = (SoulManager)HttpSpringUtils.getBean("soulManager");
				String[] freshStep = role.getFreshStep();
				StringBuffer strSoul = new StringBuffer();
				if(Integer.parseInt(freshStep[8])<8){
					Soul soul1 = soulManager.getSoulById(1);
					Soul soul2 = soulManager.getSoulById(21);
					souls.add(soul1);
					souls.add(soul2);
					strSoul.append("1").append("_");
					strSoul.append("21");
					if(role.getVip()>=2){
						Soul soul3 = soulManager.randomSoul(soul1.getSoulId(),soul2.getSoulId());
						souls.add(soul3);
						strSoul.append("_").append(soul3.getSoulId());
					}
				}else{
					Soul soul1 = soulManager.randomSoul(0,0);
					Soul soul2 = soulManager.randomSoul(soul1.getSoulId(),0);
					souls.add(soul1);
					souls.add(soul2);
					strSoul.append(soul1.getSoulId()).append("_");
					strSoul.append(soul2.getSoulId());
					if(role.getVip()>=2){
						Soul soul3 = soulManager.randomSoul(soul1.getSoulId(),soul2.getSoulId());
						souls.add(soul3);
						strSoul.append("_").append(soul3.getSoulId());
					}
				}
				
				RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
				role.setSoulRefreshTime(date.getTime());
				roleManager.updateRoleSoulRefreshTime(role.getRoleId(), date);
				roleManager.updateRoleSoul(role.getRoleId(), strSoul.toString());
				role.setSouls(souls);
				roleManager.updateRoleCache(role);
			}else{
				souls.addAll(role.getSouls());
				if(!CollectionUtils.isEmpty(souls)){
					if(souls.size()<3&&role.getVip()>=2){
						SoulManager soulManager = (SoulManager)HttpSpringUtils.getBean("soulManager");
						Soul soul3 = soulManager.randomSoul(souls.get(0).getSoulId(),souls.get(1).getSoulId());
						souls.add(soul3);
						role.setSouls(souls);
						RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
						roleManager.updateRoleCache(role);
					}
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
