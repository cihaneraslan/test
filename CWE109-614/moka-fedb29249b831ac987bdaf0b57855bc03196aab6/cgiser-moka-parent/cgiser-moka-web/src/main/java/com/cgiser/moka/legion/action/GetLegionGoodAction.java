package com.cgiser.moka.legion.action;

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
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionGood;
import com.cgiser.moka.model.LegionTech;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class GetLegionGoodAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<List<LegionGood>> returnType = new ReturnType<List<LegionGood>>();
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
			List<LegionGood> legionGoods = legionManager.getLegionGoods();
			//tech11代表的是帮派商城
			LegionTech tech = legionManager.getLegionTechByTechId("tech11");
			if(legion.getLegionLevel()>=tech.getLegionLevel()){
				if(!CollectionUtils.isEmpty(legionGoods)){
					for(LegionGood legionGood:legionGoods){
						if(legionGood.getTechLevel()<=tech.getLegionTechLevel(legion.getContribute11())){
							legionGood.setLock(1);
						}
					}
				}
			}

			returnType.setValue(legionGoods);
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
