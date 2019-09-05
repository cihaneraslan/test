package com.cgiser.moka.legion.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class ApplyDutyAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<String> returnType = new ReturnType<String>();
		try{
			Role role = super.getCurrentRole(request);
			String duty = ServletUtil.getDefaultValue(request, "duty", "");
			String legionerId = ServletUtil.getDefaultValue(request, "id", "");
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(!StringUtils.isNumeric(duty)){
				returnType.setMsg("职位不存在哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(!StringUtils.isNumeric(legionerId)){
				returnType.setMsg("帮众不存在哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			LegionManager legionManager = (LegionManager)HttpSpringUtils.getBean("legionManager");
			Legioner legioner1 = legionManager.getLegionerBylegionerId(new Long(legionerId));
			if(legioner1==null){
				returnType.setMsg("帮众不存在哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
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
				returnType.setMsg("只有帮主和副帮主有权限任职哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(Integer.parseInt(duty)>5||Integer.parseInt(duty)<1){
				returnType.setMsg("职位不存在哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(Integer.parseInt(duty)<=legioner.getDuty()){
				returnType.setMsg("您没有权限任职哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(Integer.parseInt(duty)<5){
				List<Legioner> legioners = legionManager.getLegionersByduty(legion.getId(), Integer.parseInt(duty));
				if(CollectionUtils.isEmpty(legioners)||legioners.size()<Legion.legionduty[Integer.parseInt(duty)]){
					legionManager.updateLegionerDuty(new Long(legionerId), Integer.parseInt(duty));
				}else{
					Long res = 9000000000L;
					Long legionerId1 = 0L;
					for(Legioner legioner2:legioners){
						if(legioner2.getContribute()<res){
							res = legioner2.getContribute();
							legionerId1 = legioner2.getId();
						}
					}
					if(legionerId1>0){
						legionManager.updateLegionerDuty(legionerId1, 5);
						legionManager.updateLegionerDuty(new Long(legionerId), Integer.parseInt(duty));
					}
				}
			}else{
				legionManager.updateLegionerDuty(new Long(legionerId), Integer.parseInt(duty));
			}
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
