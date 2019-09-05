package com.cgiser.moka.card.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.UserCardManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.StrengResult;
import com.cgiser.moka.result.ReturnType;

public class StrengCardPreViewAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String userCardId = ServletUtil.getDefaultValue(request, "UserCardId1", null);
		String userCardIds = ServletUtil.getDefaultValue(request, "UserCardId2", null);
		ReturnType<StrengResult> returnType = new ReturnType<StrengResult>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(userCardId==null||userCardIds==null){
				returnType.setMsg("参数有误！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			UserCardManager userCardManager = (UserCardManager)HttpSpringUtils.getBean("userCardManager");
			StrengResult result = userCardManager.StrengCardPreView(new Long(userCardId), userCardIds);
			if(result==null){
				returnType.setMsg("强化预览失败！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			returnType.setStatus(1);
			returnType.setValue(result);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
