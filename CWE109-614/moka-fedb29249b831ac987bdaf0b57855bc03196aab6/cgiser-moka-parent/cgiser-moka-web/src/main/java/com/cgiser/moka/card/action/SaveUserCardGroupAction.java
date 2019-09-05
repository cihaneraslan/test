package com.cgiser.moka.card.action;

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
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.UserCardGroupManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class SaveUserCardGroupAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String cards = ServletUtil.getDefaultValue(request, "cards", "");
		String runes = ServletUtil.getDefaultValue(request, "runes", "");
		String groupId = ServletUtil.getDefaultValue(request, "groupId", null);
		ReturnType<Long> returnType = new ReturnType<Long>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(groupId==null){
				returnType.setMsg("卡组ID不能为空");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(StringUtils.isBlank(cards)&&StringUtils.isBlank(runes)){
				returnType.setMsg("参数有误");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			UserCardGroupManager userCardGroupManager = (UserCardGroupManager)HttpSpringUtils.getBean("userCardGroupManager");
			if(!role.getFreshStep()[2].equals("19")){
				RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
				roleManager.updateFreshStep(2, 19, role.getRoleId());
			}
			if(!role.getFreshStep()[4].equals("15")&&!StringUtils.isBlank(runes)){
				RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
				roleManager.updateFreshStep(4, 15, role.getRoleId());
			}
			if(userCardGroupManager.saveUserCardGroup(new Long(groupId), cards, runes)>0){
				returnType.setStatus(1);
				returnType.setValue(new Long(groupId));
			}else{
				returnType.setStatus(0);
			}
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
