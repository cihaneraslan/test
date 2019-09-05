package com.cgiser.moka.fight.legion.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.model.LegionFighter;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class GetDefendAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<LegionFighter> returnType = new ReturnType<LegionFighter>();
		String city = ServletUtil.getDefaultValue(request, "city", null);
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(null==city){
				returnType.setMsg("参数不正确");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			
//			LegionFightManager legionFightManager = (LegionFightManager)HttpSpringUtils.getBean("legionFightManager");
//			LegionFighter legionFighter = legionFightManager.getDefend(city, role);
			returnType.setStatus(1);
//			returnType.setValue(legionFighter);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
