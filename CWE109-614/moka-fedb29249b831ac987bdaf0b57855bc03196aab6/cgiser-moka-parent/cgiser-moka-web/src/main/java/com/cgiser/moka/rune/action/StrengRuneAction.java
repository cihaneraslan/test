package com.cgiser.moka.rune.action;

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
import com.cgiser.moka.manager.UserRuneManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.StrengResult;
import com.cgiser.moka.result.ReturnType;

public class StrengRuneAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String userRuneId = ServletUtil.getDefaultValue(request, "UserRuneId1", null);
		String userRuneIds = ServletUtil.getDefaultValue(request, "UserRuneId2", null);
		ReturnType<StrengResult> returnType = new ReturnType<StrengResult>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(userRuneId==null||userRuneIds==null){
				returnType.setMsg("参数有误！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			UserRuneManager userRuneManager = (UserRuneManager)HttpSpringUtils.getBean("userRuneManager");
			StrengResult result = userRuneManager.StrengRunePreView(Integer.parseInt(userRuneId), userRuneIds);
			if(result==null){
				returnType.setMsg("强化失败！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			role = super.getCurrentRole(request);
			if(result.getCoins()>role.getCoins()){
				returnType.setMsg("您的铜钱不够哦");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			result = userRuneManager.StrengRune(Integer.parseInt(userRuneId), userRuneIds);
			if(result==null){
				returnType.setMsg("强化失败！");
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
