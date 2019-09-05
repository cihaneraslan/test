package com.cgiser.moka.buy.action;

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
import com.cgiser.moka.model.LoginAward;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class BuyEnergyAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<List<LoginAward>> returnType = new ReturnType<List<LoginAward>>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
//			if(role.getEnergy()>role.getEnergyMax()){
//				returnType.setMsg("您的体力已满不用购买！");
//				returnType.setStatus(0);
//				super.printReturnType2Response(response, returnType);
//				return null;
//			}
//			if(role.getEnergyBuyCount()>=5){
//				returnType.setMsg("您今天已经购买了5次体力！");
//				returnType.setStatus(0);
//				super.printReturnType2Response(response, returnType);
//				return null;
//			}
			if(role.getCash()<50){
				returnType.setMsg("您的元宝不够哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(roleManager.updateCash(role.getRoleName(), 50)){
				if(!roleManager.addEnergy(role.getRoleName(), 20)){
					logger.error("["+role.getRoleName()+"]购买体力成功，但是增加体力失败");
				}
			}else{
				logger.error("["+role.getRoleName()+"]购买体力失败");
			}
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
