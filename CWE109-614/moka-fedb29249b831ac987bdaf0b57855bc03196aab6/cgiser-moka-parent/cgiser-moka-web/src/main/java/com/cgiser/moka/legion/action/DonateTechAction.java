package com.cgiser.moka.legion.action;

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
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.support.BeanUtils;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionTech;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class DonateTechAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<String> returnType = new ReturnType<String>();
		try{
			String strTech = ServletUtil.getDefaultValue(request, "tech", "");
			String strType = ServletUtil.getDefaultValue(request, "type", "");
			String strNum = ServletUtil.getDefaultValue(request, "num", "");
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(!StringUtils.isNumeric(strTech)||!StringUtils.isNumeric(strType)||!StringUtils.isNumeric(strNum)){
				returnType.setMsg("参数有误哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			LegionManager legionManager = (LegionManager)HttpSpringUtils.getBean("legionManager");
			Legioner legioner = legionManager.getLegioner(role.getRoleId());
			if(legioner==null){
				returnType.setMsg("您还没加入帮派哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			Legion legion  = legionManager.getLegionById(legioner.getLegionId());
			if(legion==null){
				returnType.setMsg("帮派不存在！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			int type = Integer.parseInt(strType);
			int tech = Integer.parseInt(strTech);
			int num = Integer.parseInt(strNum);
			if(tech>11||tech<1){
				returnType.setMsg("该科技还未解锁！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			LegionTech legionTech = legionManager.getLegionTechByTechId("tech"+tech);
			if(legion.getLegionLevel()<legionTech.getLegionLevel()){
				returnType.setMsg("该科技还未解锁！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			if(type==1){
				Long contribute = num/1000+(Long)BeanUtils.getFieldValueByName("contribute"+tech, legion);
				if(legionTech.getLegionTechLevel(contribute)>legion.getLegionLevel()){
					returnType.setMsg("科技的等级不能超过帮派等级哦！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
				if(roleManager.updateCoin(role.getRoleName(), num)){
					legionManager.addRoleContributeHonor(legioner.getId(), num/1000, num/1000);
					legionManager.addLegionContribute(legion.getId(), num/1000, tech);
				}else{
					returnType.setMsg("您的铜钱不够哦！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
			}else if(type==2){
				Long contribute = num*10+(Long)BeanUtils.getFieldValueByName("contribute"+tech, legion);
				if(legionTech.getLegionTechLevel(contribute)>legion.getLegionLevel()){
					returnType.setMsg("科技的等级不能超过帮派等级哦！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
				if(roleManager.updateCash(role.getRoleName(), num)){
					legionManager.addRoleContributeHonor(legioner.getId(), num*10, num*10);
					legionManager.addLegionContribute(legion.getId(), num*10, tech);
				}else{
					returnType.setMsg("您的元宝不够哦！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
			}else if(type==3){
				returnType.setMsg("暂不支持该货币类型的捐献！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
