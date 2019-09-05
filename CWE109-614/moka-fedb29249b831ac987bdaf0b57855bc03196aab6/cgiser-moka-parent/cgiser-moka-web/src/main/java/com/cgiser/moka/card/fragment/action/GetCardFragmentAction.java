package com.cgiser.moka.card.fragment.action;

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
import com.cgiser.moka.manager.CardFragmentManager;
import com.cgiser.moka.model.CardFragment;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class GetCardFragmentAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<List<CardFragment>> returnType = new ReturnType<List<CardFragment>>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			CardFragmentManager cardFragmentManager = (CardFragmentManager)HttpSpringUtils.getBean("cardFragmentManager");
			List<CardFragment> cardFragments = cardFragmentManager.getCardFragment();
			returnType.setStatus(1);
			returnType.setValue(cardFragments);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
