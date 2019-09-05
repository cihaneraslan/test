package com.cgiser.moka.version.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.manager.VersionManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.Version;
import com.cgiser.moka.result.ReturnType;


public class ResetVersionAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			ReturnType<Version> returnType = new ReturnType<Version>();
			Role role = super.getCurrentRole(request);
			if(role==null){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			VersionManager versionManager = (VersionManager)HttpSpringUtils.getBean("versionManager");
			Version version = versionManager.getLastVersion();
			AbstractAction.version = version;
			returnType.setStatus(1);
			returnType.setValue(version);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}

		return null;
	}
}
