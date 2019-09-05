package com.cgiser.moka.legion.action;

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
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionEventEnum;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class OutLegionAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<String> returnType = new ReturnType<String>();
		try{
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
			if(legionManager.outLegion(legioner.getId())<1){				
				returnType.setMsg("退出帮派失败！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			legionManager.saveLegionEvent(legioner.getLegionId(),legioner.getRoleId(), role.getRoleName()+"退出了帮派", LegionEventEnum.OUTLEGION.getCode());
			if(legioner.getDuty()==1){
				Legioner dLegioner = legionManager.getDeputyHeader(legioner.getLegionId());
				if(dLegioner!=null){
					legionManager.updateHeader(legioner.getLegionId(),dLegioner.getRoleId());
					Role dRole = roleManager.getRoleById(dLegioner.getRoleId());
					legionManager.saveLegionEvent(legioner.getLegionId(),legioner.getRoleId(), role.getRoleName()+"退出了帮派，"+dRole.getRoleName()+"继任了帮主", LegionEventEnum.OUTLEGION.getCode());
				}else{
					Legioner mLegioner = legionManager.getMaxContributeLegioner(legioner.getLegionId());
					if(mLegioner!=null){
						legionManager.updateHeader(legioner.getLegionId(),mLegioner.getRoleId());
						Role dRole = roleManager.getRoleById(mLegioner.getRoleId());
						legionManager.saveLegionEvent(legioner.getLegionId(),legioner.getRoleId(), role.getRoleName()+"退出了帮派，"+dRole.getRoleName()+"继任了帮主", LegionEventEnum.OUTLEGION.getCode());
					}
				}
			}
			if(CollectionUtils.isEmpty(legionManager.getLegioner(legion.getId(), 1, 1))){
				legionManager.breakupLegion(legioner.getLegionId());
				legionManager.saveLegionEvent(legioner.getLegionId(),legioner.getRoleId(), role.getRoleName()+"退出了帮派，帮派被解散了", LegionEventEnum.BREAKEUPLEGION.getCode());
			}
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
