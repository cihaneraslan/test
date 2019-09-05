package com.cgiser.moka.map.action;

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
import com.cgiser.moka.manager.StageLevelManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class AddStageRewardAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<Long> returnType = new ReturnType<Long>();
		try{
			String stageLevelId = ServletUtil.getDefaultValue(request, "stageLevelId", "");
			String type = ServletUtil.getDefaultValue(request, "type", "");
			String value = ServletUtil.getDefaultValue(request, "value", "");
			String color = ServletUtil.getDefaultValue(request, "color", "");
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(StringUtils.isEmpty(type)||StringUtils.isEmpty(stageLevelId)||StringUtils.isEmpty(value)||StringUtils.isEmpty(color)){
				returnType.setMsg("参数有误！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			StageLevelManager stageLevelManager = (StageLevelManager)HttpSpringUtils.getBean("stageLevelManager");
			Long id = stageLevelManager.addStageLevelColor(Integer.parseInt(stageLevelId), Integer.parseInt(type), Integer.parseInt(value), new Float(color));
			if(id<1){
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			returnType.setStatus(1);
			returnType.setValue(id);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
