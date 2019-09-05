package com.cgiser.moka.legion.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class DonateAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<String> returnType = new ReturnType<String>();
		try{
			String strId = ServletUtil.getDefaultValue(request, "id", "");
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
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
			int id = Integer.parseInt(strId);
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			if(id==1){
				if(legioner.getLastContribute1()>0){
					if(legioner.getLastContribute1()>=2){
						legionManager.addRoleContributeHonor(legioner.getId(), 40, 40);
						legionManager.addLegionResources(legion.getId(), 40);
						legionManager.updateRoleLegionContribute(legioner.getId(), id);
					}else if(legioner.getLastContribute1()==1){
						if(roleManager.updateCoin(role.getRoleName(), 40000)){
							legionManager.addRoleContributeHonor(legioner.getId(), 40, 40);
							legionManager.addLegionResources(legion.getId(), 40);
							legionManager.updateRoleLegionContribute(legioner.getId(), id);
						}else{
							returnType.setMsg("您的铜钱不够哦！");
							returnType.setStatus(0);
							super.printReturnType2Response(response, returnType);
							return null;
						}
					}
				}else{
					returnType.setMsg("您的敲鼓次数用完了哦！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
				
			}else if(id==2){
				if(legioner.getLastContribute2()>0){
					if(roleManager.updateCash(role.getRoleName(), 50)){
						legionManager.addRoleContributeHonor(legioner.getId(), 500, 500);
						legionManager.addLegionResources(legion.getId(), 500);
						legionManager.updateRoleLegionContribute(legioner.getId(), id);
					}else{
						returnType.setMsg("您的元宝不够哦！");
						returnType.setStatus(0);
						super.printReturnType2Response(response, returnType);
						return null;
					}
				}else{
					returnType.setMsg("您的敲鼓次数用完了哦！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}

			}else if(id==3){
				if(legioner.getLastContribute3()>0){
					if(roleManager.updateCash(role.getRoleName(), 100)){
						legionManager.addRoleContributeHonor(legioner.getId(), 1200, 1200);
						legionManager.addLegionResources(legion.getId(), 1200);
						legionManager.updateRoleLegionContribute(legioner.getId(), id);
					}else{
						returnType.setMsg("您的元宝不够哦！");
						returnType.setStatus(0);
						super.printReturnType2Response(response, returnType);
						return null;
					}
				}else{
					returnType.setMsg("您的敲鼓次数用完了哦！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
				
			}
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
