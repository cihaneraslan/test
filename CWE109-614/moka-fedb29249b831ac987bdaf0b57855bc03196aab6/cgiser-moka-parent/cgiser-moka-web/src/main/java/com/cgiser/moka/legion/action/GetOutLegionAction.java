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
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionEventEnum;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class GetOutLegionAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String id = ServletUtil.getDefaultValue(request,"role","");
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
			if(legioner.getDuty()!=1&&legioner.getDuty()!=2){
				returnType.setMsg("只有帮主和副帮主有权限踢人哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			Legioner outLegioner = legionManager.getLegioner(new Long(id));
			if(outLegioner==null){
				returnType.setMsg("该成员不存在");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
//			if(outLegioner.getDuty()-legioner.getDuty()<3){
//				returnType.setMsg("您没有权限哦！");
//				returnType.setStatus(0);
//				super.printReturnType2Response(response, returnType);
//				return null;
//			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			Role outRole = roleManager.getRoleById(outLegioner.getRoleId());
			if(legionManager.outLegion(outLegioner.getId())<1){	
				returnType.setMsg("踢出帮派失败！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			legionManager.saveLegionEvent(legioner.getLegionId(),legioner.getRoleId(), role.getRoleName()+"把"+outRole.getRoleName()+"踢出了帮派", LegionEventEnum.GETOUTLEGION.getCode());
			if(CollectionUtils.isEmpty(legionManager.getLegioner(legion.getId(), 1, 1))){
				legionManager.breakupLegion(legioner.getLegionId());
			}
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
