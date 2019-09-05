package com.cgiser.moka.fane.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.manager.FragmentManager;
import com.cgiser.moka.model.Fragment;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ExChangeResult;
import com.cgiser.moka.result.ReturnType;

public class ExChangeListAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<ExChangeResult> returnType = new ReturnType<ExChangeResult>();
		try{
			Role role = super.getCurrentRole(request);
			if(role==null){
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("您还没登录哦!");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			ExChangeResult result = new ExChangeResult();
			FragmentManager fragmentManager = (FragmentManager)HttpSpringUtils.getBean("fragmentManager");
			Fragment fragment = fragmentManager.getFragment(role.getRoleId());
			result.setFragment_3(fragment.getFragment1());
			result.setFragment_4(fragment.getFragment2());
			result.setFragment_5(fragment.getFragment3());
			returnType.setValue(result);
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
			return null;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
