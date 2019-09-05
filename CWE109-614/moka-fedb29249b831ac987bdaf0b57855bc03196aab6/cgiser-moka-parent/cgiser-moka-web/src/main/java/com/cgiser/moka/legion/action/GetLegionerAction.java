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

public class GetLegionerAction extends AbstractAction {

	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String index = ServletUtil.getDefaultValue(request,"index","");
		String count = ServletUtil.getDefaultValue(request,"count","");
		ReturnType<List<Legioner>> returnType = new ReturnType<List<Legioner>>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(StringUtils.isBlank(index)||!StringUtils.isNumeric(index)){
				logger.debug("获取帮派参数错误");
				index = "1";
			}
			if(StringUtils.isBlank(count)||!StringUtils.isNumeric(count)){
				logger.debug("获取帮派参数错误");
				count = "10";
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
			List<Legioner> list = legionManager.getLegioner(legioner.getLegionId(), Integer.parseInt(index), Integer.parseInt(count));
			if(CollectionUtils.isEmpty(list)){				
				returnType.setMsg("帮派没有成员！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			returnType.setStatus(1);
			returnType.setValue(list);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
