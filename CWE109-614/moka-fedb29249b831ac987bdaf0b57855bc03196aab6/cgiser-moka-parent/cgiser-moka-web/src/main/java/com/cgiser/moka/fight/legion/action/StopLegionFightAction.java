package com.cgiser.moka.fight.legion.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.manager.LegionFightManager;
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionFight;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class StopLegionFightAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ReturnType<Object> returnType = new ReturnType<Object>();
		try {
			Role role = super.getCurrentRole(request);
			if (null == role) {
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			LegionManager legionManager = (LegionManager) HttpSpringUtils
					.getBean("legionManager");
			Legioner legioner = legionManager.getLegioner(role.getRoleId());
			if (legioner == null) {
				returnType.setMsg("您还没加入帮派哦");
				returnType.setStatus(0);
				returnType.setValue(1);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			LegionFightManager legionFightManager = (LegionFightManager) HttpSpringUtils
					.getBean("legionFightManager");
			Legion attack = legionManager.getLegionById(legioner.getLegionId());
			LegionFight legionFight = legionFightManager
					.getLegionFightByattack(attack.getId());
			if (legionFight != null && !legionFightManager.isEnd(legionFight)) {
				if(!legionFight.getStartId().equals(role.getRoleId())){
					returnType.setMsg("只有发起者才能结束帮派战");
					returnType.setValue(0);
					returnType.setStatus(0);
					return null;
				}
				legionFightManager.end(legionFight);
				returnType.setMsg("成功结束帮派战");
				returnType.setValue(1);
				returnType.setStatus(1);
				super.printReturnType2Response(response, returnType);
				return null;
			}else{
				returnType.setMsg("帮派战已结束");
				returnType.setValue(1);
				returnType.setStatus(1);
				return null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
