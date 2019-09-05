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

public class DeleteStageRewardAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<Long> returnType = new ReturnType<Long>();
		try{
			String colorId = ServletUtil.getDefaultValue(request, "colorId", "");
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(StringUtils.isEmpty(colorId)){
				returnType.setMsg("参数有误！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			StageLevelManager stageLevelManager = (StageLevelManager)HttpSpringUtils.getBean("stageLevelManager");
			int id = stageLevelManager.deleteStageLevelColor(new Long(colorId));
			if(id<1){
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
