package com.cgiser.moka.lan.action;

import java.util.Map;

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
import com.cgiser.moka.manager.LanguageManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class GetLanAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String type  = ServletUtil.getDefaultValue(request, "chs", "");
		ReturnType<Map<String,String>> returnType = new ReturnType<Map<String,String>>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(StringUtils.isBlank(type)){
				returnType.setMsg("参数有误！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			LanguageManager languageManager = (LanguageManager)HttpSpringUtils.getBean("languageManager");
			Map<String,String> map = languageManager.getLanByLanType(type);
			returnType.setStatus(1);
			returnType.setValue(map);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
