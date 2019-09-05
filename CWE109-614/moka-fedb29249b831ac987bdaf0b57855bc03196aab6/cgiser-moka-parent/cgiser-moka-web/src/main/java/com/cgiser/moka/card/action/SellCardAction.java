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
import com.cgiser.moka.manager.UserRuneManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.StrengResult;
import com.cgiser.moka.result.ReturnType;

public class SellCardAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String userRuneIds = ServletUtil.getDefaultValue(request, "Runes", null);
		String userCardIds = ServletUtil.getDefaultValue(request, "Cards", null);
		ReturnType<StrengResult> returnType = new ReturnType<StrengResult>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦,亲！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(userRuneIds==null&&userCardIds==null){
				returnType.setMsg("先把要卖的东西放出来吧,亲！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(userCardIds!=null){
				UserCardManager userCardManager = (UserCardManager)HttpSpringUtils.getBean("userCardManager");
				returnType.setStatus(userCardManager.SellCard(role.getRoleId(),userCardIds));
			}
			if(userRuneIds!=null){
				UserRuneManager userRuneManager = (UserRuneManager)HttpSpringUtils.getBean("userRuneManager");
				returnType.setStatus(userRuneManager.SellRune(role.getRoleId(),userRuneIds));
			}
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
