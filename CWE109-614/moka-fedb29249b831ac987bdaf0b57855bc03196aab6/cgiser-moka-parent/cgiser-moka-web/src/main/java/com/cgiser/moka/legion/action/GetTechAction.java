package com.cgiser.moka.legion.action;

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
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.manager.support.BeanUtils;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionTech;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class GetTechAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<List<LegionTech>> returnType = new ReturnType<List<LegionTech>>();
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
			List<LegionTech> techs = legionManager.getLegionTechs();
			LegionTech legionTech;
			for(int i=0;i<techs.size();i++){
				legionTech = techs.get(i);
				if(legion.getLegionLevel()>=legionTech.getLegionLevel()){
					legionTech.setLock(1);
				}else{
					legionTech.setLock(0);
				}
				Long resource = (Long)BeanUtils.getFieldValueByName("contribute"+legionTech.getTechId().replace("tech", ""), legion);
				int level = legionTech.getLegionTechLevel(resource);
				legionTech.setContribute(resource - legionTech.getContributeByLevel(level));
				legionTech.setNextContribute(new Long(legionTech.getContributeByLevel(level+1)-legionTech.getContributeByLevel(level)));
				legionTech.setTechLevel(level);
				legionTech.setTechEffect(legionTech.getTechEffect().replace("{?}", String.valueOf((int)(legionTech.getTechLevel()*legionTech.getAnd()))));
			}
			returnType.setValue(techs);
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
