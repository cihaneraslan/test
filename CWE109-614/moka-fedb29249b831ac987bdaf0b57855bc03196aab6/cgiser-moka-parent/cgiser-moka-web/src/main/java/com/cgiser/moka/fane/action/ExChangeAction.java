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
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.FragmentManager;
import com.cgiser.moka.manager.RuneManager;
import com.cgiser.moka.manager.UserRuneManager;
import com.cgiser.moka.model.Fragment;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.Rune;
import com.cgiser.moka.result.ReturnType;

public class ExChangeAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String id = ServletUtil.getDefaultValue(request, "runeId",
		"");
		ReturnType<String> returnType = new ReturnType<String>();
		try{
			Role role = super.getCurrentRole(request);
			if(role==null){
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("您还没登录哦!");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			FragmentManager fragmentManager = (FragmentManager)HttpSpringUtils.getBean("fragmentManager");
			RuneManager runeManager = (RuneManager)HttpSpringUtils.getBean("runeManager");
			UserRuneManager userRuneManager = (UserRuneManager)HttpSpringUtils.getBean("userRuneManager");
			Rune rune = runeManager.getRuneById(Integer.parseInt(id));
			if(rune.getColor()==3){
				Fragment fragment = fragmentManager.getFragment(role.getRoleId());
				if(fragment.getFragment1()<rune.getFragment()){
					returnType.setValue(null);
					returnType.setStatus(0);
					returnType.setMsg("兑换星辰所需要的陨石不够!");
					super.printReturnType2Response(response, returnType);
					return null;
				}else{
					if(fragmentManager.delFragment(role.getRoleId(), rune.getFragment(), 0, 0)>0){
						userRuneManager.saveUserRune(Integer.parseInt(id), role.getRoleId());
					}else{
						returnType.setValue(null);
						returnType.setStatus(0);
						returnType.setMsg("兑换星辰出错了!");
						super.printReturnType2Response(response, returnType);
						return null;
					}
				}
				
			}else if(rune.getColor()==4){
				Fragment fragment = fragmentManager.getFragment(role.getRoleId());
				if(fragment.getFragment2()<rune.getFragment()){
					returnType.setValue(null);
					returnType.setStatus(0);
					returnType.setMsg("兑换星辰所需要的碎片不够!");
					super.printReturnType2Response(response, returnType);
					return null;
				}else{
					if(fragmentManager.delFragment(role.getRoleId(), 0, rune.getFragment(), 0)>0){
						userRuneManager.saveUserRune(Integer.parseInt(id), role.getRoleId());
					}else{
						returnType.setValue(null);
						returnType.setStatus(0);
						returnType.setMsg("兑换星辰出错了!");
						super.printReturnType2Response(response, returnType);
						return null;
					}
				}
			}else if(rune.getColor()==5){
				Fragment fragment = fragmentManager.getFragment(role.getRoleId());
				if(fragment.getFragment3()<rune.getFragment()){
					returnType.setValue(null);
					returnType.setStatus(0);
					returnType.setMsg("兑换星辰所需要的碎片不够!");
					super.printReturnType2Response(response, returnType);
					return null;
				}else{
					if(fragmentManager.delFragment(role.getRoleId(), 0, 0, rune.getFragment())>0){
						userRuneManager.saveUserRune(Integer.parseInt(id), role.getRoleId());
					}else{
						returnType.setValue(null);
						returnType.setStatus(0);
						returnType.setMsg("兑换星辰出错了!");
						super.printReturnType2Response(response, returnType);
						return null;
					}
				}
			}
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
			return null;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
