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

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.model.LegionApply;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class GetLegionApplyAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String index = ServletUtil.getDefaultValue(request,"index","");
		String count = ServletUtil.getDefaultValue(request,"count","");
		ReturnType<List<LegionApply>> returnType = new ReturnType<List<LegionApply>>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(StringUtils.isBlank(index)||!StringUtils.isNumeric(index)){
				index = "1";
			}
			if(StringUtils.isBlank(count)||!StringUtils.isNumeric(count)){
				count = "10";
			}
			LegionManager legionManager = (LegionManager)HttpSpringUtils.getBean("legionManager");
			Legioner legioner = legionManager.getLegioner(role.getRoleId());
			if(legioner.getDuty()!=1&&legioner.getDuty()!=2&&legioner.getDuty()!=3){
				returnType.setMsg("只有帮主和副帮主可以获取申请哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			List<LegionApply> listLegionApply = legionManager.getLegionApply(legioner.getLegionId(), Integer.parseInt(index), Integer.parseInt(count));
			returnType.setStatus(1);
			returnType.setValue(listLegionApply);
			super.printReturnType2Response(response, returnType);
			
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
